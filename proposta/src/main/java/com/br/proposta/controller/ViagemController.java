package com.br.proposta.controller;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.proposta.enumerator.StatusCartao;
import com.br.proposta.model.Cartao;
import com.br.proposta.model.Viagem;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.repository.ViagemRepository;
import com.br.proposta.request.ViagemRequest;
import com.br.proposta.response.ViagemResponse;

@RestController
@RequestMapping("/viagem")
public class ViagemController {

	private ViagemRepository viagemRepository;
	private CartaoRepository cartaoRepository;

	public ViagemController(ViagemRepository viagemRepository, CartaoRepository cartaoRepository) {
		this.viagemRepository = viagemRepository;
		this.cartaoRepository = cartaoRepository;
	}

	@Transactional
	@PostMapping
	private ResponseEntity<ViagemResponse> cadastrar(@RequestParam String id,
			@RequestBody @Valid ViagemRequest viagemRequest, HttpServletRequest request) {
		Cartao cartao = cartaoRepository.findByNumeroCartao(id);

		if (cartao == null)
			return ResponseEntity.notFound().build();
		if (cartao.getStatus() == StatusCartao.BLOQUEADO)
			return ResponseEntity.unprocessableEntity().build();

		Viagem viagem = new Viagem(cartao, viagemRequest, request.getRemoteAddr(), request.getHeader("User-Agent"));
		viagemRepository.save(viagem);

		return ResponseEntity.ok(new ViagemResponse(viagem));
	}

}
