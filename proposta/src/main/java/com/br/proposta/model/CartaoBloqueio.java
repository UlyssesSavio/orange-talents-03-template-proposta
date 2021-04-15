package com.br.proposta.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class CartaoBloqueio {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne
	private Cartao cartao;
	
	@CreationTimestamp
	private LocalDateTime dataBloqueio;
	
	@NotNull
	@Column(nullable = false)
	private String ipCliente;
	
	@NotNull
	@Column(nullable = false)
	private String userAgent;
	
	public CartaoBloqueio(){}



	public CartaoBloqueio(@NotNull Cartao cartao, @NotNull String ipCliente, @NotNull String userAgent) {
		this.cartao = cartao;
		this.ipCliente = ipCliente;
		this.userAgent = userAgent;
	}



	public Long getId() {
		return id;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public LocalDateTime getDataBloqueio() {
		return dataBloqueio;
	}

	public String getIpCliente() {
		return ipCliente;
	}

	public String getUserAgent() {
		return userAgent;
	}
	
	
	
	
	
}
