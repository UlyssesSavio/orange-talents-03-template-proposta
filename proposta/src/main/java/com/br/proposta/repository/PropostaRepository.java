package com.br.proposta.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.br.proposta.model.Proposta;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

	Optional<Proposta> findByDocumento(String documento);

}
