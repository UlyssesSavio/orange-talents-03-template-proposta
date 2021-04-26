package com.br.proposta;


import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.br.proposta.interfaces.CartaoServiceFeign;
import com.br.proposta.model.Cartao;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.request.CartaoBloqueioRequest;
import com.br.proposta.response.RespostaCartao;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class TesteBloqueioCartao {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
		
	@Autowired
	CartaoRepository cartaoRepository;
	
		
	@MockBean
	CartaoServiceFeign cartaoServiceFeign;
	
	
	@Test
	@Order(1)
	public void deveriaBloquearCartao() throws Exception {
		String numeroCartao = "5555-5555-5555-5555";
		
		
		Cartao cartao = new Cartao(numeroCartao);
		cartaoRepository.save(cartao);
		
		CartaoBloqueioRequest cartaoBloqueioRequest = new CartaoBloqueioRequest("proposta");
		
		Mockito.when(cartaoServiceFeign.bloqueia(numeroCartao, new CartaoBloqueioRequest("proposta")))
		.thenReturn(new RespostaCartao("BLOQUEADO"));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/cartao/"+numeroCartao+"/bloqueio")
				.header("User-Agent", "Junit")
				.header("REMOTE_ADDR", "255.255.255.255")
				).andExpect(MockMvcResultMatchers.status().isCreated());
		
		
	}
	
	@Test
	@Order(2)
	public void deveriaNaoEncontrarCartao() throws Exception {
		String numeroCartao = "5555-5555-5555-5555";
		
		
		Cartao cartao = new Cartao(numeroCartao);
		cartaoRepository.save(cartao);
			
		mockMvc.perform(MockMvcRequestBuilders.post("/cartao/1111-1111-1111-1111/bloqueio")
				.header("User-Agent", "Junit")
				.header("REMOTE_ADDR", "255.255.255.255")
				).andExpect(MockMvcResultMatchers.status().isNotFound());
		
		
	}
	
	
	@Test
	@Order(3)
	public void deveriaDarCartaoRepetido() throws Exception {
		String numeroCartao = "5555-5555-5555-5555";
		
		
		Cartao cartao = new Cartao(numeroCartao);
		cartaoRepository.save(cartao);
		
		CartaoBloqueioRequest cartaoBloqueioRequest = new CartaoBloqueioRequest("proposta");
		
		Mockito.when(cartaoServiceFeign.bloqueia(numeroCartao, new CartaoBloqueioRequest("proposta")))
		.thenReturn(new RespostaCartao("BLOQUEADO"));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/cartao/"+numeroCartao+"/bloqueio")
				.header("User-Agent", "Junit")
				.header("REMOTE_ADDR", "255.255.255.255")
				).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
		
		
		
		
		
	}

}
