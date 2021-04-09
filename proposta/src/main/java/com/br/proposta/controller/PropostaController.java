package com.br.proposta.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.proposta.model.Proposta;
import com.br.proposta.repository.PropostaRepository;
import com.br.proposta.request.PropostaRequest;
import com.br.proposta.response.PropostaResponse;

@RestController
@RequestMapping("/proposta")
public class PropostaController {

	private PropostaRepository propostaRepository;

	public PropostaController(PropostaRepository propostaRepository) {
		this.propostaRepository = propostaRepository;
	}

	@Transactional
	@PostMapping
	private ResponseEntity<PropostaResponse> cadastrar(@RequestBody @Valid PropostaRequest propostaRequest,
			UriComponentsBuilder uriBuilder) {

		Proposta proposta = propostaRequest.converter();
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

}
