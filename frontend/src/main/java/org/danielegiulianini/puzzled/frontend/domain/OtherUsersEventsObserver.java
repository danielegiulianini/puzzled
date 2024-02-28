package org.danielegiulianini.puzzled.frontend.domain;

import pcd.ass03.puzzle.mysol.commons.Position;

public interface OtherUsersEventsObserver {
	void onPuzzleCompleted();
	void onTilesSwap(String t1Id, String t2Id);
	void onCursorMoved(String userId, Position cursorPosition);
	void onOtherUserExited(String userId);
}
