package org.danielegiulianini.puzzled.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.vertx.core.http.ServerWebSocket;

/* Represents a layer built on top of Vertx' ServerWebSockets that provides 
 * some convenient utilities for handling a group (room) of websocket clients
 * and related communication patterns between them (broadcast, point-to-point, 
 * multicast). 
 * */
public class WebSocketsRoom{
	private Map<String, ServerWebSocket> usersToWebSockets;
	
	public WebSocketsRoom() {
		this.usersToWebSockets = new HashMap<>();
	}

	public void broadcast(String message) {
		this.usersToWebSockets.entrySet()
		.forEach(ws -> {System.out.println("sending to "+ws.getKey());ws.getValue().writeTextMessage(message);});
	}

	public void broadcastExcept(String userId, String message) {
		this.usersToWebSockets.entrySet().stream()
		.filter(x -> !x.getKey().equals(userId))
		.map(x -> x.getValue())
		.forEach(ws -> ws.writeTextMessage(message));
	}

	public void sendTo(String userId, String message) {
		this.usersToWebSockets.get(userId).writeTextMessage(message);
	}

	@JsonIgnore
	public Set<String> getUsers() {
		return usersToWebSockets.keySet();
	}

	public void addUser(String userId, ServerWebSocket userSocket) {
		this.usersToWebSockets.put(userId, userSocket);
	}

	public void disconnectUser(String userId) {
		this.usersToWebSockets.remove(userId);
	}

}
