package org.danielegiulianini.puzzled.systemtest;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.danielegiulianini.puzzled.commons.JsonMessageEncoderDecoder;
import org.danielegiulianini.puzzled.commons.constants.ServicesInfo;
import org.danielegiulianini.puzzled.backend.gamelogicservice.GameLogicService;
import org.danielegiulianini.puzzled.backend.managementservice.PuzzlesManagementService;
import org.danielegiulianini.puzzled.commons.Position;
import org.danielegiulianini.puzzled.commons.WebSocketClient;
import org.danielegiulianini.puzzled.commons.WebSocketListenerAdapter;
import org.danielegiulianini.puzzled.commons.domain.events.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Consumer;

import static org.danielegiulianini.puzzled.commons.LoggingUtils.log;


/* Tests gameLogicService with many-service-consumers-interaction 
 * scenario (request-response only messages pattern).
 * */

@RunWith(VertxUnitRunner.class)
public class TestGameLogicServiceManyConsumers {

	private Vertx vertx;
	private WebSocketClient oldWebSocketClient, newWebSocketClient;		
	private String oldWebSocketClientId;

	public static int WAIT_FOR_SERVER_UP = 1000;	//in ms

	//before each test
	@Before
	public void before(TestContext context) {
		log("inside before.");

		vertx = Vertx.vertx();
		Async async = context.async();

		//register the context exception handler
		vertx.exceptionHandler(context.exceptionHandler());

		log("setting up websockets.");

		//service-consumer-side
		oldWebSocketClient = createWebSocketClientToPuzzleService();
		newWebSocketClient = createWebSocketClientToPuzzleService();

		//service-side
		vertx
		.deployVerticle(new PuzzlesManagementService(ServicesInfo.PUZZLES_MANAGEMENT_SERVICE_PORT),
				id -> {
					log("puzzleManagementService successfully deployed.");
					vertx
					.deployVerticle(new GameLogicService(ServicesInfo.GAME_LOGIC_SERVICE_PORT),
							id2 -> {
								log("GameLogicService successfully deployed.");

								vertx.setTimer(WAIT_FOR_SERVER_UP, l-> oldWebSocketClient.connect(new PuzzleEventsListenerAdapter(decoded -> {
									if (decoded instanceof GameInfo) {
										oldWebSocketClientId = ((GameInfo) decoded).getIdAssignedToNewClient();//context.asyncAssertSuccess();

										log("context set up.");
										async.complete();
									}
								})));
							});
				});

	}

	public static WebSocketClient createWebSocketClientToPuzzleService() {
		return new WebSocketClient(
				ServicesInfo.GAME_LOGIC_SERVICE_PORT,
				ServicesInfo.GAME_LOGIC_SERVICE_HOST,
				"/api/events/room");
	}

	@After
	public void after(TestContext context) {
		log("context shutting down.");
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void testServiceBroadcastsUserExitedAfterAUserExited(TestContext context) {

		Async async = context.async();
		newWebSocketClient.connect(new PuzzleEventsListenerAdapter(decoded -> {
			if (decoded instanceof UserExited) async.complete();
		}));
		oldWebSocketClient.writeTextMessage(
				new UserExited(oldWebSocketClientId)
				.toJsonString());		
	}

	@Test
	public void testServiceBroadcastsUserJoinedAfterNewWebsocketConnection(TestContext context) {
		Async async = context.async();

		newWebSocketClient.connect(new PuzzleEventsListenerAdapter(decoded -> {
			if (decoded instanceof UserJoined) async.complete();
		}));
		oldWebSocketClient.writeTextMessage(
				new UserJoined(oldWebSocketClientId)
				.toJsonString());	
	}

	@Test
	public void testServiceBroadcastsCursorMoved(TestContext context) {
		Async async = context.async();

		Position randomPosition = new Position(1.1, 20.0);

		newWebSocketClient.connect(new PuzzleEventsListenerAdapter(decoded -> {
			if (decoded instanceof CursorMoved) async.complete();// context.asyncAssertSuccess();
		}));
		oldWebSocketClient.writeTextMessage(
				new CursorMoved(oldWebSocketClientId, 
						randomPosition).toJsonString());
	}

	
	public static class PuzzleEventsListenerAdapter extends WebSocketListenerAdapter {
		private Consumer<PuzzleEvent> decodedConsumer;
		private JsonMessageEncoderDecoder decoder;

		public PuzzleEventsListenerAdapter(Consumer<PuzzleEvent> decodedConsumer) {
			this.decodedConsumer = decodedConsumer;
			this.decoder = new JsonMessageEncoderDecoder(); 
		}

		@Override
		public void onMessage(String message) {
			decoder
			.decodePuzzleEvent(message)
			.ifPresent(decodedConsumer);
		}
	}

}