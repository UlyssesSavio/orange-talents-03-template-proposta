package com.br.proposta;

import java.time.LocalDate;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.br.proposta.interfaces.CartaoServiceFeign;
import com.br.proposta.model.Cartao;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.request.AvisoViagemRequest;
import com.br.proposta.request.ViagemRequest;
import com.br.proposta.response.RespostaCartao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class TesteViagem {
	
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
	public void deveriaCadastrarAvisoViagem() throws JsonProcessingException, Exception {
		
		String numeroCartao = "2222-2222-2222-2222";
		
		
		Cartao cartao = new Cartao(numeroCartao);
		cartaoRepository.save(cartao);
		
		LocalDate dataTermino = LocalDate.of(2030, 1, 1);
		
		ViagemRequest request = new ViagemRequest("Chile", dataTermino);
		
		AvisoViagemRequest aviso = new AvisoViagemRequest(request.getDestino(), request.getDataTermino());
		
		Mockito.when(cartaoServiceFeign.avisos(numeroCartao, aviso)).thenReturn(new RespostaCartao("CADASTRADO"));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/viagem/"+numeroCartao)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("User-Agent", "Junit")
				.header("REMOTE_ADDR", "255.255.255.255")
				).andExpect(MockMvcResultMatchers.status().isOk());
		
		
	}
	
	
	@Test
	@Order(2)
	public void deveriaDarCartaoNaoEncontrado() throws JsonProcessingException, Exception {
		
		String numeroCartao = "2222-2222-2222-2222";
		
		
		
		LocalDate dataTermino = LocalDate.of(2030, 1, 1);
		
		ViagemRequest request = new ViagemRequest("Chile", dataTermino);
		
		AvisoViagemRequest aviso = new AvisoViagemRequest(request.getDestino(), request.getDataTermino());
		
		Mockito.when(cartaoServiceFeign.avisos(numeroCartao, aviso)).thenReturn(new RespostaCartao("CADASTRADO"));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/viagem/2222-2222-2222-2121")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("User-Agent", "Junit")
				.header("REMOTE_ADDR", "255.255.255.255")
				).andExpect(MockMvcResultMatchers.status().isNotFound());
		
		
	}

}
