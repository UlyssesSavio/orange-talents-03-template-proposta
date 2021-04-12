package com.br.proposta.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.br.proposta.model.CartaoSolicitado;



@FeignClient(name= "CartaoServiceFeign", url = "http://${endereco.cartao}:8888/")
public interface CartaoServiceFeign {
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/cartoes")
	CartaoSolicitado findByIdProposta(@RequestParam(value = "idProposta") Long idProposta) ;
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/cartoes/{id}")
	CartaoSolicitado findById(@PathVariable String id) ;

}
