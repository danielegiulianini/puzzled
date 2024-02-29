package org.danielegiulianini.puzzled.commons.domain.events;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.danielegiulianini.puzzled.commons.domain.AbstractJsonable;


/*
 * This class represent the root of the hierarchy of the kind of events 
 * that happen during a game, while players attempt to resolve the puzzle.
 * It eases decoding of these events by proving a type property for each 
 * of them. 
 * Jackson's json annotations purpose is to provide automatic
 * encoding and decoding of the right kind of message when receiver is 
 * expecting that specific type.
 * */
@JsonSubTypes({
	@JsonSubTypes.Type(value = TilesSwap.class, name="tilesSwap"),	//value of type property added in line 29
	@JsonSubTypes.Type(value = CursorMoved.class, name="cursorMoved"),
	@JsonSubTypes.Type(value = PuzzleCompleted.class, name="puzzleCompleted"),
	@JsonSubTypes.Type(value = UserJoined.class, name="clientJoined"),
	@JsonSubTypes.Type(value = UserExited.class, name="clientExited"),
	@JsonSubTypes.Type(value = GameInfo.class, name="gameInfo") 
})
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME, 
		include = JsonTypeInfo.As.PROPERTY, 
		property = "eventType"	//type property name (added)
		)
public abstract class PuzzleEvent extends AbstractJsonable {
	private String senderId;

	public PuzzleEvent(String senderId){
		this.senderId = senderId;
	}

	public String getSenderId() {
		return senderId;
	}


	//for testing
	public static void main(String[] args) {
		System.out.println(new TilesSwap("user-0", "3"," 2", "1").toJsonString());
		System.out.println(new PuzzleCompleted("user-0").toJsonString());
	}
}
