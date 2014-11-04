package br.com.redes.pop3cliente.app;

import java.io.IOException;
import java.util.List;

import br.com.redes.pop3cliente.service.POP3Cliente;
import br.com.redes.pop3cliente.model.Mensagem;;

public class AppPOP3 {

	public static void main(String[] args) throws IOException {
		
		POP3Cliente client = new POP3Cliente();
		
		client.setDebug(true);
		client.connect("pop.infoquality.com.br");
		client.login("vinicius@infoquality.com.br", "jesus12131415");
		
		System.out.println("Numero de novas mensagens: " + client.getNumberOfNewMessages());
		
		for(String cabecalho : client.getListaEmails()){
			System.out.println(cabecalho);
		}
		
		
		
		/*
		
		List<Mensagem> mensagens = client.getMessagesHeaders();
		
		for (int i = 0; i < mensagens.size(); i++) {
			System.out.println("--- Message num. " + i + " ---");
			System.out.println(mensagens.get(i).getHeaders().toString());
		}
		
		client.logout();
		client.disconnect();
	
		*/
	}

}
