package br.com.redes.pop3cliente.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.redes.pop3cliente.model.Mensagem;

public class POP3Cliente{

	private Socket socket;

	private boolean debug = false;

	private BufferedReader reader;
	private BufferedWriter writer;

	private static final int DEFAULT_PORT = 110;

	public void connect(String host) throws IOException {

		socket = new Socket();
		socket.connect(new InetSocketAddress(host, DEFAULT_PORT));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		if (debug)
			System.out.println("Connected to the host");

		readResponseLine();
	}

	public void disconnect() throws IOException {

		if (!isConnected())
			throw new IllegalStateException("Not connected to a host");

		socket.close();
		reader = null;
		writer = null;

		if (debug)
			System.out.println("Disconnected from the host");
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	protected String readResponseLine() throws IOException{

		String response = reader.readLine();

		if (debug) {
			System.out.println("DEBUG [in] : " + response);
		}

		if (response.startsWith("-ERR"))
			throw new RuntimeException("Server has returned an error: " + response.replaceFirst("-ERR ", ""));

		return response;
	}

	protected String sendCommand(String command) throws IOException {

		if (debug) {
			System.out.println("DEBUG [out]: " + command);
		}

		writer.write(command + "\n");
		writer.flush();
		return readResponseLine();
	}

	public void login(String username, String password) throws IOException {
		sendCommand("USER " + username);
		sendCommand("PASS " + password);
	}

	public void logout() throws IOException {
		sendCommand("QUIT");
	}

	public int getNumberOfNewMessages() throws IOException {

		String response = sendCommand("STAT");
		String[] values = response.split(" ");

		return Integer.parseInt(values[1]);
	}


	protected Mensagem getMessageCompleta(int i) throws IOException {

		String response = sendCommand("RETR " + i).trim();
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		String headerName = null;

		while ((response = readResponseLine()).length() != 0) {

			if (response.startsWith("\t")) {
				continue; //no process of multiline headers
			}

			int colonPosition = response.indexOf(":");

			if(response.contains(":") && response.split(":").length > 1){
				headerName = response.split(":")[0];

				String headerValue;

				if (headerName.length() > colonPosition) {
					headerValue = response.substring(colonPosition + 2);
				} 

				else {
					headerValue = "";
				}

				List<String> headerValues = headers.get(headerName);

				if (headerValues == null) {
					headerValues = new ArrayList<String>();
					headers.put(headerName, headerValues);
				}

				headerValues.add(headerValue);
			}
		} 

		StringBuilder bodyBuilder = new StringBuilder();

		while (!(response = readResponseLine()).equals(".")) {
			bodyBuilder.append(response + "\n");
		}

		return new Mensagem(headers, bodyBuilder.toString());
	}
	
	public List<String> getListaEmails() throws IOException{
		
		String response = sendCommand("LIST").trim();
		List<String> listaEmails = new ArrayList<String>();
		
		while (!(response = readResponseLine()).equals(".")) {
			listaEmails.add(response);
		}
		
		return listaEmails;
	}

	public List<Mensagem> getMessagesHeaders() throws IOException {

		int numOfMessages = getNumberOfNewMessages();
		List<Mensagem> messageList = new ArrayList<Mensagem>();

		for (int i = 1; i <= numOfMessages; i++) {
			messageList.add(getMessageCompleta(i));
		}

		return messageList;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
