package com.br.proposta.response;

import java.time.LocalDateTime;

import com.br.proposta.model.CartaoBloqueio;


public class CartaoBloqueioResponse {

	
	
	
	private CartaoResponse cartao;
	

	private LocalDateTime dataBloqueio;
	
	private String ipCliente;
	
	private String userAgent;
	
	

	public CartaoBloqueioResponse(CartaoBloqueio cartaoBloqueio) {
		this.cartao = new CartaoResponse(cartaoBloqueio.getCartao());
		this.dataBloqueio = cartaoBloqueio.getDataBloqueio();
		this.ipCliente = cartaoBloqueio.getIpCliente();
		this.userAgent = cartaoBloqueio.getUserAgent();
	}

	public CartaoResponse getCartao() {
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
