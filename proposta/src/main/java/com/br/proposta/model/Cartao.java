package com.br.proposta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.br.proposta.enumerator.StatusCartao;

@Entity
public class Cartao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@NotBlank
	@Column(nullable = false)
	private String numeroCartao;
	
	@Enumerated(EnumType.STRING)
	private StatusCartao status;


	public Cartao() {}
	
	public Cartao(@NotBlank String numeroCartao) {
		this.numeroCartao = numeroCartao;
		this.status = StatusCartao.ATIVO;
	}
	
	


	public Cartao(Cartao cartao, StatusCartao status) {
		this.id = cartao.getId();
		this.numeroCartao = cartao.getNumeroCartao();
		this.status = status;
	}

	public Long getId() {
		return id;
	}


	public String getNumeroCartao() {
		return numeroCartao;
	}

	public StatusCartao getStatus() {
		return status;
	}
	
}
