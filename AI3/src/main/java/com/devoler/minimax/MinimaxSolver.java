package com.devoler.minimax;

import org.apache.commons.lang3.tuple.Pair;

public final class MinimaxSolver {
	private MinimaxSolver() {
		throw new RuntimeException("Should not be instantiated");
	}

	private static long nodesVisited;
	private static long timeEvaluating;

	public static int solve(MinimaxGame game, int depth) {
		return solveRecursively(game, depth, true).getKey();
	}

	public static int solveAB(MinimaxGame game, int depth) {
		nodesVisited = 0;
		timeEvaluating = 0;
		long time = System.currentTimeMillis();
		int result = solveRecursivelyAB(game, depth, Long.MIN_VALUE, Long.MAX_VALUE, true).getKey();
		// System.out.println("Nodes visited: " + nodesVisited + ", time: " + (System.currentTimeMillis() - time) +
		// " ms, evals: " + timeEvaluating / 1000000 + " ms");
		return result;
	}

	private static long evaluateFor(MinimaxGame game, boolean maximizingPlayer) {
		long time = System.nanoTime();
		long result = maximizingPlayer ? game.evaluate() : -game.evaluate();
		timeEvaluating += (System.nanoTime() - time);
		return result;
	}

	private static Pair<Integer, Long> solveRecursively(MinimaxGame game, int depth, boolean maximizingPlayer) {
		int bestMove = MinimaxGame.NO_MOVE;
		int numberOfMoves = game.getNumberOfMoves();
		if ((depth == 0) || (numberOfMoves <= 0)) {
			return Pair.of(bestMove, evaluateFor(game, maximizingPlayer));
		}

		long bestValue = maximizingPlayer ? Long.MIN_VALUE : Long.MAX_VALUE;
		for (int moveIndex = 0; moveIndex < numberOfMoves; moveIndex++) {
			Pair<Integer, Long> value = solveRecursively(game.applyMove(moveIndex), depth - 1, !maximizingPlayer);
			boolean isBestMove = maximizingPlayer ? (value.getRight() > bestValue) : (value.getRight() < bestValue);
			if (isBestMove) {
				bestValue = value.getRight();
				bestMove = moveIndex;
			}
		}

		return Pair.of(bestMove, bestValue);
	}

	private static Pair<Integer, Long> solveRecursivelyAB(MinimaxGame game, int depth, long a, long b,
			boolean maximizingPlayer) {
		nodesVisited++;
		int bestMove = MinimaxGame.NO_MOVE;
		int numberOfMoves = game.getNumberOfMoves();
		if ((depth == 0) || (numberOfMoves <= 0)) {
			return Pair.of(bestMove, evaluateFor(game, maximizingPlayer));
		}

		long bestValue = maximizingPlayer ? Long.MIN_VALUE : Long.MAX_VALUE;

		if (maximizingPlayer) {
			for (int moveIndex = 0; moveIndex < numberOfMoves; moveIndex++) {
				Pair<Integer, Long> value = solveRecursivelyAB(game.applyMove(moveIndex), depth - 1, a, b, false);
				boolean isBestMove = value.getRight() > bestValue;
				if (isBestMove) {
					bestValue = value.getRight();
					bestMove = moveIndex;
					a = Math.max(a, bestValue);
				}
				if (b <= a) {
					break;
				}
			}
		} else {
			for (int moveIndex = 0; moveIndex < numberOfMoves; moveIndex++) {
				Pair<Integer, Long> value = solveRecursivelyAB(game.applyMove(moveIndex), depth - 1, a, b, true);
				boolean isBestMove = value.getRight() < bestValue;
				if (isBestMove) {
					bestValue = value.getRight();
					bestMove = moveIndex;
					b = Math.min(b, bestValue);
				}
				if (b <= a) {
					break;
				}
			}
		}

		return Pair.of(bestMove, bestValue);
	}
}
