package org.danielegiulianini.puzzled.commons;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/*
 * This is an adapter class that allows the user to implement only the methods of WebSocketListener
 * he wants whenever a WebSocketListener instance is required (by providing a default implementation 
 * that does nothing).
 * */
public abstract class WebSocketListenerAdapter implements WebSocketListener {

	@Override
	public void onOpen(Session userSession) {
	}

	@Override
	public void onMessage(String message) {
	}
	
	@Override
	public void onClose(Session userSession, CloseReason reason) {
	}
	
	@Override
	public void onError( Exception ex ) {
	}

}
