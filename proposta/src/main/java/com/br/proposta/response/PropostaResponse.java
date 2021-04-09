package com.br.proposta.response;

import java.math.BigDecimal;



import com.br.proposta.model.Proposta;

public class PropostaResponse {
	
	private String email;


	private String nome;


	private String endereco;


	private BigDecimal salario;
	
	
	
	public PropostaResponse() {}

	public PropostaResponse(Proposta proposta) {
		
		this.email = proposta.getEmail();
		this.nome = proposta.getNome();
		this.endereco = proposta.getEndereco();
		this.salario = proposta.getSalario();
	}


	public String getEmail() {
		return email;
	}


	public String getNome() {
		return nome;
	}


	public String getEndereco() {
		return endereco;
	}


	public BigDecimal getSalario() {
		return salario;
	}
	
	

}
