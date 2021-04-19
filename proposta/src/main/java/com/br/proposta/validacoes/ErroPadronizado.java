package com.br.proposta.validacoes;

import java.util.ArrayList;
import java.util.Collection;

public class ErroPadronizado {

	private Collection<String> mensagens;

	public ErroPadronizado(Collection<String> mensagens) {
		this.mensagens = mensagens;
	}

	public ErroPadronizado(String message) {
		mensagens = new ArrayList<String>();
		mensagens.add(message);
	}

	public Collection<String> getMensagens() {
		return mensagens;
	}

}
