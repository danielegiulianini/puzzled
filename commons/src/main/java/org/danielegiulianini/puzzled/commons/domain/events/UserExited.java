package org.danielegiulianini.puzzled.commons.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserExited extends PuzzleEvent {

	@JsonCreator
	public UserExited(@JsonProperty("senderId") String senderId) {
		super(senderId);
	}
	
}
