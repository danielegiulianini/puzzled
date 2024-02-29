package org.danielegiulianini.puzzled.commons.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserJoined extends PuzzleEvent {
	
	@JsonCreator
	public UserJoined(@JsonProperty("senderId") String senderId) {
		super(senderId);
	}
	
}
