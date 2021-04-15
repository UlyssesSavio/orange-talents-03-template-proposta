package com.br.proposta.request;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.br.proposta.model.Cartao;
import com.br.proposta.model.Viagem;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ViagemRequest {

	@NotNull
	@NotBlank
	@Column(nullable = false)
	private String destino;

	@NotNull
	@Column(nullable = false)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Future
	private LocalDate dataTermino;

	public ViagemRequest() {
	}

	public ViagemRequest(@NotNull @NotBlank String destino, @NotNull LocalDate dataTermino) {
		this.destino = destino;
		this.dataTermino = dataTermino;
	}

	public String getDestino() {
		return destino;
	}

	public LocalDate getDataTermino() {
		return dataTermino;
	}

	public Viagem converter(Cartao cartao, String ipCliente, String userAgent) {

		return new Viagem(cartao, destino, dataTermino, ipCliente, userAgent);

	}

}
