package es.deusto.ingenieria.sd.META.server;

import java.io.IOException;
import java.net.ServerSocket;

public class METAServer {
	
	public static void main(String args[]) {
		int serverPort = 8083;
		
		try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);) {
			System.out.println(" - Esperando conexiones '" + tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getLocalPort() + "' ...");
			
			while (true) {
				new METAService(tcpServerSocket.accept());
				System.out.println(" - Nueva conexion");
			}
		} catch (IOException e) {
			System.err.println("#Error E/S" + e.getMessage());
		}
	}
}