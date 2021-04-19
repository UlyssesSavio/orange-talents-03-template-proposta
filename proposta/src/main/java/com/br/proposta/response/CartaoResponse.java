package com.br.proposta.response;

import com.br.proposta.model.Cartao;

public class CartaoResponse {

	private String numeroCartao;

	public CartaoResponse(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public CartaoResponse(Cartao cartao) {
		this.numeroCartao = cartao.getNumeroCartao();
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

}
