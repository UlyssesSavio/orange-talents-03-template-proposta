package com.br.proposta.response;



import java.time.LocalDateTime;
import java.util.Base64;

import com.br.proposta.model.Biometria;


public class BiometriaResponse {
	
	
	
	private String biometria;
	
	
	private CartaoResponse cartao;
	
	private LocalDateTime dataAssociada;
	
	

	public BiometriaResponse(String biometria, CartaoResponse cartao, LocalDateTime dataAssociada) {
		super();
		this.biometria = biometria;
		this.cartao = cartao;
		this.dataAssociada = dataAssociada;
	}

	public BiometriaResponse(Biometria biometria2) {
		byte[] aux = Base64.getDecoder().decode(biometria2.getBiometria());
		this.biometria = new String(aux);;
		this.dataAssociada = biometria2.getDataAssociada();
		this.cartao = new CartaoResponse(biometria2.getCartao());
	}

	public String getBiometria() {
		return biometria;
	}

	public CartaoResponse getCartao() {
		return cartao;
	}

	public LocalDateTime getDataAssociada() {
		return dataAssociada;
	}
	
	

}
