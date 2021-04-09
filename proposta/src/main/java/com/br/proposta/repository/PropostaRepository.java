package com.br.proposta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.proposta.enumerator.StatusProposta;
import com.br.proposta.model.Proposta;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

	Optional<Proposta> findByDocumento(String documento);

	List<Proposta> findAllByStatus(StatusProposta naoElegivel);

}
