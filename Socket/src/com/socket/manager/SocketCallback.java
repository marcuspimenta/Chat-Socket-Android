package com.socket.manager;

/**
 * 
 * @author Marcus Pimenta
 * @email mvinicius.pimenta@gmail.com
 * @date 16:28:55 24/01/2013
 */
public interface SocketCallback {

	void onSocketReceiverMsg(byte[] msg);
	
}