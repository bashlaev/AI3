package com.devoler.ai3.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.devoler.minimax.MinimaxGame;

public class Game implements MinimaxGame {
	private final Evaluator evaluator;
	private final GameField gameField;
	private final List<List<Move>> moves;
	private final Set<GameField> gameFields = new HashSet<>();

	public Game(final Evaluator evaluator, final GameField gameField) {
		this.evaluator = evaluator;
		this.gameField = gameField;
		moves = new ArrayList<>();
		addMoves(gameField, new ArrayList<Move>());

		// for(List<Move> move: moves) {
		// System.out.println("\t" + move);
		// }
	}

	private final void addMoves(GameField field, List<Move> currentPath) {
		Move lastMove = currentPath.isEmpty() ? null : currentPath.get(currentPath.size() - 1);
		List<Move> movesFromHere = field.getViableMoves(lastMove, evaluator.isGatheringMode(field, true));
		if (movesFromHere.isEmpty()) {
			addMove(currentPath, field);
			return;
		}
		for (Move move : movesFromHere) {
			List<Move> newPath = new ArrayList<>(currentPath);
			newPath.add(move);
			addMoves(move.apply(field), newPath);
		}
	}

	private final void addMove(List<Move> move, GameField field) {
		// System.out.println("move to add: " + move + ", field: " + field);
		if (!gameFields.contains(field)) {
			// System.out.println("Move " + move.toString() + " added");
			moves.add(move);
			gameFields.add(field);
		} else {
			// System.out.println("Move " + move.toString() + " ignored, already there");
		}
	}
	
	public GameField getGameField() {
		return gameField;
	}

	@Override
	public long evaluate(boolean maximizingPlayer) {
		return evaluator.evaluate(gameField, maximizingPlayer);
	}

	@Override
	public int getNumberOfMoves() {
		return moves.size();
	}

	public List<Move> getMove(int moveIndex) {
		return moves.get(moveIndex);
	}

	@Override
	public MinimaxGame applyMove(int moveIndex) {
		List<Move> move = moves.get(moveIndex);
		GameField field = gameField;
		for (Move singleMove : move) {
			field = singleMove.apply(field);
		}
		// apply base effects if its player2 turn
		Player player1 = field.getPlayer1();
		Player player2 = field.getPlayer2();
		int player1Health = player1.getHealth();
		int player2Health = player2.getHealth();
		if (!field.isPlayer1Turn()) {
			// apply bases healing
			if (player1.getPosition().equals(field.getBase1())) {
				player1Health = Math.min(player1.getStats().getHealth() * Stats.HEALTH_POINTS, player1.getHealth()
						+ Stats.HEALTH_POINTS);
			}
			if (player2.getPosition().equals(field.getBase2())) {
				player2Health = Math.min(player2.getStats().getHealth() * Stats.HEALTH_POINTS, player2.getHealth()
						+ Stats.HEALTH_POINTS);
			}
			// apply bases attack
			if (player1.getPosition().equals(field.getBase2())) {
				player1Health -= Stats.HEALTH_POINTS;
			}
			if (player2.getPosition().equals(field.getBase1())) {
				player2Health -= Stats.HEALTH_POINTS;
			}
		}
		int player1Moves = player1.getStats().getNumberOfMoves(player1.getCrystalsCarried());
		int player2Moves = player2.getStats().getNumberOfMoves(player2.getCrystalsCarried());

		player1 = new Player(player1.getId(), player1Health, player1.getStats(), player1.getPosition(), player1Moves,
				player1.getCrystalsCarried(), player1.getCrystalsCollected());
		player2 = new Player(player2.getId(), player2Health, player2.getStats(), player2.getPosition(), player2Moves,
				player2.getCrystalsCarried(), player2.getCrystalsCollected());

		field = new GameField(field.getWidth(), field.getHeight(), player1, player2, field.getCrystals(),
				field.getBase1(), field.getBase2(), !field.isPlayer1Turn());
		return new Game(evaluator, field);
	}

}
