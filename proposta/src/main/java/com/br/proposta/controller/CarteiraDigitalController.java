package com.br.proposta.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.proposta.interfaces.CartaoServiceFeign;
import com.br.proposta.model.Cartao;
import com.br.proposta.model.CarteiraDigital;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.repository.CarteiraDigitalRepository;
import com.br.proposta.request.CarteiraDigitalRequest;
import com.br.proposta.response.CarteiraDigitalResponse;
import com.br.proposta.response.CarteiraDigitalResponseFeign;
import com.br.proposta.validacoes.ApiErroException;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@RequestMapping("/carteira")
public class CarteiraDigitalController {

	private CarteiraDigitalRepository carteiraDigitalRepository;
	private CartaoRepository cartaoRepository;
	private CartaoServiceFeign cartaoServiceFeign;

	private final MeterRegistry meterRegistry;
	private Counter compositeCounter;

	private Tracer tracer;

	public CarteiraDigitalController(CarteiraDigitalRepository carteiraDigitalRepository,
			CartaoRepository cartaoRepository, CartaoServiceFeign cartaoServiceFeign, MeterRegistry meterRegistry,
			Tracer tracer) {
		super();
		this.carteiraDigitalRepository = carteiraDigitalRepository;
		this.cartaoRepository = cartaoRepository;
		this.cartaoServiceFeign = cartaoServiceFeign;
		this.meterRegistry = meterRegistry;
		this.tracer = tracer;

		Collection<Tag> tags = new ArrayList<>();
		tags.add(Tag.of("carteira", "cadastro"));

		compositeCounter = this.meterRegistry.counter("carteira", tags);
	}

	@Transactional
	@PostMapping()
	public ResponseEntity<CarteiraDigitalResponse> cadastrar(@RequestParam String id,
			@RequestBody @Valid CarteiraDigitalRequest carteiraDigitalRequest, UriComponentsBuilder uriBuilder) {

		Span span = tracer.activeSpan();
		span.setTag("cartao.id", id);

		span.log("iniciando cadastro carteira digital");

		Cartao cartao = cartaoRepository.findByNumeroCartao(id);
		if (cartao == null)
			throw new ApiErroException(HttpStatus.NOT_FOUND, "Cartao invalido");
		CarteiraDigital carteira = carteiraDigitalRequest.converter(cartao);
		if (carteiraDigitalRepository.existsByCartaoAndGateway(cartao, carteira.getGateway()))
			throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY,
					"Ja existe uma carteira digital do " + carteira.getGateway() + ", para esse cartao.");

		CarteiraDigitalResponseFeign carteiraDigital = cartaoServiceFeign.carteiraDigital(id, carteiraDigitalRequest);
		carteira = new CarteiraDigital(carteira, carteiraDigital.getId());

		carteiraDigitalRepository.save(carteira);

		compositeCounter.increment();
		URI uri = uriBuilder.path("/carteira/{id}").buildAndExpand(carteira.getId()).toUri();
		span.log("finalizando cadastro carteira digital");

		return ResponseEntity.created(uri).body(new CarteiraDigitalResponse(carteira));
	}

}
