package com.br.proposta.request;

public class CartaoBloqueioRequest {

	private String sistemaResponsavel;

	public CartaoBloqueioRequest(String sistemaResponsavel) {
		this.sistemaResponsavel = sistemaResponsavel;
	}

	public String getSistemaResponsavel() {
		return sistemaResponsavel;
	}

}
