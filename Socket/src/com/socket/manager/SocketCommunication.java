package com.socket.manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.IOException;

import android.os.Handler;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 20:35:24 15/01/2013
 */
public class SocketCommunication extends Thread {
	
	private int whatMsgSocket;
	private int whatMsgNotice;

	private Socket socket;
	private Handler handler;

	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	
	public SocketCommunication(Socket socket, Handler handler, int whatMsgSocket, int whatMsgNotice){
		this.socket = socket;
		this.handler = handler;
		this.whatMsgSocket = whatMsgSocket;
		this.whatMsgNotice = whatMsgNotice;
	}
	
	@Override
	public void run() {
		 super.run();
		
		 try {
			 dataInputStream = new DataInputStream(socket.getInputStream());
			 dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
			 while (true) {
				 if(dataInputStream.available() > 0){
					 byte[] msg = new byte[dataInputStream.available()];
					 dataInputStream.read(msg, 0, dataInputStream.available());
					 
					 sendHandler(whatMsgSocket, "Ele" + ": " + new String(msg));
				 }
			 }
		 } catch (IOException e) {
			 e.printStackTrace(); 
			 
			 dataInputStream = null;
			 dataOutputStream = null;
			 
			 sendHandler(whatMsgNotice, "Conexao perdida");
		 }
	}
	
	public void sendHandler(int what, Object object){
		handler.obtainMessage(what, object).sendToTarget();
	}
	
	public void sendMsg(String msg){
		try {
			if(dataOutputStream != null){
				dataOutputStream.write(msg.getBytes());
				dataOutputStream.flush();
			}else{
				sendHandler(whatMsgNotice, "Sem conexao");
			}
			
		} catch (IOException e) {
			e.printStackTrace(); 
			 
			sendHandler(whatMsgNotice, "Falha no envio da mensagem");
		}
	}
	
	 public void stopComunication(){ 
		try {
			socket.close();
			
			if(dataInputStream != null && dataOutputStream != null){
				dataInputStream.close();
				dataOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }

 }