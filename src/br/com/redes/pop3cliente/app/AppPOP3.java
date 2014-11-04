package br.com.redes.pop3cliente.app;

import java.io.IOException;
import java.util.List;

import br.com.redes.pop3cliente.service.POP3Cliente;
import br.com.redes.pop3cliente.model.Mensagem;;

public class AppPOP3 {

	public static void main(String[] args) throws IOException {
		POP3Cliente client = new POP3Cliente();
		client.setDebug(true);
		client.connect("pop3.myserver.com");
		client.login("name@myserver.com", "password");
		System.out.println("Number of new emails: " + client.getNumberOfNewMessages());
		List<Mensagem> messages = client.getMessages();
		for (int index = 0; index < messages.size(); index++) {
			System.out.println("--- Message num. " + index + " ---");
			System.out.println(messages.get(index).getBody());
		}
		client.logout();
		client.disconnect();
	}

}
