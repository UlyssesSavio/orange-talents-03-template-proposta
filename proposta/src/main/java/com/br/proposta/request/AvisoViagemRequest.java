package com.br.proposta.request;

import java.time.LocalDate;

public class AvisoViagemRequest {

	private String destino;
	private LocalDate validoAte;

	public AvisoViagemRequest(String destino, LocalDate validoAte) {
		this.destino = destino;
		this.validoAte = validoAte;
	}

	public AvisoViagemRequest() {
	}

	public String getDestino() {
		return destino;
	}

	public LocalDate getValidoAte() {
		return validoAte;
	}

}
