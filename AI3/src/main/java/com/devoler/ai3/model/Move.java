package com.devoler.ai3.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public abstract class Move {
	public static enum Direction {
		LEFT, RIGHT, UP, DOWN;
	}
	
	public static final Move NO_OP = new Move() {
		@Override
		public GameField apply(GameField gameField) {
			Player player1 = gameField.getPlayer1();
			Player player2 = gameField.getPlayer2();
			Player curPlayer = gameField.isPlayer1Turn() ? player1: player2;
			int movesLeft = Math.max(0, curPlayer.getMovesLeft() - 1);
			curPlayer = new Player(curPlayer.getId(), curPlayer.getHealth(), curPlayer.getStats(), curPlayer.getPosition(), movesLeft, curPlayer.getCrystalsCarried(), curPlayer.getCrystalsCollected());
			if (gameField.isPlayer1Turn()) {
				player1 = curPlayer;
			} else {
				player2 = curPlayer;
			}
			return new GameField(gameField.getWidth(), gameField.getHeight(), player1, player2, gameField.getCrystals(), gameField.getBase1(), gameField.getBase2(), gameField.isPlayer1Turn());
		}
		
		public JsonObject toJson() {
			return new JsonObject();
		};
		
		public String toString() {
			return "NO_OP";
		};
	};
	
	public static final Move MOVE_UP = new MovementMove(Direction.UP);
	public static final Move MOVE_LEFT = new MovementMove(Direction.LEFT);
	public static final Move MOVE_RIGHT = new MovementMove(Direction.RIGHT);
	public static final Move MOVE_DOWN = new MovementMove(Direction.DOWN);
	
	private static class MovementMove extends Move {
		private final Direction direction;
		
		public MovementMove(final Direction direction) {
			this.direction = direction;
		}
		
		@Override
		public GameField apply(GameField gameField) {
			Player player1 = gameField.getPlayer1();
			Player player2 = gameField.getPlayer2();
			Player curPlayer = gameField.isPlayer1Turn() ? player1: player2;
			Position curBase = gameField.isPlayer1Turn() ? gameField.getBase1(): gameField.getBase2();
			int movesLeft = Math.max(0, curPlayer.getMovesLeft() - 1);
			// move
			Position newPosition = curPlayer
					.getPosition().move(direction, gameField.getWidth(), gameField.getHeight());
			// give up crystals
			int newCrystalsCarried = curPlayer.getCrystalsCarried();
			int newCrystalsCollected = curPlayer.getCrystalsCollected();
			if (newPosition.equals(curBase)) {
				newCrystalsCollected += newCrystalsCarried;
				newCrystalsCarried = 0;
			}
			curPlayer = new Player(curPlayer.getId(), curPlayer.getHealth(), curPlayer.getStats(), newPosition, movesLeft,
					newCrystalsCarried, newCrystalsCollected);
			if (gameField.isPlayer1Turn()) {
				player1 = curPlayer;
			} else {
				player2 = curPlayer;
			}
			return new GameField(gameField.getWidth(), gameField.getHeight(), player1, player2, gameField.getCrystals(), gameField.getBase1(), gameField.getBase2(), gameField.isPlayer1Turn());
		}
		
		@Override
		public JsonObject toJson() {
			JsonObject result = new JsonObject();
			result.addProperty("action", "MOVE");
			result.addProperty("moveDir", direction.name());
			return result;
		}
		
		public String toString() {
			return "MOVE " + direction.name();
		};
	}
	
	public static final Move ATTACK = new Move(){
		@Override
		public GameField apply(GameField gameField) {
			Player player1 = gameField.getPlayer1();
			Player player2 = gameField.getPlayer2();
			Player curPlayer = gameField.isPlayer1Turn() ? player1: player2;
			Player oppPlayer = gameField.isPlayer1Turn() ? player2: player1;
			
			// check if can attack
			int oppPlayerHealth = oppPlayer.getHealth();
			int distance = curPlayer.getPosition().getManhattanDistance(oppPlayer.getPosition());
			if (distance <= 2) {
				// attack
				oppPlayerHealth -= curPlayer.getStats().getAttack();
				oppPlayer = new Player(oppPlayer.getId(), oppPlayerHealth, oppPlayer.getStats(), oppPlayer.getPosition(), oppPlayer.getMovesLeft(),
						oppPlayer.getCrystalsCarried(), oppPlayer.getCrystalsCollected());
			}
			int movesLeft = Math.max(0, curPlayer.getMovesLeft() - 1);
			curPlayer = new Player(curPlayer.getId(), curPlayer.getHealth(), curPlayer.getStats(), curPlayer.getPosition(), movesLeft,
					curPlayer.getCrystalsCarried(), curPlayer.getCrystalsCollected());
			if (gameField.isPlayer1Turn()) {
				player1 = curPlayer;
				player2 = oppPlayer;
			} else {
				player1 = oppPlayer;
				player2 = curPlayer;
			}
			return new GameField(gameField.getWidth(), gameField.getHeight(), player1, player2, gameField.getCrystals(), gameField.getBase1(), gameField.getBase2(), gameField.isPlayer1Turn());
		}
		
		@Override
		public JsonObject toJson() {
			JsonObject result = new JsonObject();
			result.addProperty("action", "ATTACK");
			return result;
		}
		
		public String toString() {
			return "ATTACK";
		};
	};
	
	public static final Move COLLECT = new Move(){
		@Override
		public GameField apply(GameField gameField) {
			Player player1 = gameField.getPlayer1();
			Player player2 = gameField.getPlayer2();
			Player curPlayer = gameField.isPlayer1Turn() ? player1: player2;
			
			// check if can collect
			List<Position> crystals = gameField.getCrystals();
			int crystalsCarried = curPlayer.getCrystalsCarried();
			if (crystals.contains(curPlayer.getPosition())) {
				// collect
				crystalsCarried++;
				crystals = new ArrayList<>(gameField.getCrystals());
				crystals.remove(curPlayer.getPosition());
			}
			int movesLeft = Math.max(0, curPlayer.getMovesLeft() - 1);
			curPlayer = new Player(curPlayer.getId(), curPlayer.getHealth(), curPlayer.getStats(), curPlayer.getPosition(), movesLeft,
					crystalsCarried, curPlayer.getCrystalsCollected());
			if (gameField.isPlayer1Turn()) {
				player1 = curPlayer;
			} else {
				player2 = curPlayer;
			}
			return new GameField(gameField.getWidth(), gameField.getHeight(), player1, player2, crystals, gameField.getBase1(), gameField.getBase2(), gameField.isPlayer1Turn());
		}

		@Override
		public JsonObject toJson() {
			JsonObject result = new JsonObject();
			result.addProperty("action", "TAKE_A_CRYSTAL");
			return result;
		}
		
		public String toString() {
			return "COLLECT";
		};
	};
	
	public abstract GameField apply(GameField gameField);
	
	public abstract JsonObject toJson();
}
