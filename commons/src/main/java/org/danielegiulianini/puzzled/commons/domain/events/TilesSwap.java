package org.danielegiulianini.puzzled.commons.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TilesSwap extends PuzzleEvent {	
	private String puzzleId;
	private String tile1Id;
	private String tile2Id;

	//for de-serializing with Jackson (or use empty constructor):
	@JsonCreator
	public TilesSwap(@JsonProperty("senderId") String senderId, @JsonProperty("puzzleId") String puzzleId, @JsonProperty("tile1Id") String tile1Id, @JsonProperty("tile2Id") String tile2Id) {
		super(senderId);
		this.puzzleId = puzzleId;
		this.tile1Id = tile1Id;
		this.tile2Id = tile2Id;
	}
	
	public String getPuzzleId() {
		return puzzleId;
	}

	public String getTile1Id() {
		return tile1Id;
	}

	public String getTile2Id() {
		return tile2Id;
	}
	
	
	
	
	//for testing
	public static void main(String[] args) {
		TilesSwap jo = new TilesSwap("a", "3", "4", "5");
		System.out.println("single element encoding is: " + jo.toJsonString());
	}
}
