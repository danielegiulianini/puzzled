package org.danielegiulianini.puzzled.backend.managementservice;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.danielegiulianini.backend.domain.Puzzle;

public class PuzzlesStorage {
	private Set<Puzzle> puzzles;
	
	public PuzzlesStorage() {
		this.puzzles = new HashSet<>();
	}

	public Set<Puzzle> getAllPuzzles(){
		return Collections.unmodifiableSet(this.puzzles);
	}

	public Optional<Puzzle> getPuzzleById(String id){
		return this.puzzles.stream().filter(puzzle -> puzzle.getId().equals(id)).findFirst();
	}

	private int puzzlesAutoIncrement = 0;
	public int insertPuzzle(Puzzle p) {
		p.setId(""+puzzlesAutoIncrement);
		this.puzzles.add(p);
		return puzzlesAutoIncrement++;
	}
	
	public void removePuzzle(String id){
		this.puzzles.removeIf(puzzle -> puzzle.getId().equals(id));
	}
}
