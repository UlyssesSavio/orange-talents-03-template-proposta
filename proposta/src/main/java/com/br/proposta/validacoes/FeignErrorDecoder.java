package com.br.proposta.validacoes;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {


		switch(response.status()) {
		case 422:
			throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartao com restricao");
		default :
				return new ApiErroException(HttpStatus.valueOf(response.status()), response.reason());
		
		}
		
	}

}
