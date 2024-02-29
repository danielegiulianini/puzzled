package org.danielegiulianini.puzzled.commons.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.danielegiulianini.puzzled.commons.Position;


public class CursorMoved extends PuzzleEvent {
	private Position position;
	
	//for de-serializing with Jackson
	@JsonCreator public CursorMoved(@JsonProperty("senderId") String senderId, @JsonProperty("position")Position position) {
		super(senderId);
		this.position = position;
	}
	
	public Position getPosition() {
		return position;
	}
}
