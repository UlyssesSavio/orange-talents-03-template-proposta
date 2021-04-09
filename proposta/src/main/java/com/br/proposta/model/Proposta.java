package com.br.proposta.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.br.proposta.anotacoes.CpfCnpj;
import com.br.proposta.enumerator.StatusProposta;

@Entity
public class Proposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
	
	@Enumerated(EnumType.STRING)
	private StatusProposta status;

	public Proposta() {
	}

	public Proposta(@NotBlank String documento, @NotBlank @Email String email, @NotBlank String nome,
			@NotBlank String endereco, @NotNull @PositiveOrZero BigDecimal salario) {
		super();
		this.documento = documento;
		this.email = email;
		this.nome = nome;
		this.endereco = endereco;
		this.salario = salario;
		this.status = StatusProposta.NAO_ELEGIVEL;
	}
	
	public Proposta(Proposta proposta, StatusProposta status) {
		super();
		this.id = proposta.getId();
		this.documento = proposta.getDocumento();
		this.email = proposta.getEmail();
		this.nome = proposta.getNome();
		this.endereco = proposta.getEndereco();
		this.salario = proposta.getSalario();
		this.status = status;
	}

	public Long getId() {
		return id;
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

	public StatusProposta getStatus() {
		return status;
	}
	
	

}
