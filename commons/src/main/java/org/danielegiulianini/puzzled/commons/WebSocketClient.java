package org.danielegiulianini.puzzled.commons;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/*
 * This class is a facade to provide javax.websocket APIs in a
 * more user-friendly fashion. 
 * */
@ClientEndpoint
public class WebSocketClient {
	private String completeUri;

	private Session userSession = null;
	private WebSocketContainer container;
	private WebSocketListener listener;

	public WebSocketClient(String completeUri) {
		this.completeUri = completeUri;
		this.container =  ContainerProvider.getWebSocketContainer();
		this.container.setDefaultMaxTextMessageBufferSize(100000000);
	}
	
	public WebSocketClient(int port, String host, String requestUri) {
		this(buildCompleteWebSocketUriStringFrom(host, port, requestUri));
	}
	
	
	public void writeTextMessage(String payload) {
		this.userSession.getAsyncRemote().sendText(payload);
	}
	
	
	public void closeConnection() {
		try {
			this.userSession.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startConnection(WebSocketListener listener) {
		try {
			this.listener = listener;
			container.connectToServer(this, new URI(completeUri));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void connect(WebSocketListener listener) {
		startConnection(listener);
	}

	@OnMessage
	public void onMessage(String message) {
		if (this.listener != null) {
			this.listener.onMessage(message);
		}
	}

	@OnOpen
	public void onOpen(Session userSession) {
		this.userSession = userSession;
		listener.onOpen(userSession);
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		this.userSession = null;
		listener.onClose(userSession, reason);
	}
	
	

	
	//this is a utility class (could go in separated file)
	public static String buildCompleteWebSocketUriStringFrom(String host, int port, String requestUri) {
		return "ws://" +
				host + ":" +
				port + 
				requestUri;
	}

}
