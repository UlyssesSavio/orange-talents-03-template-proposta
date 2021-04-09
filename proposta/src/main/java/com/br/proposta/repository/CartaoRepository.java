package com.br.proposta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.proposta.model.Cartao;

public interface CartaoRepository  extends JpaRepository<Cartao, Long>{

}
