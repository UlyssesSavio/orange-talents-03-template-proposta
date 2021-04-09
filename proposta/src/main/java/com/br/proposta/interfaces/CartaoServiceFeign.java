package com.br.proposta.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.br.proposta.model.CartaoSolicitado;



@FeignClient(name= "CartaoServiceFeign", url = "http://localhost:8888/")
public interface CartaoServiceFeign {
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/cartoes")
	CartaoSolicitado busca(@RequestParam(value = "idProposta") Long idProposta) ;

}
