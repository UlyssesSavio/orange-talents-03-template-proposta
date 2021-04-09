package com.br.proposta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Cartao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@NotBlank
	@Column(nullable = false)
	private String numeroCartao;


	public Cartao() {}
	
	public Cartao(@NotBlank String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}


	public Long getId() {
		return id;
	}


	public String getNumeroCartao() {
		return numeroCartao;
	}
	
	

}
