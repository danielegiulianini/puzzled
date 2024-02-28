package org.danielegiulianini.backend.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PuzzleCompleted extends PuzzleEvent {
	
	@JsonCreator public PuzzleCompleted(@JsonProperty("senderId")String senderId){
		super(senderId);
	}
}
