package com.br.proposta.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.proposta.enumerator.StatusCartao;
import com.br.proposta.interfaces.CartaoServiceFeign;
import com.br.proposta.model.Cartao;
import com.br.proposta.model.Viagem;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.repository.ViagemRepository;
import com.br.proposta.request.AvisoViagemRequest;
import com.br.proposta.request.ViagemRequest;
import com.br.proposta.response.RespostaCartao;
import com.br.proposta.response.ViagemResponse;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@RequestMapping("/viagem")
public class ViagemController {

	private ViagemRepository viagemRepository;
	private CartaoRepository cartaoRepository;
	private CartaoServiceFeign cartaoServiceFeign;

	private final MeterRegistry meterRegistry;
	private Counter compositeCounter;

	private Tracer tracer;

	public ViagemController(ViagemRepository viagemRepository, CartaoRepository cartaoRepository,
			CartaoServiceFeign cartaoServiceFeign, MeterRegistry meterRegistry, Tracer tracer) {
		super();
		this.viagemRepository = viagemRepository;
		this.cartaoRepository = cartaoRepository;
		this.cartaoServiceFeign = cartaoServiceFeign;
		this.meterRegistry = meterRegistry;
		this.tracer = tracer;

		Collection<Tag> tags = new ArrayList<>();
		tags.add(Tag.of("viagem", "cadastro"));
		compositeCounter = this.meterRegistry.counter("viagem", tags);

	}

	@Transactional
	@PostMapping("/{id}")
	public ResponseEntity<ViagemResponse> cadastrar(@PathVariable(value = "id") String id,
			@RequestBody @Valid ViagemRequest viagemRequest, HttpServletRequest request) {

		Span span = tracer.activeSpan();
		span.setTag("cartao.id", id);

		span.log("iniciando cadastro aviso viagem");

		Optional<Cartao> cartaoOp = cartaoRepository.findById(id);

		if (!cartaoOp.isPresent())
			return ResponseEntity.notFound().build();
		
		Cartao cartao = cartaoOp.get();
		
		if (cartao.getStatus() == StatusCartao.BLOQUEADO)
			return ResponseEntity.unprocessableEntity().build();

		Viagem viagem = new Viagem(cartao, viagemRequest, request.getRemoteAddr(), request.getHeader("User-Agent"));

		AvisoViagemRequest avisoViagemRequest = new AvisoViagemRequest(viagem.getDestino(), viagem.getDataTermino());
		RespostaCartao avisos = cartaoServiceFeign.avisos(id, avisoViagemRequest);

		viagemRepository.save(viagem);
		compositeCounter.increment();
		span.log("finalizando cadastro aviso viagem");
		return ResponseEntity.ok(new ViagemResponse(viagem));
	}

}
