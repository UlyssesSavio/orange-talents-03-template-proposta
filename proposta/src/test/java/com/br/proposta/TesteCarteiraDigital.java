package com.br.proposta;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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

import com.br.proposta.enumerator.GatewayCarteira;
import com.br.proposta.interfaces.CartaoServiceFeign;
import com.br.proposta.model.Cartao;
import com.br.proposta.repository.CartaoRepository;
import com.br.proposta.request.CarteiraDigitalRequest;
import com.br.proposta.response.CarteiraDigitalResponseFeign;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class TesteCarteiraDigital {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CartaoRepository cartaoRepository;

	@MockBean
	CartaoServiceFeign cartaoServiceFeign;
	
	

	//@Test
	//@Order(1)
	public void deveriaCadastrarCarteiraDigital() throws Exception {

		String numeroCartao = "1111-1111-1111-1211";

		Cartao cartao = new Cartao(numeroCartao);
		cartaoRepository.save(cartao);
		
		CarteiraDigitalRequest request = new CarteiraDigitalRequest("teste@teste.com", GatewayCarteira.Paypal);
		CarteiraDigitalResponseFeign feign = Mockito.mock(CarteiraDigitalResponseFeign.class);
		
		
		
		Mockito.when(cartaoServiceFeign.carteiraDigital(numeroCartao, request))
		.thenReturn(new CarteiraDigitalResponseFeign("CADASTRADO","0000-0000-0000-0000"));
		
		
		
		
		Mockito.when(feign.getId()).thenReturn("0000-0000-0000-0000");

		mockMvc.perform(MockMvcRequestBuilders.post("/carteira/"+numeroCartao)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				).andExpect(MockMvcResultMatchers.status().isCreated());
		
	}

}
