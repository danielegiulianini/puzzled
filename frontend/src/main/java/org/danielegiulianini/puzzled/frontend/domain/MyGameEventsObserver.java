package org.danielegiulianini.puzzled.frontend.domain;

import pcd.ass03.puzzle.mysol.commons.Position;
import pcd.ass03.puzzle.mysol.services.domain.PuzzleTile;

public interface MyGameEventsObserver {
	void onMyTileSelection(final PuzzleTile tile);
	void onMyPointerMoved(Position newPosition);
	void onMyExit();
}
