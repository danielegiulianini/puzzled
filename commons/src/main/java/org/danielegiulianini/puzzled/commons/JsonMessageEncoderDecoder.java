package org.danielegiulianini.puzzled.commons;

import org.danielegiulianini.puzzled.commons.domain.Jsonable;
import org.danielegiulianini.puzzled.commons.Puzzle;
import org.danielegiulianini.puzzled.commons.domain.events.PuzzleEvent;
import org.danielegiulianini.puzzled.commons.JsonUtils;

import java.util.List;
import java.util.Optional;


public class JsonMessageEncoderDecoder {

	public Optional<List<Puzzle>> decodePuzzles(String puzzles){
		return JsonUtils.decodeJson(puzzles, List.class, Puzzle.class);
	}

	public Optional<PuzzleEvent> decodePuzzleEvent(String message) {
		return JsonUtils.decodeJson(message, PuzzleEvent.class);
	}
	
	public String jsonEncode(Jsonable jsonable) {
		return jsonable.toJsonString();
	}

}
