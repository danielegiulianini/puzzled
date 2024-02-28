package org.danielegiulianini.puzzled.commons;

import javax.websocket.CloseReason;
import javax.websocket.Session;

public interface WebSocketListener {
	void onMessage(String message);
	void onOpen(Session userSession);
	void onClose(Session userSession, CloseReason reason);
    void onError( Exception ex );
}
