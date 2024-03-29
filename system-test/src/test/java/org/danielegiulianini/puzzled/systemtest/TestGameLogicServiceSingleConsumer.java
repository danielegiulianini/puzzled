package org.danielegiulianini.puzzled.systemtest;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.danielegiulianini.puzzled.commons.constants.ServicesInfo;
import org.danielegiulianini.puzzled.commons.domain.events.GameInfo;
import org.danielegiulianini.puzzled.backend.gamelogicservice.GameLogicService;
import org.danielegiulianini.puzzled.backend.managementservice.PuzzlesManagementService;
import org.danielegiulianini.puzzled.commons.WebSocketClient;

import static org.danielegiulianini.puzzled.systemtest.TestGameLogicServiceManyConsumers.WAIT_FOR_SERVER_UP;
import static org.danielegiulianini.puzzled.systemtest.TestGameLogicServiceManyConsumers.createWebSocketClientToPuzzleService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * Tests gameLogicService with a single-service-consumer-interaction scenario 
 * (request-response only messages pattern).
 */
@RunWith(VertxUnitRunner.class)
public class TestGameLogicServiceSingleConsumer {

	private Vertx vertx;
	private WebSocketClient webSocketClient;

	@Before
	public void before(TestContext context) {
		vertx = Vertx.vertx();
		Async async = context.async();

		//register the context exception handler
		vertx.exceptionHandler(context.exceptionHandler());

		//service-consumer-side
		webSocketClient = createWebSocketClientToPuzzleService();

		//service-side
		vertx
		.deployVerticle(new PuzzlesManagementService(ServicesInfo.PUZZLES_MANAGEMENT_SERVICE_PORT),
				id -> vertx
				.deployVerticle(new GameLogicService(ServicesInfo.GAME_LOGIC_SERVICE_PORT),
						id2 -> vertx.setTimer(WAIT_FOR_SERVER_UP, l-> async.complete())));
	}

	@After
	public void after(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void testServiceSendsGameInfoAfterWebsocketConnection(TestContext context) {
		Async async = context.async();
		webSocketClient.connect(new TestGameLogicServiceManyConsumers.PuzzleEventsListenerAdapter(decoded -> {
			if (decoded instanceof GameInfo) async.complete();
		}));
	}

	@Test
	public void testRoomSentByServiceIsNotNull(TestContext context) {
		Async async = context.async();
		webSocketClient.connect(new TestGameLogicServiceManyConsumers.PuzzleEventsListenerAdapter(decoded -> {
			if (decoded instanceof GameInfo && ((GameInfo) decoded).getRoomAssignedToClient() != null) async.complete();
		}));
	}
	
	@Test
	public void testPuzzleSentByServiceIsNotNull(TestContext context) {
		Async async = context.async();
		webSocketClient.connect(new TestGameLogicServiceManyConsumers.PuzzleEventsListenerAdapter(decoded -> {
			if (decoded instanceof GameInfo && ((GameInfo) decoded).getRoomAssignedToClient().getPuzzle() != null) async.complete();
		}));
	}
}