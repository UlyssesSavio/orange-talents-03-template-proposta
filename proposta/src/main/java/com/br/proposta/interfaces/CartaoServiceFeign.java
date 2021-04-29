package com.br.proposta.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.br.proposta.model.CartaoSolicitado;
import com.br.proposta.request.AvisoViagemRequest;
import com.br.proposta.request.CartaoBloqueioRequest;
import com.br.proposta.request.CarteiraDigitalRequest;
import com.br.proposta.response.CarteiraDigitalResponseFeign;
import com.br.proposta.response.RespostaCartao;

import io.opentracing.contrib.spring.cloud.feign.FeignTracingAutoConfiguration;

@FeignClient(name = "CartaoServiceFeign", url = "http://${endereco.cartao}:8888/")
public interface CartaoServiceFeign {

	@RequestMapping(method = RequestMethod.GET, value = "/api/cartoes")
	CartaoSolicitado findByIdProposta(@RequestParam(value = "idProposta") Long idProposta);

	@RequestMapping(method = RequestMethod.GET, value = "/api/cartoes/{id}")
	CartaoSolicitado findById(@PathVariable String id);

	@RequestMapping(method = RequestMethod.POST, value = "/api/cartoes/{id}/bloqueios", consumes = "application/json")
	RespostaCartao bloqueia(@PathVariable String id, CartaoBloqueioRequest cartaoBloqueioRequest);

	@RequestMapping(method = RequestMethod.POST, value = "/api/cartoes/{id}/avisos", consumes = "application/json")
	RespostaCartao avisos(@PathVariable String id, AvisoViagemRequest avisoViagemRequest);

	@RequestMapping(method = RequestMethod.POST, value = "/api/cartoes/{id}/carteiras", consumes = "application/json")
	CarteiraDigitalResponseFeign carteiraDigital(@PathVariable String id,
			CarteiraDigitalRequest carteiraDigitalRequest);

}
