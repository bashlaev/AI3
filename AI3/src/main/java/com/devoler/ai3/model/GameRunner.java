package com.devoler.ai3.model;

import com.devoler.minimax.MinimaxSolver;

public class GameRunner {
	public static final int run(Evaluator ev1, Stats stats1, Evaluator ev2, Stats stats2, int iterations) {
		int run1 = doRun(ev1, stats1, ev2, stats2, iterations);
		int run2 = doRun(ev2, stats2, ev1, stats1, iterations);
		System.out.println("fw: " + run1 + ", rev: " + run2);
		return run1 - run2;
	}

	private static final int doRun(Evaluator ev1, Stats stats1, Evaluator ev2, Stats stats2, int iterations) {
		int maxMoves = 100;
		int score = 0;
		for (int i = 0; i < iterations; i++) {
			GameField field = GameField.getStartPosition(stats1, stats2);
			int moves = 0;
			boolean result;
			while (true) {
				// System.out.print(field);
				Game game1 = new Game(ev1, field);
				int move1 = MinimaxSolver.solveAB(game1, ev1.chooseDepth(field));
				 System.out.println("1 -> " + game1.getMove(move1));
				field = ((Game) game1.applyMove(move1)).getGameField();
				// System.out.println(field);
				Boolean gameOver = field.isGameOver();
				if (gameOver != null) {
					result = gameOver.booleanValue();
					break;
				}
				// System.out.print(field);
				Game game2 = new Game(ev2, field);
				int move2 = MinimaxSolver.solveAB(game2, ev2.chooseDepth(field));
				 System.out.println("2 -> " + game2.getMove(move2));
				field = ((Game) game2.applyMove(move2)).getGameField();
				// System.out.println(field);
				gameOver = field.isGameOver();
				if (gameOver != null) {
					result = gameOver.booleanValue();
					break;
				}
				moves++;
				if (moves > maxMoves) {
					System.out.println("passive end");
					result = field.getPassiveWinner();
					break;
				}
			}
			if (result) {
				System.out.println("player1 wins on move " + moves);
				score++;
			} else {
				System.out.println("player2 wins on move " + moves);
				score--;
			}
		}
		return score;
	}

	public static void main(String[] args) {
		// System.out.println(run(new FeatureBasedEvaluator(100, 10, -10), 3, new GreedyEvaluator(10000, 1000, 100, 10),
		// 3, 1));
		System.out.println(run(new GreedyEvaluator(10000, 1000, 100, 10), new Stats(3, 7, 2, 1), new GreedyEvaluator(10000, 1000, 100, 10), new Stats(3, 6, 4, 1),
				5));
	}
}
