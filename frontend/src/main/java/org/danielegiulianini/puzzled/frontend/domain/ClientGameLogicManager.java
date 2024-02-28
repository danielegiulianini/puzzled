package org.danielegiulianini.puzzled.frontend.domain;

import pcd.ass03.puzzle.mysol.commons.Position;
import pcd.ass03.puzzle.mysol.services.domain.Puzzle;
import pcd.ass03.puzzle.mysol.services.domain.PuzzleTile;
import pcd.ass03.puzzle.mysol.services.domain.events.UserExited;
import pcd.ass03.puzzle.mysol.services.domain.events.UserJoined;
import pcd.ass03.puzzle.mysol.services.domain.events.CursorMoved;
import pcd.ass03.puzzle.mysol.services.domain.events.GameInfo;
import pcd.ass03.puzzle.mysol.services.domain.events.PuzzleCompleted;
import pcd.ass03.puzzle.mysol.services.domain.events.PuzzleEvent;
import pcd.ass03.puzzle.mysol.services.domain.events.TilesSwap;

import static pcd.ass03.puzzle.mysol.commons.LoggingUtils.log;


/*
 * This class contains the logic for a service-consumer to play.
 * */
public class ClientGameLogicManager implements MyGameEventsObserver {
	private PuzzleWebSocketClient client;

	private String myUserId;
	private String puzzleId;
	private PuzzleBoard puzzleBoard;

	private boolean selectionActive = false;
	private PuzzleTile selectedTile;

	public ClientGameLogicManager() {
		client = new PuzzleWebSocketClient(this);
		client.connect();
	}

	public void handleGameMessage(PuzzleEvent decoded) {
		if (decoded instanceof UserJoined) {
			log("received ClientJoined event at service consumer side.");
		} else if (decoded instanceof GameInfo) {
			log("received GameInfo event at service consumer side.");

			GameInfo credentials = (GameInfo)decoded;
			Puzzle puzzle = credentials.getRoomAssignedToClient().getPuzzle();
			puzzleId = puzzle.getId();
			myUserId =  credentials.getIdAssignedToNewClient();

			puzzleBoard = new PuzzleBoard(this, puzzle);
			puzzleBoard.display();

			client.notifyEventToServer(new UserJoined(myUserId));
		} else if (decoded instanceof TilesSwap) {
			log("received tilesswap event at service consumer side.");
			TilesSwap tilesSwapEvent = (TilesSwap)decoded;
			puzzleBoard.onTilesSwap(tilesSwapEvent.getTile1Id(), tilesSwapEvent.getTile2Id());
		} else if (decoded instanceof CursorMoved){
			log("received CursorMoved event at service consumer side.");
			CursorMoved cursorMovedEvent = (CursorMoved)decoded;
			puzzleBoard.onCursorMoved(cursorMovedEvent.getSenderId(), cursorMovedEvent.getPosition());
		} else if (decoded instanceof PuzzleCompleted){
			log("received PuzzleEvent event at service consumer side.");
			puzzleBoard.onPuzzleCompleted();
		} else if (decoded instanceof UserExited){
			log("received ClientExited event at service consumer side.");
			puzzleBoard.onOtherUserExited(decoded.getSenderId());
		} else {
			log("errors on formatting json.");
		}
	}

	public void onMyTileSelection(final PuzzleTile tile) {
		if(selectionActive) {
			selectionActive = false;
			onMyTileSwap(tile);
		} else {
			selectionActive = true;
			selectedTile = tile;
		}
	}

	private void onMyTileSwap(final PuzzleTile tile) {
		client.notifyEventToServer(
				new TilesSwap(
						myUserId,
						puzzleId,
						selectedTile.getId(),
						tile.getId()));
	}

	public void onMyPointerMoved(Position pos) {
		client.notifyEventToServer(
				new CursorMoved(
						myUserId,
						pos));
	}

	@Override
	public void onMyExit() {
		client.notifyEventToServer(new UserExited(myUserId));		
	}
}
