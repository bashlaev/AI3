package com.devoler.ai3.model;

public class GreedyDefensiveEvaluator implements Evaluator {
	private final GreedyEvaluator greedyEvaluator = new GreedyEvaluator(10000, 1000, 100, 10);

	@Override
	public long evaluate(GameField gameField, boolean maximizingPlayer) {
		if (isGatheringMode(gameField, maximizingPlayer)) {
			return greedyEvaluator.evaluate(gameField, maximizingPlayer);
		}

		// check for victory
		Boolean gameOver = gameField.isGameOver();
		if (gameOver != null) {
			// System.out.println("GameOver!");
			long score = (gameOver.booleanValue() == gameField.isPlayer1Turn()) ? Integer.MAX_VALUE : Integer.MIN_VALUE; 
			return maximizingPlayer ? score: -score;
		}
		
		// switch players
		
//		System.out.println("***** defensiveMode!!!");

		boolean player1Turn = !gameField.isPlayer1Turn();
		Player player1 = gameField.getPlayer1();
		Player player2 = gameField.getPlayer2();
		Player curPlayer = player1Turn ? player1 : player2;
		Player oppPlayer = player1Turn ? player2 : player1;
		Position curBase = player1Turn ? gameField.getBase1() : gameField.getBase2();

		// defensive mode
		long score = 0;
		// if not at the base -> go to base
		int proximity = curPlayer.getPosition().getManhattanDistance(curBase);
		score -= proximity * 100000;

		// if at the base -> attack if possible
		score -= oppPlayer.getHealth();
		
		return score;
	}

	@Override
	public int chooseDepth(GameField gameField) {
		return 1;
	}

	@Override
	public boolean isGatheringMode(GameField gameField, boolean maxPlayer) {
		return gameField.isGatheringMode(maxPlayer);
	}

}
