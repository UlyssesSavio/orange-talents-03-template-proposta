package com.br.proposta.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.br.proposta.model.SolicitaRestricaoCartao;
import com.br.proposta.request.SolicitaRestricaoCartaoRequest;

import io.opentracing.contrib.spring.cloud.feign.FeignTracingAutoConfiguration;

@FeignClient(name = "solicitaRestricaoCartaoFeign", url = "http://${endereco.restricao}:9999/")
public interface SolicitaRestricaoCartaoFeign {

	@RequestMapping(method = RequestMethod.POST, value = "/api/solicitacao", consumes = "application/json")
	SolicitaRestricaoCartao create(SolicitaRestricaoCartaoRequest request);

}
