package org.danielegiulianini.puzzled.commons;

import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.danielegiulianini.puzzled.commons.PuzzleTile;
import org.danielegiulianini.puzzled.commons.domain.AbstractJsonable;
import org.danielegiulianini.puzzled.commons.domain.Identifiable;

import static org.danielegiulianini.puzzled.commons.LoggingUtils.log;

public class Puzzle extends AbstractJsonable implements Identifiable {
	private String id;
	private Set<PuzzleTile> tiles;
	private int rows, cols;
	
	private boolean isAssigned = false;
	
	
	//for jackson de-serialization
	public Puzzle(){
	}
	
	public Puzzle(Set<PuzzleTile> tiles, int cols, int rows) {
		this.rows = rows;
		this.cols = cols;
		this.tiles = tiles;
	}

	public void swap(String t1Id, String t2Id) {
		Optional<PuzzleTile> optT1 = tiles.stream().filter(t -> t.getId().equals(t1Id)).findFirst();
		Optional<PuzzleTile> optT2 = tiles.stream().filter(t -> t.getId().equals(t2Id)).findFirst();

		if (optT1.isPresent() && optT2.isPresent()) {
			PuzzleTile t1 = optT1.get();
			PuzzleTile t2 = optT2.get();
			int pos = t1.getCurrentPosition();
			t1.setCurrentPosition(t2.getCurrentPosition());
			t2.setCurrentPosition(pos);
		} else {
			log("swap malformed...");
		}
	}

	public Set<PuzzleTile> getTiles() {
		return tiles;
	}

	@JsonIgnore
	public boolean isComplete() {
		tiles.stream().sorted().map(x -> x.isInRightPlace()).forEach(System.out::print);
		return tiles.stream().sorted().allMatch(PuzzleTile::isInRightPlace);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}
	
	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean assigned) {
		isAssigned = assigned;
	}
	
}
