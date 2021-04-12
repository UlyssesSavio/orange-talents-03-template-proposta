package com.br.proposta.request;

import java.util.Base64;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import com.br.proposta.model.Biometria;
import com.br.proposta.model.Cartao;
import com.br.proposta.validacoes.ApiErroException;

public class BiometriaRequest {

	@NotNull
	private String biometria;

	public BiometriaRequest() {
	}

	public BiometriaRequest(String biometria) {
		this.biometria = biometria;
	}

	public String getBiometria() {
		return biometria;
	}

	public Biometria converter(Cartao cartao) {

		String regex = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
		if(biometria.matches(regex)) {

			
			return new Biometria(biometria, cartao);
		}
		throw new ApiErroException(HttpStatus.BAD_REQUEST
				, "Biometria invalida");
		

	}

}
