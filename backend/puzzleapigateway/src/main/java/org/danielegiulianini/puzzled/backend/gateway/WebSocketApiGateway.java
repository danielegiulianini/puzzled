package org.danielegiulianini.puzzled.backend.gateway;

import java.util.Map;
import javax.websocket.Session;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.http.ServerWebSocket;
import org.danielegiulianini.puzzled.commons.ServiceVerticle;
import org.danielegiulianini.puzzled.commons.WebSocketClient;
import org.danielegiulianini.puzzled.commons.WebSocketListenerAdapter;


/*
 * This class provides a reusable API gateway for webSocket connections
 * (with circuit-breaker pattern).
 * */
public class WebSocketApiGateway extends ServiceVerticle {
	private Map<String, String> routingPoints;
	private CircuitBreaker breaker;

	public WebSocketApiGateway(int port, Map<String, String> routingPoints) {
		super(port);
		this.routingPoints = routingPoints;
	}

	@Override
	protected void setup() {
		setUpCircuitBreaker();

		getVertx()
		.createHttpServer()
		.webSocketHandler(this::webSocketHandler)
		.listen(this.localPort);
	}

	private void webSocketHandler(ServerWebSocket socketFromClient) {
		log("someone connected with ws: " + socketFromClient);

		/*This handler redirects in-coming connection to out-coming ones.
		for each "fromAPI" of the exchange points provided, it re-addresses them 
		to the respective "toAPI".*/

		this.routingPoints.entrySet().forEach(e -> {
			String fromPath = e.getKey();
			String toPath = e.getValue();

			//log("redirecting from" + socketFromClient.path()+ " to: " + fromPath);

			if (socketFromClient.path().equals(fromPath)) {
				log("websocket connection arrived, redirecting to specific service.");

				breaker.execute(future -> {
					WebSocketClient socketTowardsService = new WebSocketClient(toPath);

					socketTowardsService
					.connect(new WebSocketListenerAdapter(){
						@Override
						public void onOpen(Session userSession) {
							log("connection arrived, registering client-msgs handler.");

							socketFromClient.textMessageHandler(msg -> {
								log("received msg from ws"+ socketFromClient + ", redirecting client-message to specific service");
								socketTowardsService.writeTextMessage(msg);
							});
							future.complete();
						}
						@Override
						public void onMessage(String payload) {
							log("received from service and redirecting msg to client with ws: " + socketFromClient);
							socketFromClient.writeTextMessage(payload);
						}
						@Override
						public void onError(Exception t) {
							future.fail("websocket error");
						}
					});
				});
			} else {
				log("connection rejected. No mapping found for the URI provided.");
				socketFromClient.reject();
			}
		});
	}

	private void setUpCircuitBreaker() {
		breaker = CircuitBreaker.create(
				"ws-api-gateway-breaker", 
				vertx,
				new CircuitBreakerOptions().setMaxFailures(5).setTimeout(2000)
				);
	}

}
