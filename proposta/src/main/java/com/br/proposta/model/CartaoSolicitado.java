package com.br.proposta.model;

public class CartaoSolicitado {
	
	private String id;

	public CartaoSolicitado() {}
	
	public CartaoSolicitado(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Cartao toCartao() {
		return new Cartao(id);
	}
	

}
