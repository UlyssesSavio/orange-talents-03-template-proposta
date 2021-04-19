package com.br.proposta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.br.proposta.enumerator.GatewayCarteira;

@Entity
public class CarteiraDigital {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	private Cartao cartao;

	@Email
	@NotNull
	@Column(nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	private GatewayCarteira gateway;

	private String idCarteiraDigital;

	public CarteiraDigital() {
	}

	public CarteiraDigital(@NotNull Cartao cartao, @Email @NotNull String email, GatewayCarteira gateway) {
		this.cartao = cartao;
		this.email = email;
		this.gateway = gateway;
	}

	public CarteiraDigital(CarteiraDigital carteira, String id2) {
		this.cartao = carteira.getCartao();
		this.email = carteira.getEmail();
		this.gateway = carteira.getGateway();
		this.idCarteiraDigital = id2;
	}

	public Long getId() {
		return id;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public String getEmail() {
		return email;
	}

	public GatewayCarteira getGateway() {
		return gateway;
	}

	public String getIdCarteiraDigital() {
		return idCarteiraDigital;
	}

}
