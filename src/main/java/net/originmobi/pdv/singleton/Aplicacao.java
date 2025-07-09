package net.originmobi.pdv.singleton;

import org.springframework.security.core.context.SecurityContextHolder;

public class Aplicacao {

	private static Aplicacao aplicacao;
	private String usuarioAtual;

	public Aplicacao() {
		try {
			if (SecurityContextHolder.getContext() != null && 
				SecurityContextHolder.getContext().getAuthentication() != null) {
				usuarioAtual = SecurityContextHolder.getContext().getAuthentication().getName();
			} else {
				usuarioAtual = "sistema"; // Valor padrão para testes
			}
		} catch (Exception e) {
			usuarioAtual = "sistema"; // Fallback em caso de erro
		}
	}

	public static synchronized Aplicacao getInstancia() {
		if (aplicacao == null) {
			aplicacao = new Aplicacao();
		}
		return aplicacao;
	}

	public String getUsuarioAtual() {
		return usuarioAtual;
	}
	
}
