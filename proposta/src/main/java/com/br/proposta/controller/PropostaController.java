package com.br.proposta.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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

@RestController
@RequestMapping("/proposta")
public class PropostaController {

	private PropostaRepository propostaRepository;
	private CartaoRepository cartaoRepository;
	private SolicitaRestricaoCartaoFeign solicitaRestricaoCartaoFeign;
	private CartaoServiceFeign cartaoServiceFeign;

	public PropostaController(PropostaRepository propostaRepository, CartaoRepository cartaoRepository,
			SolicitaRestricaoCartaoFeign solicitaRestricaoCartaoFeign, CartaoServiceFeign cartaoServiceFeign) {
		this.propostaRepository = propostaRepository;
		this.cartaoRepository = cartaoRepository;
		this.solicitaRestricaoCartaoFeign = solicitaRestricaoCartaoFeign;
		this.cartaoServiceFeign = cartaoServiceFeign;
	}

	@Transactional
	@PostMapping
	private ResponseEntity<PropostaResponse> cadastrar(@RequestBody @Valid PropostaRequest propostaRequest,
			UriComponentsBuilder uriBuilder) {

		Proposta proposta = propostaRequest.converter();

		Optional<Proposta> existe = propostaRepository.findByDocumento(proposta.getDocumento());
		if (existe.isPresent())
			throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Ja existe uma proposta para esse documento.");

		propostaRepository.save(proposta);
		proposta = validaRestricao(proposta);
		propostaRepository.save(proposta);

		URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();

		return ResponseEntity.created(uri).body(new PropostaResponse(proposta));
	}

	@GetMapping("/{id}")
	private ResponseEntity<PropostaResponse> detalhar(@PathVariable Long id) {

		Optional<Proposta> proposta = propostaRepository.findById(id);
		if (proposta.isPresent())
			return ResponseEntity.ok(new PropostaResponse(proposta.get()));

		return ResponseEntity.notFound().build();
	}

	private Proposta validaRestricao(Proposta proposta) {

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
	private void verificaCartaoPeriodicamente() {
		List<Proposta> propostasNaoLegiveis = propostaRepository.findAllByStatus(StatusProposta.NAO_ELEGIVEL);
		propostasNaoLegiveis.forEach(proposta -> validaProposta(proposta));
	}

	@Transactional
	private void validaProposta(Proposta pro) {

		try {
			CartaoSolicitado cartaoS = cartaoServiceFeign.busca(pro.getId());
			Cartao cartao = cartaoS.toCartao();

			cartaoRepository.save(cartao);

			pro.adicionaCartaoValido(cartao);

			propostaRepository.save(pro);
		} catch (ApiErroException e) {
			throw e;
		}

	}

}
