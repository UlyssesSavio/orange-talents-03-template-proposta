package com.br.proposta.controller;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.proposta.enumerator.StatusCartao;
import com.br.proposta.interfaces.CartaoServiceFeign;
import com.br.proposta.model.Cartao;
import com.br.proposta.model.CartaoBloqueio;
import com.br.proposta.repository.CartaoBloqueadoRepository;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.request.CartaoBloqueioRequest;
import com.br.proposta.response.CartaoBloqueioResponse;
import com.br.proposta.response.RespostaCartao;
import com.br.proposta.validacoes.ApiErroException;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

@RestController
@RequestMapping("/cartao")
public class CartaoController {

	
	private CartaoRepository cartaoRepository;
	private CartaoBloqueadoRepository cartaoBloqueadoRepository;
	private CartaoServiceFeign cartaoServiceFeign;
	
	
	
	CompositeMeterRegistry composite = new CompositeMeterRegistry();

	Counter compositeCounter = composite.counter("cartao");




	public CartaoController(CartaoRepository cartaoRepository, CartaoBloqueadoRepository cartaoBloqueadoRepository,
			CartaoServiceFeign cartaoServiceFeign) {
		this.cartaoRepository = cartaoRepository;
		this.cartaoBloqueadoRepository = cartaoBloqueadoRepository;
		this.cartaoServiceFeign = cartaoServiceFeign;
	}

	@Transactional
	@PostMapping("/bloqueio")
	private ResponseEntity<CartaoBloqueioResponse> cadastrar(@RequestParam String id, UriComponentsBuilder uriBuilder, HttpServletRequest request){
		
		
		if(!cartaoRepository.existsByNumeroCartao(id)) {
			return ResponseEntity.notFound().build();
		}
		
		Cartao cartao = cartaoRepository.findByNumeroCartao(id);
		
		if(cartaoBloqueadoRepository.existsByCartao(cartao)) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		CartaoBloqueio cartaoBloqueio = new CartaoBloqueio(cartao, request.getRemoteAddr(), request.getHeader("User-Agent"));
		
		try {
		CartaoBloqueioRequest cartaoBloqueioRequest = new CartaoBloqueioRequest("proposta");	
		RespostaCartao bloqueia = cartaoServiceFeign.bloqueia(id, cartaoBloqueioRequest);
		}catch (Exception e) {
			throw new ApiErroException(HttpStatus.BAD_REQUEST, e.getMessage()); 
		}
		
		alteraEstadoCartao(StatusCartao.BLOQUEADO, cartao);
		cartaoBloqueadoRepository.save(cartaoBloqueio);
		
		
		URI uri = uriBuilder.path("/cartao/bloqueio/{id}").buildAndExpand(cartaoBloqueio.getId()).toUri();
		
		compositeCounter.increment();
		return ResponseEntity.created(uri).body(new CartaoBloqueioResponse(cartaoBloqueio));
	}
	
	@GetMapping("/bloqueio/{id}")
	private ResponseEntity<CartaoBloqueioResponse> busca(@PathVariable Long id){
		Optional<CartaoBloqueio> cartao = cartaoBloqueadoRepository.findById(id);
		if(cartao.isPresent())
			return ResponseEntity.ok(new CartaoBloqueioResponse(cartao.get()));
		return ResponseEntity.notFound().build();
	}
	
	private void alteraEstadoCartao(StatusCartao status, Cartao cartao) {
		cartao = new Cartao(cartao, status);
		cartaoRepository.save(cartao);
	}
	
	
}
