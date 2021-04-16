package com.br.proposta.controller;

import java.net.URI;

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
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

@RestController
@RequestMapping("/carteira")
public class CarteiraDigitalController {

	private CarteiraDigitalRepository carteiraDigitalRepository;
	private CartaoRepository cartaoRepository;
	private CartaoServiceFeign cartaoServiceFeign;

	private CompositeMeterRegistry composite = new CompositeMeterRegistry();
	private Counter compositeCounter = composite.counter("carteira");

	public CarteiraDigitalController(CarteiraDigitalRepository carteiraDigitalRepository,
			CartaoRepository cartaoRepository, CartaoServiceFeign cartaoServiceFeign) {
		this.carteiraDigitalRepository = carteiraDigitalRepository;
		this.cartaoRepository = cartaoRepository;
		this.cartaoServiceFeign = cartaoServiceFeign;
	}

	@Transactional
	@PostMapping()
	private ResponseEntity<CarteiraDigitalResponse> cadastrar(@RequestParam String id,
			@RequestBody @Valid CarteiraDigitalRequest carteiraDigitalRequest, UriComponentsBuilder uriBuilder) {

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

		return ResponseEntity.created(uri).body(new CarteiraDigitalResponse(carteira));
	}

}
