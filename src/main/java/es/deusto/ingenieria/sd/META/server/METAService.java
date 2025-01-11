package es.deusto.ingenieria.sd.META.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class METAService extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;

	private static String DELIMITER = "#";
	private static String login = "LGIN";
    private static String comprobarEmail = "CMPE";

	private Map<String, String> usuarios = new HashMap<String, String>();
	private Logger logger = Logger.getLogger("METAService");

	
	public METAService(Socket socket) {
		try {
			this.cargarUsuarios();
			this.tcpSocket = socket;
		    this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
		} catch (IOException e) {
			logger.info("# Error E/S" + e.getMessage());
		}
	}

	public void run() {
		try {
			String data = this.in.readUTF();
			logger.info ("Datos recibidos de:'" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");				
			
			if (data.startsWith(login)) {
				data = login(data);
			} else if (data.startsWith(comprobarEmail)) {
				data = comprobarEmail(data.substring(comprobarEmail.length() + DELIMITER.length()));
			} else {
				data = "ERR";
			}
					
			this.out.writeUTF(data);
			logger.info("Datos enviados a :'" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
		} catch (EOFException e) {
			logger.info("EOF error" + e.getMessage());
		} catch (IOException e) {
			logger.info("IO error:" + e.getMessage());
		} finally {
			try {
				tcpSocket.close();
				logger.info("Conexion cerrada");
			} catch (IOException e) {
				logger.info("IO error:" + e.getMessage());
			}
		}
	}
	
	public String login(String msg) {
		
		if (msg != null && !msg.trim().isEmpty()) {
			try {
				StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);
				tokenizer.nextToken();	
				String email = tokenizer.nextToken();
				String contraseña = tokenizer.nextToken();
				logger.info("Comprobando " + email + " y " + contraseña);
				if(this.usuarios.containsKey(email) && this.usuarios.get(email).equals(contraseña)) {
					return "TRUE";
				}else {
					return "FALSE";
				}
			} catch (Exception e) {
				return "ERR";
			}
		}
		return "ERR";
	}

	public String comprobarEmail(String msg) {
		logger.info("Comprobando email " + msg);
		if(this.usuarios.containsKey(msg)) {
			return "TRUE";
		}else {
			return "FALSE";
		}
	}

	public void cargarUsuarios() {
		//Email , contraseña
		this.usuarios.put("string", "string");
		this.usuarios.put("usain.bolt@athletics.com", "1234");
		this.usuarios.put("michael.phelps@swimming.com", "1234");
		this.usuarios.put("serena.williams@tennis.com", "1234");
		this.usuarios.put("lionel.messi@soccer.com", "1234");
		this.usuarios.put("lebron.james@basketball.com", "1234");
		this.usuarios.put("cristiano.ronald@soccer.com", "1234");
		this.usuarios.put("mikel@mikel.com", "1234");
		this.usuarios.put("iker@iker.com", "1234");
		logger.info("Usuarios cargados");

	}
}