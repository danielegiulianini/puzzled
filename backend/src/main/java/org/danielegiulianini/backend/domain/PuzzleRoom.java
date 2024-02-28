package org.danielegiulianini.backend.domain;

import org.danielegiulianini.backend.domain.Puzzle;
import org.danielegiulianini.backend.domain.Jsonable;

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
