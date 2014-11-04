package br.com.redes.pop3cliente.app;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import br.com.redes.pop3cliente.service.POP3Cliente;
import br.com.redes.pop3cliente.model.Mensagem;;

public class AppPOP3 {

	public static void main(String[] args) throws IOException {

		Scanner teclado = new Scanner(System.in);
		POP3Cliente client = new POP3Cliente();

		client.setDebug(false);
		client.connect("pop.myserver.com");
		client.login("username@myserver.com", "123");

		System.out.println("Numero de novas mensagens: " + client.getNumberOfNewMessages());

		
		do{
			
			for(String cabecalho : client.getListaEmails()){
				System.out.println(cabecalho);
			}

			System.out.println("Digite o codigo de uma mensagem para ser exibida: ");
			Integer codigo = Integer.parseInt(teclado.nextLine());
			
			Mensagem m = client.getMessageCompleta(codigo);
			
			System.out.println(m.getBody());
			
			System.out.println("Pressione enter para continuar.");
        	teclado.nextLine();
			
		}while (teclado.hasNextLine());

		client.logout();
		client.disconnect();

	}

}
