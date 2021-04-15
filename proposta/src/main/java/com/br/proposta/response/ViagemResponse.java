package com.br.proposta.response;

import java.time.LocalDate;
import java.time.LocalDateTime;



import com.br.proposta.model.Viagem;

public class ViagemResponse {
	
	
	private String destino;	
	private LocalDate dataTermino;
	private LocalDateTime instanteAviso;
	private String ipCliente;
	private String userAgent;
	
	public ViagemResponse() {}
	
	public ViagemResponse(Viagem viagem) {
		this.destino = viagem.getDestino();
		this.dataTermino = viagem.getDataTermino();
		this.instanteAviso = viagem.getInstanteAviso();
		this.ipCliente = viagem.getIpCliente();
		this.userAgent = viagem.getUserAgent();
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
