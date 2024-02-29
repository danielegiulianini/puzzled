package org.danielegiulianini.puzzled.frontend.domain;


import org.danielegiulianini.puzzled.commons.Position;
import org.danielegiulianini.puzzled.commons.PuzzleTile;

public interface MyGameEventsObserver {
	void onMyTileSelection(final PuzzleTile tile);
	void onMyPointerMoved(Position newPosition);
	void onMyExit();
}
