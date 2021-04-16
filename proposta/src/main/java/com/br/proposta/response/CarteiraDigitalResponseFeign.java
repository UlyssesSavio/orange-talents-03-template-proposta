package com.br.proposta.response;

public class CarteiraDigitalResponseFeign {

	private String resultado;
	private String id;
	
	public CarteiraDigitalResponseFeign() {}
	
	public CarteiraDigitalResponseFeign(String resultado, String id) {
		this.resultado = resultado;
		this.id = id;
	}

	public String getResultado() {
		return resultado;
	}

	public String getId() {
		return id;
	}
	
	
	
	
}
