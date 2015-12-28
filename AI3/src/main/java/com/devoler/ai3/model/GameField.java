package com.devoler.ai3.model;

import java.util.ArrayList;
import java.util.List;

import com.devoler.ai3.model.Move.Direction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class GameField {
	private static final List<Move> NO_MOVES = new ArrayList<>();
	
	private final int width;
	private final int height;
	private final Player player1;
	private final Player player2;
	private final List<Position> crystals;
	private final Position base1;
	private final Position base2;
	private final boolean player1Turn;
	
	GameField(final int width, final int height, final Player player1, final Player player2, final List<Position> crystals, final Position base1, final Position base2, final boolean player1Turn) {
		this.width = width;
		this.height = height;
		this.player1 = player1;
		this.player2 = player2;
		this.crystals = crystals;
		this.base1 = base1;
		this.base2 = base2;
		this.player1Turn = player1Turn;
	}
	
	public Position getBase1() {
		return base1;
	}
	
	public Position getBase2() {
		return base2;
	}
	
	public List<Position> getCrystals() {
		return crystals;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Player getPlayer1() {
		return player1;
	}
	
	public Player getPlayer2() {
		return player2;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isPlayer1Turn() {
		return player1Turn;
	}
	
	public static GameField fromJson(JsonObject json, int myId) {
		int width = json.get("width").getAsInt();
		int height = json.get("height").getAsInt();
		JsonArray players = json.getAsJsonArray("players");
		Player playerAt1 = Player.fromJson(players.get(0).getAsJsonObject());
		Player playerAt2 = Player.fromJson(players.get(1).getAsJsonObject());
		final Player player1 = (playerAt1.getId() == 1) ? playerAt1 : playerAt2;
		final Player player2 = (playerAt1.getId() == 1) ? playerAt2 : playerAt1;
		JsonArray bases = json.getAsJsonArray("bases");
		JsonObject base1Obj = bases.get(0).getAsJsonObject();
		JsonObject base2Obj = bases.get(0).getAsJsonObject();
		Position base1ObjPos = Position.fromJson(base1Obj.getAsJsonObject("position"));
		Position base2ObjPos = Position.fromJson(base2Obj.getAsJsonObject("position"));
		int base1Id = base1Obj.get("id").getAsInt();
		Position base1 = (base1Id == 1) ? base1ObjPos : base2ObjPos; 
		Position base2 = (base1Id == 1) ? base2ObjPos : base1ObjPos;
		List<Position> crystals = new ArrayList<>();
		JsonArray crystalsAr = json.getAsJsonArray("crystals");
		int size = crystalsAr.size();
		for(int i = 0; i < size; i++) {
			Position pos = Position.fromJson(crystalsAr.get(i).getAsJsonObject().getAsJsonObject("position"));
			crystals.add(pos);
		}
		boolean player1Turn = (myId == 1);
		return new GameField(width, height, player1, player2, crystals, base1, base2, player1Turn);
	}
	
	public List<Move> getViableMoves() {
		Player curPlayer = isPlayer1Turn() ? player1 : player2;
		Player oppPlayer = isPlayer1Turn() ? player2 : player1;
		if (curPlayer.getMovesLeft() == 0) {
			return NO_MOVES;
		}
		List<Move> moves = new ArrayList<>();
		moves.add(Move.NO_OP);
		// add movement moves
		int x = curPlayer.getPosition().getX();
		int y = curPlayer.getPosition().getY();
		if ((x > 0) && (!curPlayer.getPosition().move(Direction.LEFT, width, height).equals(oppPlayer.getPosition()))){
			moves.add(Move.MOVE_LEFT);
		}
		if ((y > 0) && (!curPlayer.getPosition().move(Direction.UP, width, height).equals(oppPlayer.getPosition()))) {
			moves.add(Move.MOVE_UP);
		}
		if ((x < width - 1) && (!curPlayer.getPosition().move(Direction.RIGHT, width, height).equals(oppPlayer.getPosition()))) {
			moves.add(Move.MOVE_RIGHT);
		}
		if ((y < height - 1) && (!curPlayer.getPosition().move(Direction.DOWN, width, height).equals(oppPlayer.getPosition()))) {
			moves.add(Move.MOVE_DOWN);
		}
		// add attack move
		if (curPlayer.getPosition().getManhattanDistance(oppPlayer.getPosition()) <= 2) {
			moves.add(Move.ATTACK);
		}
		// add collect move
		if (crystals.contains(curPlayer.getPosition())) {
			moves.add(Move.COLLECT);
		}
		return moves;
	}
	
	@Override
	public String toString() {
		return String.format("GameField %dx%d", width, height);
	}
}
