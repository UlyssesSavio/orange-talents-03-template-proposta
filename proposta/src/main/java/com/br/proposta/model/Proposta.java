package com.br.proposta.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
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
	@OneToOne()
	private Cartao cartao;

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
	
	

	public Proposta(Proposta pro, String encripta) {
		this.id = pro.getId();
		this.documento = encripta;
		this.email = pro.getEmail();
		this.nome = pro.getNome();
		this.endereco = pro.getEndereco();
		this.salario = pro.getSalario();
		this.status = pro.getStatus();
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

	public void adicionaCartaoValido(Cartao cartao) {
		this.cartao = cartao;
		cartaoElegivel();
	}

	public void cartaoElegivel() {
		this.status = StatusProposta.ELEGIVEL;

	}

	public Cartao getCartao() {
		return cartao;
	}
	

}
