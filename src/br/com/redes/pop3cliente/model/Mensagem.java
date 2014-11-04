package br.com.redes.pop3cliente.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Mensagem{

	private final Map<String, List<String>> headers;

	private final String body;

	public Mensagem(Map<String, List<String>> headers, String body) {
		this.headers = Collections.unmodifiableMap(headers);
		this.body = body;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}



}