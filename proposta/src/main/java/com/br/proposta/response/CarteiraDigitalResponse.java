package com.br.proposta.response;

import com.br.proposta.enumerator.GatewayCarteira;
import com.br.proposta.model.CarteiraDigital;

public class CarteiraDigitalResponse {

	private String email;
	private GatewayCarteira carteira;

	public CarteiraDigitalResponse() {
	}

	public CarteiraDigitalResponse(CarteiraDigital carteiraDigital) {
		this.email = carteiraDigital.getEmail();
		this.carteira = carteiraDigital.getGateway();
	}

	public String getEmail() {
		return email;
	}

	public GatewayCarteira getCarteira() {
		return carteira;
	}

}
