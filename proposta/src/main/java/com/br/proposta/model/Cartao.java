package com.br.proposta.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.br.proposta.enumerator.StatusCartao;

@Entity
public class Cartao {

	@Id
	private String id;

	@Enumerated(EnumType.STRING)
	private StatusCartao status;

	public Cartao() {
	}

	public Cartao(StatusCartao status) {
		this.status = status;
	}

	public Cartao(Cartao cartao, StatusCartao status) {
		this.id = cartao.getId();
		this.status = status;
	}

	public Cartao(String id) {
		this.id = id;
		this.status = StatusCartao.ATIVO;
	}

	public String getId() {
		return id;
	}

	public StatusCartao getStatus() {
		return status;
	}

}
