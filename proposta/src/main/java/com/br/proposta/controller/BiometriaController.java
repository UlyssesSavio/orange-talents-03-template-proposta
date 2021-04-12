package com.br.proposta.controller;

import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.proposta.model.Biometria;
import com.br.proposta.model.Cartao;
import com.br.proposta.repository.BiometriaRepository;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.request.BiometriaRequest;
import com.br.proposta.response.BiometriaResponse;
import com.br.proposta.validacoes.ApiErroException;

@RestController
@RequestMapping("/biometria")
public class BiometriaController {

	private BiometriaRepository biometriaRepository;
	private CartaoRepository cartaoRepository;

	public BiometriaController(BiometriaRepository biometriaRepository, CartaoRepository cartaoRepository) {
		super();
		this.biometriaRepository = biometriaRepository;
		this.cartaoRepository = cartaoRepository;
	}

	@Transactional
	@PostMapping()
	private ResponseEntity<BiometriaResponse> cadastrar(@RequestParam String id, @RequestBody @Valid BiometriaRequest biometriaRequest, UriComponentsBuilder uriBuilder) {
		Cartao cartao = cartaoRepository.findByNumeroCartao(id);
		if(cartao==null) throw new ApiErroException(HttpStatus.NOT_FOUND, "Cartao invalido");
		
		
		Biometria biometria = biometriaRequest.converter(cartao);
		biometriaRepository.save(biometria);
		
		BiometriaResponse bioRes = new BiometriaResponse(biometria);
		URI uri = uriBuilder.path("/biometria/{id}").buildAndExpand(biometria.getId()).toUri();
		return ResponseEntity.created(uri).body(bioRes);
	}
	
	@GetMapping("/{id}")
	private ResponseEntity<BiometriaResponse> detalhar(@PathVariable Long id) {

		Optional<Biometria> biometria = biometriaRepository.findById(id);
		if (biometria.isPresent())
			return ResponseEntity.ok(new BiometriaResponse(biometria.get()));

		return ResponseEntity.notFound().build();
	}

}
