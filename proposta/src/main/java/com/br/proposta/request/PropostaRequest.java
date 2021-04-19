package com.br.proposta.request;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.br.proposta.anotacoes.CpfCnpj;
import com.br.proposta.model.Proposta;

public class PropostaRequest {

	@NotBlank
	@CpfCnpj
	@Column(nullable = false)
	private String documento;

	@NotBlank
	@Email
	@Column(nullable = false)
	private String email;

	@NotBlank
	@Column(nullable = false)
	private String nome;

	@Lob
	@NotBlank
	@Column(nullable = false)
	private String endereco;

	@NotNull
	@PositiveOrZero
	@Column(nullable = false)
	private BigDecimal salario;

	public PropostaRequest() {

	}

	public PropostaRequest(@NotBlank String documento, @NotBlank @Email String email, @NotBlank String nome,
			@NotBlank String endereco, @NotNull @PositiveOrZero BigDecimal salario) {
		super();
		this.documento = documento;
		this.email = email;
		this.nome = nome;
		this.endereco = endereco;
		this.salario = salario;
	}

	public String getDocumento() {
		return documento;
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

	public Proposta converter() {

		documento = documento.replaceAll("[^0-9]+", "");

		return new Proposta(documento, email, nome, endereco, salario);
	}

}
