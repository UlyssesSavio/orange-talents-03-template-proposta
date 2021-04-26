package com.br.proposta;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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

import com.br.proposta.interfaces.SolicitaRestricaoCartaoFeign;
import com.br.proposta.model.Proposta;
import com.br.proposta.model.SolicitaRestricaoCartao;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.request.PropostaRequest;
import com.br.proposta.request.SolicitaRestricaoCartaoRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class TesteProposta {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	CartaoRepository cartaoRepository;
	
	@MockBean
	SolicitaRestricaoCartaoFeign solicitaRestricaoCartaoFeign;

	@Test
	public void deveriaCriarProposta() throws Exception{

		PropostaRequest request = new PropostaRequest("20470847093", "jao@jao.com", "joao", "Rua joao pedro", BigDecimal.valueOf(100.0));
		
						
		mockMvc.perform(MockMvcRequestBuilders.post("/proposta")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isCreated());				
	
	}
	
	@Test
	public void deveriaNaoCriarPropostaDadosInvalidos() throws JsonProcessingException, Exception {
		PropostaRequest request = new PropostaRequest("", "jao email", "joao", "Rua joao pedro", BigDecimal.valueOf(100.0));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/proposta")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		
	}
	
}
