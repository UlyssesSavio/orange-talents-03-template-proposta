package com.br.proposta.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.proposta.model.Biometria;
import com.br.proposta.model.Cartao;
import com.br.proposta.repository.BiometriaRepository;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.request.BiometriaRequest;
import com.br.proposta.response.BiometriaResponse;
import com.br.proposta.validacoes.ApiErroException;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@RequestMapping("/biometria")
public class BiometriaController {

	private BiometriaRepository biometriaRepository;
	private CartaoRepository cartaoRepository;

	private Tracer tracer;

	private final MeterRegistry meterRegistry;
	private Counter compositeCounter;

	public BiometriaController(BiometriaRepository biometriaRepository, CartaoRepository cartaoRepository,
			Tracer tracer, MeterRegistry meterRegistry) {
		super();
		this.biometriaRepository = biometriaRepository;
		this.cartaoRepository = cartaoRepository;
		this.tracer = tracer;
		this.meterRegistry = meterRegistry;

		Collection<Tag> tags = new ArrayList<>();
		tags.add(Tag.of("biometria", "cadastro"));
		compositeCounter = this.meterRegistry.counter("biometria", tags);

	}

	@Transactional
	@PostMapping("/{id}")
	public ResponseEntity<BiometriaResponse> cadastrar(@PathVariable(value = "id") String id,
			@RequestBody @Valid BiometriaRequest biometriaRequest, UriComponentsBuilder uriBuilder) {

		Span span = tracer.activeSpan();
		span.setTag("cartao.id", id);
		span.log("iniciando cadastro biometria");
		Optional<Cartao> cartaoOp = cartaoRepository.findById(id);
		
		if (!cartaoOp.isPresent())
			throw new ApiErroException(HttpStatus.NOT_FOUND, "Cartao invalido");

		Cartao cartao = cartaoOp.get();
		Biometria biometria = biometriaRequest.converter(cartao);
		biometriaRepository.save(biometria);

		BiometriaResponse bioRes = new BiometriaResponse(biometria);
		URI uri = uriBuilder.path("/biometria/{id}").buildAndExpand(biometria.getId()).toUri();
		compositeCounter.increment();
		span.log("finalizando cadastro biometria");
		return ResponseEntity.created(uri).body(bioRes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BiometriaResponse> detalhar(@PathVariable Long id) {

		Optional<Biometria> biometria = biometriaRepository.findById(id);
		Optional<ResponseEntity<BiometriaResponse>> retorno = biometria.map(bio -> ResponseEntity.ok(new BiometriaResponse(biometria.get())));
		return retorno.orElseGet(() -> ResponseEntity.notFound().build());
	
	}

}
