package es.deusto.ingenieria.sd.translation.server;

import java.io.IOException;
import java.net.ServerSocket;

public class TranslationServer {
	
	private static int numClients = 0;
	
	public static void main(String args[]) {
		int serverPort = 8083;
		
		try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);) {
			System.out.println(" - Esperando conexiones '" + tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getLocalPort() + "' ...");
			
			while (true) {
				new TranslationService(tcpServerSocket.accept());
				System.out.println(" - Nueva conexion" + ++numClients);
			}
		} catch (IOException e) {
			System.err.println("# TranslationServer: IO error:" + e.getMessage());
		}
	}
}