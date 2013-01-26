package com.socket.manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 19:18:05 15/01/2013
 */
public class SocketServer {
	
	private final int TIME_OUT = 15000;
	
	private Socket socket;
	
	public Socket startServer(int port){
		try {
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(TIME_OUT);
			
			socket = server.accept();
			
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return socket;
	}

}