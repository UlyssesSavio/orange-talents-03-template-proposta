package com.br.proposta.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Biometria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Lob
	@Column(nullable = false)
	private String biometria;

	@NotNull
	@ManyToOne
	private Cartao cartao;

	@CreationTimestamp
	private LocalDateTime dataAssociada;

	public Biometria() {
	}

	public Biometria(String biometria, Cartao cartao) {
		this.biometria = biometria;
		this.cartao = cartao;
	}

	public Long getId() {
		return id;
	}

	public String getBiometria() {
		return biometria;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public LocalDateTime getDataAssociada() {
		return dataAssociada;
	}

}
