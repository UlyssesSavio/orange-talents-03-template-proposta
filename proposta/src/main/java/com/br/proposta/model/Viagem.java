package com.br.proposta.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import com.br.proposta.request.ViagemRequest;

@Entity
public class Viagem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cartao cartao;

	@NotNull
	@NotBlank
	@Column(nullable = false)
	private String destino;

	@NotNull
	@Column(nullable = false)
	@Future
	private LocalDate dataTermino;
	@CreationTimestamp
	private LocalDateTime instanteAviso;

	@NotNull
	@Column(nullable = false)
	private String ipCliente;

	@NotNull
	@Column(nullable = false)
	private String userAgent;

	public Viagem() {
	}

	public Viagem(Cartao cartao, @NotNull @NotBlank String destino, @NotNull LocalDate dataTermino,
			@NotNull String ipCliente, @NotNull String userAgent) {
		this.cartao = cartao;
		this.destino = destino;
		this.dataTermino = dataTermino;
		this.ipCliente = ipCliente;
		this.userAgent = userAgent;
	}

	public Viagem(Cartao cartao, @Valid ViagemRequest viagemRequest, String remoteAddr, String header) {
		this.cartao = cartao;
		this.destino = viagemRequest.getDestino();
		this.dataTermino = viagemRequest.getDataTermino();
		this.ipCliente = remoteAddr;
		this.userAgent = header;
	}

	public Long getId() {
		return id;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public String getDestino() {
		return destino;
	}

	public LocalDate getDataTermino() {
		return dataTermino;
	}

	public LocalDateTime getInstanteAviso() {
		return instanteAviso;
	}

	public String getIpCliente() {
		return ipCliente;
	}

	public String getUserAgent() {
		return userAgent;
	}

}
