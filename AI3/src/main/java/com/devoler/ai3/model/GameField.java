package com.devoler.ai3.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.devoler.ai3.model.Move.Direction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class GameField {
	private static final List<Move> NO_MOVES = new ArrayList<>();
	private static final List<Move> NO_OP = new ArrayList<>(Arrays.asList(Move.NO_OP));

	private final int width;
	private final int height;
	private final Player player1;
	private final Player player2;
	private final List<Position> crystals;
	private final Position base1;
	private final Position base2;
	private final boolean player1Turn;

	private final int hashCode;

	GameField(final int width, final int height, final Player player1, final Player player2,
			final List<Position> crystals, final Position base1, final Position base2, final boolean player1Turn) {
		this.width = width;
		this.height = height;
		this.player1 = player1;
		this.player2 = player2;
		this.crystals = crystals;
		this.base1 = base1;
		this.base2 = base2;
		this.player1Turn = player1Turn;

		hashCode = new HashCodeBuilder().append(width).append(height).append(player1).append(player2).append(crystals)
				.append(base1).append(base2).append(player1Turn).toHashCode();
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
		for (int i = 0; i < size; i++) {
			Position pos = Position.fromJson(crystalsAr.get(i).getAsJsonObject().getAsJsonObject("position"));
			crystals.add(pos);
		}
		boolean player1Turn = (myId == 1);
		return new GameField(width, height, player1, player2, crystals, base1, base2, player1Turn);
	}

	public List<Move> getViableMoves(Move lastMove) {
		Player curPlayer = isPlayer1Turn() ? player1 : player2;
		Player oppPlayer = isPlayer1Turn() ? player2 : player1;
		if (curPlayer.getMovesLeft() == 0) {
			return NO_MOVES;
		}
		if (lastMove == Move.NO_OP) {
			return NO_OP;
		}
		List<Move> moves = new ArrayList<>();
		// add attack move
		if (curPlayer.getPosition().getManhattanDistance(oppPlayer.getPosition()) <= 2) {
			moves.add(Move.ATTACK);
		}
		// add collect move
		if (crystals.contains(curPlayer.getPosition())) {
			moves.add(Move.COLLECT);
		}
		// add movement moves
		int x = curPlayer.getPosition().getX();
		int y = curPlayer.getPosition().getY();
		if ((x > 0) && (!curPlayer.getPosition().move(Direction.LEFT, width, height).equals(oppPlayer.getPosition()))) {
			moves.add(Move.MOVE_LEFT);
		}
		if ((y > 0) && (!curPlayer.getPosition().move(Direction.UP, width, height).equals(oppPlayer.getPosition()))) {
			moves.add(Move.MOVE_UP);
		}
		if ((x < width - 1)
				&& (!curPlayer.getPosition().move(Direction.RIGHT, width, height).equals(oppPlayer.getPosition()))) {
			moves.add(Move.MOVE_RIGHT);
		}
		if ((y < height - 1)
				&& (!curPlayer.getPosition().move(Direction.DOWN, width, height).equals(oppPlayer.getPosition()))) {
			moves.add(Move.MOVE_DOWN);
		}
		// add NO_OP move
		moves.add(Move.NO_OP);
		return moves;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof GameField) {
			GameField that = (GameField) obj;
			return (this.width == that.width) && (this.height == that.height) && (this.player1.equals(that.player1))
					&& (this.player2.equals(that.player2)) && (this.crystals.equals(that.crystals))
					&& (this.base1.equals(that.base1)) && (this.base2.equals(that.base2)) && (this.player1Turn == that.player1Turn);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	// FALSE if player1 lost, TRUE if player1 won, null if game not over
	public Boolean isGameOver() {
		// TODO: better victory conditions
		if (player1.getHealth() <= 0) {
			return Boolean.FALSE;
		}
		if (player2.getHealth() <= 0) {
			return Boolean.TRUE;
		}
		int totalCrystals = player1.getCrystalsCollected() + player1.getCrystalsCarried()
				+ player2.getCrystalsCollected() + player2.getCrystalsCarried() + crystals.size();
		int crystalsToWin = totalCrystals / 2 + 1;
		if (player1.getCrystalsCollected() >= crystalsToWin) {
			return Boolean.TRUE;
		}
		if (player2.getCrystalsCollected() >= crystalsToWin) {
			return Boolean.FALSE;
		}
		return null;
	}
	
	public boolean getPassiveWinner() {
		if (player1.getCrystalsCollected() > player2.getCrystalsCollected()) {
			return true;
		} else if (player1.getCrystalsCollected() < player2.getCrystalsCollected()) {
			return false;
		} 
		if (player1.getCrystalsCarried() > player2.getCrystalsCarried()) {
			return true;
		} else if (player1.getCrystalsCarried() < player2.getCrystalsCarried()) {
			return false;
		}
		if (player1.getHealth() > player2.getHealth()) {
			return true;
		} else if (player1.getHealth() < player2.getHealth()) {
			return false;
		}
		return false;
	}
	
	private static List<Position> generateRandomCrystals(int width, int height) {
		List<Position> crystals = new ArrayList<>();
		crystals.add(new Position(width / 2, height / 2));
		Random random = new Random();
		int minCrystals = 3;
		int maxCrystals = 5;
		int crystalsToGenerate = random.nextInt(maxCrystals - minCrystals) + minCrystals;
		while (true) {
			Position p = new Position(random.nextInt(width / 2), random.nextInt(height / 2));
			if (crystals.contains(p)) {
				continue;
			}
			if (p.equals(new Position(0, 0))) {
				continue;
			}
			crystals.add(p);
			crystals.add(new Position(width - p.getX() - 1, height - p.getY() - 1));
			crystalsToGenerate--;
			if (crystalsToGenerate == 0) {
				break;
			}
		}
		return crystals;
	}
	
	public static GameField getStartPosition(Stats stats1, Stats stats2) {
		Player player1 = new Player(1, stats1.getHealth() * Stats.HEALTH_POINTS, stats1, new Position(0, 0), stats1.getAgility(), 0, 0);
		Player player2 = new Player(2, stats2.getHealth() * Stats.HEALTH_POINTS, stats2, new Position(8, 10), stats2.getAgility(), 0, 0);
		return new GameField(9, 11, player1, player2, generateRandomCrystals(9, 11), new Position(0, 0), new Position(8, 10), true);
	}
	
	@Override
	public String toString() {
		return String.format("GameField %dx%d, player1: %s, player2: %s, crystals: %s", width, height, player1, player2, crystals);
	}
	
	public static void main(String[] args) {
//		GameField field = GameField.getStartPosition();
//		System.out.println(field);
//		Game game = new Game(new FeatureBasedEvaluator(1000, 100, -100), field);
//		MinimaxSolver.solveAB(game, 4);
	}
}
