package org.danielegiulianini.puzzled.frontend.domain;


import org.danielegiulianini.puzzled.commons.JsonMessageEncoderDecoder;
import org.danielegiulianini.puzzled.commons.WebSocketClient;
import org.danielegiulianini.puzzled.commons.WebSocketListenerAdapter;
import org.danielegiulianini.puzzled.commons.constants.ServicesInfo;
import org.danielegiulianini.puzzled.commons.domain.events.PuzzleEvent;

/*
 * This class contains the networking logic (websockets) for retrieving the 
 * information needed to play.
 * */
public class PuzzleWebSocketClient {
	private static final String puzzleServiceEntryPointHost = ServicesInfo.API_GATEWAY_HOST;
	private static final int puzzleServiceEntryPointPort = ServicesInfo.API_GATEWAY_PORT;
	private static final String puzzleServiceEntryPointUri = "/api/events/room";

	private ClientGameLogicManager man;
	private WebSocketClient webSocketClient;
	private JsonMessageEncoderDecoder decoder;
	

	public PuzzleWebSocketClient(ClientGameLogicManager man) {
		this.decoder = new JsonMessageEncoderDecoder();
		this.man = man;
		this.webSocketClient = new WebSocketClient(
				puzzleServiceEntryPointPort, 
				puzzleServiceEntryPointHost,
				puzzleServiceEntryPointUri);
	}
	
	public void connect() {
		webSocketClient.connect(new WebSocketListenerAdapter() {
			@Override
			public void onMessage(String message) {
				decoder.decodePuzzleEvent(message)
				.ifPresent(decoded -> man.handleGameMessage(decoded));
			}
		});
	}	

	public void notifyEventToServer(PuzzleEvent event) {
		webSocketClient.writeTextMessage(event.toJsonString());
	}
}

