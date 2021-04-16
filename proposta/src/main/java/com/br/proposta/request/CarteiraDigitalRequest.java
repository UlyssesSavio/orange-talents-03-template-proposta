package com.br.proposta.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.br.proposta.enumerator.GatewayCarteira;
import com.br.proposta.model.Cartao;
import com.br.proposta.model.CarteiraDigital;

public class CarteiraDigitalRequest {
	
	@NotNull
	@Email
	private String email;
	@NotNull
	private GatewayCarteira gateway;
	
	public CarteiraDigitalRequest() {}
	
	public CarteiraDigitalRequest(String email, GatewayCarteira gateway) {
		this.email = email;
		this.gateway = gateway;
	}

	public String getEmail() {
		return email;
	}
	
	public GatewayCarteira getGateway() {
		return gateway;
	}

	public CarteiraDigital converter(Cartao cartao) {
		return new CarteiraDigital(cartao, email, gateway);
	}
	
	
	

}
