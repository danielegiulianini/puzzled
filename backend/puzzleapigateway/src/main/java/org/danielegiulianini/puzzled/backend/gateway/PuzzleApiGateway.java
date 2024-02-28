package org.danielegiulianini.puzzled.backend.gateway;

import java.util.HashMap;
import java.util.Map;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import org.danielegiulianini.backend.constants.ServicesInfo;
import org.danielegiulianini.puzzled.commons.WebSocketClient;

/*
 * This is the API gateway for puzzle service and its entry point from outside.
 * It mediates between client-consumers connections and the service contextually 
 * implementing circuit-breaker pattern for handling partial failures.
 * */
public class PuzzleApiGateway {

	public PuzzleApiGateway(int localPort, Handler<AsyncResult<String>> handler) {
		Map<String, String> routingPoints = new HashMap<>();

		routingPoints.put(
				"/api/events/room",
				WebSocketClient.buildCompleteWebSocketUriStringFrom(
						ServicesInfo.GAME_LOGIC_SERVICE_HOST,
						ServicesInfo.GAME_LOGIC_SERVICE_PORT, 
						"/api/events/room"));

		Vertx.vertx().deployVerticle(new WebSocketApiGateway(localPort, routingPoints), id -> handler.handle(id));
	}
	
	public PuzzleApiGateway(int localPort) {
		Map<String, String> routingPoints = new HashMap<>();

		routingPoints.put(
				"/api/events/room",
				WebSocketClient.buildCompleteWebSocketUriStringFrom(
						ServicesInfo.GAME_LOGIC_SERVICE_HOST, 
						ServicesInfo.GAME_LOGIC_SERVICE_PORT, 
						"/api/events/room"));

		Vertx.vertx().deployVerticle(new WebSocketApiGateway(localPort, routingPoints));
	}


}
