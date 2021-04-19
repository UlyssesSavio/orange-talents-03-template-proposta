package com.br.proposta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.proposta.model.Cartao;
import com.br.proposta.model.CartaoBloqueio;

public interface CartaoBloqueadoRepository extends JpaRepository<CartaoBloqueio, Long> {

	boolean existsByCartao(Cartao cartao);

}
