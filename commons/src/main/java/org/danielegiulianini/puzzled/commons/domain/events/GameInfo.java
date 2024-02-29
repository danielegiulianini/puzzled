package org.danielegiulianini.puzzled.commons.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.danielegiulianini.puzzled.commons.domain.PuzzleRoom;


public class GameInfo extends PuzzleEvent {
	private String idAssignedToNewClient;
	private PuzzleRoom roomAssignedToClient;

	@JsonCreator
	public GameInfo(@JsonProperty("senderId") String senderId, 
			@JsonProperty("idAssignedToNewClient") String idAssignedToNewClient, 
			@JsonProperty("roomAssignedToClient") PuzzleRoom roomAssignedToClient) {
		super(senderId);
		this.idAssignedToNewClient = idAssignedToNewClient;
		this.roomAssignedToClient = roomAssignedToClient;
	}

	public String getIdAssignedToNewClient() {
		return idAssignedToNewClient;
	}

	public PuzzleRoom getRoomAssignedToClient() {
		return roomAssignedToClient;
	}

}
