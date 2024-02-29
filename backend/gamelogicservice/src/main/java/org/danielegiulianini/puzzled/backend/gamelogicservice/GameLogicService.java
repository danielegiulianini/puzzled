package org.danielegiulianini.puzzled.backend.gamelogicservice;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.file.FileSystemOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;

import org.danielegiulianini.puzzled.commons.constants.ServicesInfo;
import org.danielegiulianini.puzzled.commons.Puzzle;
import org.danielegiulianini.puzzled.commons.domain.PuzzleRoom;
import org.danielegiulianini.puzzled.commons.RestFetcherService;
import org.danielegiulianini.puzzled.commons.JsonMessageEncoderDecoder;
import org.danielegiulianini.puzzled.commons.domain.events.*;

/*
 * This is the service that:
 * 1. groups service-consumers into rooms, each of which is assigned a puzzle
 * 2. handles puzzles events from service-consumers and
 * 3. keeps the state for the puzzle associated to every room.
 * */
public class GameLogicService extends RestFetcherService {
	private Set<PuzzleRoom> rooms;
	private Map<String, PuzzleRoom> roomsByUserId;
	private JsonMessageEncoderDecoder encoder;

	public GameLogicService(int localPort) {
		super(localPort);
		this.roomsByUserId = new HashMap<>();
		this.rooms = new HashSet<>();
		this.encoder = new JsonMessageEncoderDecoder();
	}

	protected void setup() {
		super.setup();	//needed

		HttpServerOptions options = new HttpServerOptions();
		options.setMaxWebSocketFrameSize(100000);

		getVertx()
		.createHttpServer(options)
		.webSocketHandler(this::webSocketHandler)
		.listen(localPort);

		fetchPuzzles(

				puzzles -> {
					rooms.addAll(puzzles.stream().map(PuzzleRoom::new).collect(Collectors.toList()));
					log("puzzles fetched!, they are"+ puzzles);
					log("rooms are" + rooms);
				}

		);

		log("ready at port: " + localPort);
	}


	private void webSocketHandler(ServerWebSocket ws) {
		log("client connection arrived.");

		if (ws.path().startsWith("/api/events/room")) {

			getAnAvailableRoom()
			.onSuccess(room -> assignRoomToNewClient(room, ws))
			.onFailure(e -> ws.reject());					
		}
	}

	private void fetchPuzzles(Handler<List<Puzzle>> fun) {
		log("fetching puzzles...");

		fetchFromRestService(
				ServicesInfo.PUZZLES_MANAGEMENT_SERVICE_PORT, 
				ServicesInfo.PUZZLES_MANAGEMENT_SERVICE_HOST,
				"/api/puzzles",
				result -> {
					Optional<List<Puzzle>> puzzles = encoder.decodePuzzles(result.bodyAsString());
					if (puzzles.isPresent()) {
						log("puzzle is present in response by service");

						fun.handle(puzzles.get());
					} else {
						log("puzzles management service response malformed: "+puzzles);
					}
				});
	}

	private Future<PuzzleRoom> getAnAvailableRoom(){
		Promise<PuzzleRoom> promise = Promise.promise();
		Optional<PuzzleRoom> room = rooms.stream().filter(x -> !x.getPuzzle().isComplete()).findFirst();
		if (room.isPresent()) {
			promise.complete(room.get());
			log("a room is present: assigning to user");
		} else {
			log("a room is not present: fetching from service");

			fetchPuzzles(puzzles -> {

				if (!puzzles.isEmpty()) {
					log("a room is returned by service");

					Collections.shuffle(puzzles);
					Puzzle newPuzzle = puzzles.get(0);	//get the first puzzle available
					promise.complete(new PuzzleRoom(newPuzzle));
				} else {
					log("no rooms returned by service");

					promise.fail("no rooms.");
				}
			});
		}
		return promise.future();
	}

	private void assignRoomToNewClient(PuzzleRoom room, ServerWebSocket ws) {
		log("assigning room to client");
		String newClientId = ws.textHandlerID();	//could have generated ID randomly
		rooms.add(room);
		room.addUser(newClientId, ws);
		roomsByUserId.put(newClientId, room);

		ws.closeHandler(msg -> handleClientDisconnection(ws));
		ws.textMessageHandler(msg -> handleWebSocketTextMessageByClient(msg, ws));

		ws.writeTextMessage(new GameInfo("service", newClientId, room).toJsonString());
	}


	private void handleWebSocketTextMessageByClient(String message, ServerWebSocket ws) {
		encoder.decodePuzzleEvent(message).ifPresent(decoded -> {
			
			String senderUserId = decoded.getSenderId();
			PuzzleRoom senderRoom = roomsByUserId.get(senderUserId);

			if (decoded instanceof UserJoined) {
				log("received clientjoined event at service.");
				senderRoom.broadcastExcept(senderUserId, message);
			} else if (decoded instanceof TilesSwap) {
				log("received tilesswap event at service.");
				senderRoom.getPuzzle().swap(
						((TilesSwap) decoded).getTile1Id(),
						((TilesSwap) decoded).getTile2Id());
				senderRoom.broadcast(message);
				if (senderRoom.getPuzzle().isComplete()){
					senderRoom.broadcast(encoder.jsonEncode(new PuzzleCompleted(decoded.getSenderId())));
				}
			} else if (decoded instanceof CursorMoved){
				log("received CursorMoved event at service.");
				senderRoom.broadcastExcept(senderUserId, message);
			} else if (decoded instanceof UserExited){
				log("received ClientExited event at service.");
				senderRoom.disconnectUser(senderUserId);
				senderRoom.broadcast(message);
			} else {
				log("errors on formatting json."); 		//send error message to client
			}
		});
	}

	private void handleClientDisconnection(ServerWebSocket ws) {
		log("a user disconnected.");
		String senderUserId = ws.textHandlerID();
		PuzzleRoom senderRoom = roomsByUserId.get(senderUserId);
		senderRoom.disconnectUser(senderUserId);
		senderRoom.broadcast(new UserExited(senderUserId).toJsonString());
	}

}
