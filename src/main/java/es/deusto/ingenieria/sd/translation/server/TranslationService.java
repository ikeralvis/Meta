package es.deusto.ingenieria.sd.translation.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class TranslationService extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;

	private static String DELIMITER = "#";
	private static String login = "LGIN";
    private static String comprobarEmail = "CMPE";


	
	public TranslationService(Socket socket) {
		try {
			this.tcpSocket = socket;
		    this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.err.println("# TranslationService - TCPConnection IO error:" + e.getMessage());
		}
	}

	public void run() {
		try {
			String data = this.in.readUTF();			
			System.out.println("   - TranslationService - Received data from '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");					
			
			if (data.startsWith(login)) {
				data = login(data.substring(login.length() + DELIMITER.length()));
				System.out.println("   - Comprobando el email '" + data + "' -> '" + data.toUpperCase() + "'");
			} else if (data.startsWith(comprobarEmail)) {
				data = comprobarEmail(data.substring(comprobarEmail.length() + DELIMITER.length()));
			} else {
				data = "ERR";
			}
		
					
			this.out.writeUTF(data);					
			System.out.println("   - TranslationService - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
		} catch (EOFException e) {
			System.err.println("   # TranslationService - TCPConnection EOF error" + e.getMessage());
		} catch (IOException e) {
			System.err.println("   # TranslationService - TCPConnection IO error:" + e.getMessage());
		} finally {
			try {
				tcpSocket.close();
			} catch (IOException e) {
				System.err.println("   # TranslationService - TCPConnection IO error:" + e.getMessage());
			}
		}
	}
	
	public String login(String msg) {
		String translation = null;
		
		if (msg != null && !msg.trim().isEmpty()) {
			try {
				StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);		
				String email = tokenizer.nextToken();
				String contraseña = tokenizer.nextToken();
				System.out.println("   - Comprobando " + email + " y " + contraseña );
			} catch (Exception e) {
				System.err.println("   # TranslationService - Translation API error:" + e.getMessage());
				translation = null;
			}
		}
		
		return (translation == null) ? "ERR" : "OK" + DELIMITER + translation;
	}

	public String comprobarEmail(String msg) {
		System.out.println("   - Comprobando email " + msg);
		return "string".equals(msg) ? "TRUE" : "FALSE";
	}
}