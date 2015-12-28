package com.devoler.ai3.model;

import com.devoler.minimax.MinimaxSolver;

public class GameRunner {
	public static final int run(Evaluator ev1, int depth1, Evaluator ev2, int depth2) {
		int ITERATIONS = 100;
		int maxMoves = 100;
		int score = 0;
		for(int i = 0; i < ITERATIONS; i++) {
			GameField field = GameField.getStartPosition();
			int moves = 0;
			boolean result;
			while(true) {
				System.out.print(field);
				Game game1 = new Game(ev1, field);
				int move1 = MinimaxSolver.solveAB(game1, depth1);
				System.out.print(" -> " + game1.getMove(move1) + " ->");
				field = ((Game) game1.applyMove(move1)).getGameField();
				System.out.println(field);
				Boolean gameOver = field.isGameOver();
				if (gameOver != null) {
					result = gameOver.booleanValue();
					break;
				}
				Game game2 = new Game(ev2, field);
				int move2 = MinimaxSolver.solveAB(game2, depth2);
				field = ((Game) game2.applyMove(move2)).getGameField();
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
		System.out.println(run(new FeatureBasedEvaluator(100, 10, -10), 3, new FeatureBasedEvaluator(100, 100, -100), 2));
	}
}
