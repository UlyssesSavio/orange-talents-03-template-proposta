package com.br.proposta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.proposta.enumerator.GatewayCarteira;
import com.br.proposta.model.Cartao;
import com.br.proposta.model.CarteiraDigital;

public interface CarteiraDigitalRepository extends JpaRepository<CarteiraDigital, Long> {

	boolean existsByCartaoAndGateway(Cartao cartao, GatewayCarteira gateway);

}
