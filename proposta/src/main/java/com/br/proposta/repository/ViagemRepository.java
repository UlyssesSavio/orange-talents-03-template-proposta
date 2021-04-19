package com.br.proposta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.proposta.model.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Long> {

}
