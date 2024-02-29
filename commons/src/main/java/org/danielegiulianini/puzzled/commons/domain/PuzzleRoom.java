package org.danielegiulianini.puzzled.commons.domain;

import org.danielegiulianini.puzzled.commons.Puzzle;

import io.vertx.core.json.Json;
import org.danielegiulianini.puzzled.commons.WebSocketsRoom;


public class PuzzleRoom extends WebSocketsRoom implements Jsonable {
	private Puzzle puzzle;
	
	//for jackson de-serialization
	public PuzzleRoom(){
	}
	
	public PuzzleRoom(Puzzle puzzle){
		this.puzzle = puzzle;
	}

	public Puzzle getPuzzle() {
		return puzzle;
	}
	
    public String toJsonString() {
    	return Json.encodePrettily(this);
    }
    
}
