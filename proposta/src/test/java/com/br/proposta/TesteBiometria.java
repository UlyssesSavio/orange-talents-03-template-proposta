package com.br.proposta;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.br.proposta.model.Cartao;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.request.BiometriaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class TesteBiometria {
	
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
		
	@Autowired
	CartaoRepository cartaoRepository;
	
	@Test
	@Order(1)
	public void deveriaCadastraBiometria() throws Exception {
		String numeroCartao ="4444-4444-4444-4444";
		BiometriaRequest request = new BiometriaRequest("cG9sZWdhcg0K");
		
		Cartao cartao = new Cartao(numeroCartao);
		cartaoRepository.save(cartao);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/biometria/"+numeroCartao)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				).andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	@Test
	@Order(2)
	public void deveriaDarCartaoInvalido() throws Exception {
		String numeroCartao ="1444-4444-4444-4444";
		BiometriaRequest request = new BiometriaRequest("cG9sZWdhcg0K");
		
				
		mockMvc.perform(MockMvcRequestBuilders.post("/biometria/"+numeroCartao)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
