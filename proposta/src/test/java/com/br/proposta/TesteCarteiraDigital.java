package com.br.proposta;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.br.proposta.enumerator.GatewayCarteira;
import com.br.proposta.request.CarteiraDigitalRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureDataJpa
public class TesteCarteiraDigital {

//	@Autowired
	MockMvc mockMvc;
	
	//@Autowired
	ObjectMapper jsonMapper;
	
	
	//@Test
	void deveriaCriarCarteiraDigitalPayPal() throws JsonProcessingException, Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/carteira")
				.param("id", "2539-9105-5315-3527")
				.content(toJson(new CarteiraDigitalRequest("ulysses@gmail.com", GatewayCarteira.Paypal)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "http://localhost/carteira/3"))
				
				;
	}
	
	private String toJson(CarteiraDigitalRequest request) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(request);
	}
	
}
