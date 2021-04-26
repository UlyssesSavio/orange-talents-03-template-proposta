package com.br.proposta.request;

public class CartaoBloqueioRequest {

	private String sistemaResponsavel;

	public CartaoBloqueioRequest(String sistemaResponsavel) {
		this.sistemaResponsavel = sistemaResponsavel;
	}

	public String getSistemaResponsavel() {
		return sistemaResponsavel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sistemaResponsavel == null) ? 0 : sistemaResponsavel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartaoBloqueioRequest other = (CartaoBloqueioRequest) obj;
		if (sistemaResponsavel == null) {
			if (other.sistemaResponsavel != null)
				return false;
		} else if (!sistemaResponsavel.equals(other.sistemaResponsavel))
			return false;
		return true;
	}
	
	

}
