package com.br.proposta.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.proposta.enumerator.StatusProposta;
import com.br.proposta.interfaces.CartaoServiceFeign;
import com.br.proposta.interfaces.SolicitaRestricaoCartaoFeign;
import com.br.proposta.model.Cartao;
import com.br.proposta.model.CartaoSolicitado;
import com.br.proposta.model.Proposta;
import com.br.proposta.model.SolicitaRestricaoCartao;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.repository.PropostaRepository;
import com.br.proposta.request.PropostaRequest;
import com.br.proposta.request.SolicitaRestricaoCartaoRequest;
import com.br.proposta.response.PropostaResponse;
import com.br.proposta.validacoes.ApiErroException;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@RequestMapping("/proposta")
public class PropostaController {

	@Value(value = "${documento.secret}")
	String salt;

	private PropostaRepository propostaRepository;
	private CartaoRepository cartaoRepository;
	private SolicitaRestricaoCartaoFeign solicitaRestricaoCartaoFeign;
	private CartaoServiceFeign cartaoServiceFeign;

	private final MeterRegistry meterRegistry;
	private Counter compositeCounter;
	private Tracer tracer;

	public PropostaController(PropostaRepository propostaRepository, CartaoRepository cartaoRepository,
			SolicitaRestricaoCartaoFeign solicitaRestricaoCartaoFeign, CartaoServiceFeign cartaoServiceFeign,
			MeterRegistry meterRegistry, Tracer tracer) {
		super();
		this.propostaRepository = propostaRepository;
		this.cartaoRepository = cartaoRepository;
		this.solicitaRestricaoCartaoFeign = solicitaRestricaoCartaoFeign;
		this.cartaoServiceFeign = cartaoServiceFeign;
		this.meterRegistry = meterRegistry;
		this.tracer = tracer;

		Collection<Tag> tags = new ArrayList<>();
		tags.add(Tag.of("proposta", "cadastro"));

		compositeCounter = this.meterRegistry.counter("proposta", tags);
	}

	@PostMapping
	public ResponseEntity<PropostaResponse> cadastrar(@RequestBody @Valid PropostaRequest propostaRequest,
			UriComponentsBuilder uriBuilder) {

		Span span = tracer.activeSpan();
		span.setTag("user.email", propostaRequest.getEmail());
		span.log("iniciando cadastro proposta");
		Proposta proposta = propostaRequest.converter();
		
		Optional<List<Proposta>> existe = propostaRepository.findAllByEmail(proposta.getEmail());
		if (existe.isPresent()){
			for(Proposta pro : existe.get()) {
				if(decripta(pro.getDocumento()).equals(proposta.getDocumento()))
					throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Ja existe uma proposta para esse documento.");
			}
		}
		
		propostaRepository.save(proposta);
		proposta = validaRestricao(proposta);
		proposta = new Proposta(proposta, encripta(proposta.getDocumento()));
		propostaRepository.save(proposta);

		URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();

		compositeCounter.increment();
		span.log("finalizando cadastro proposta");
		return ResponseEntity.created(uri).body(new PropostaResponse(proposta));
	}

	public String encripta(String texto) {

		TextEncryptor encode = Encryptors.text("secreto", salt);
		return encode.encrypt(texto);

	}

	public String decripta(String texto) {

		TextEncryptor encode = Encryptors.text("secreto", salt);
		return encode.decrypt(texto);

	}

	// Metodo get de retorno de proposta
	@GetMapping("/{id}")
	public ResponseEntity<PropostaResponse> detalhar(@PathVariable Long id) {

		Optional<Proposta> proposta = propostaRepository.findById(id);
		Optional<ResponseEntity<PropostaResponse>> resposta =proposta.map(pro ->ResponseEntity.ok(new PropostaResponse(pro))); 
		return resposta.orElseGet(() -> ResponseEntity.notFound().build());
	}
	public Proposta validaRestricao(Proposta proposta) {

		SolicitaRestricaoCartaoRequest cartaoRequest = new SolicitaRestricaoCartaoRequest(proposta.getDocumento(),
				proposta.getNome(), proposta.getId() + "");

		try {
			SolicitaRestricaoCartao restricao = solicitaRestricaoCartaoFeign.create(cartaoRequest);

		} catch (ApiErroException e) {
			if (e.getHttpStatus().equals(HttpStatus.UNPROCESSABLE_ENTITY))
				return proposta;
			else
				throw e;
		}
		proposta.cartaoElegivel();
		return proposta;
	}
	
	
	@Scheduled(fixedDelay = 5000)
	public void verificaCartaoPeriodicamente() {
		List<Proposta> propostasNaoLegiveis = propostaRepository.findAllByStatusAndCartao(StatusProposta.ELEGIVEL,
				null);
		propostasNaoLegiveis.forEach(proposta -> validaProposta(proposta));
	}

	public void validaProposta(Proposta pro) {

		try {
			Span span = tracer.activeSpan();
			span.log("iniciando valida proposta");

			span.setTag("user.email", pro.getEmail());

			CartaoSolicitado cartaoS = cartaoServiceFeign.findByIdProposta(pro.getId());
			Cartao cartao = cartaoS.toCartao();

			
			cartaoRepository.save(cartao);
			pro.adicionaCartaoValido(cartao);
			propostaRepository.save(pro);
			
			
			span.log("terminando valida proposta");
		} catch (ApiErroException e) {
		}

	}

}
