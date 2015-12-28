package com.devoler.ai3.model;

public final class GreedyEvaluator implements Evaluator {
	private final int collectedCrystalsWeight;
	private final int carriedCrystalsWeight;
	private final int carriedProximityWeight;
	private final int proximityWeight;
	
	public GreedyEvaluator(final int collectedCrystalsWeight, final int carriedCrystalWeight, final int carriedProximityWeight, final int proximityWeight) {
		this.collectedCrystalsWeight = collectedCrystalsWeight;
		this.carriedCrystalsWeight = carriedCrystalWeight;
		this.carriedProximityWeight = carriedProximityWeight;
		this.proximityWeight = proximityWeight;
	}
	
	@Override
	public long evaluate(GameField gameField) {
		// check for victory
		Boolean gameOver = gameField.isGameOver();
		if (gameOver != null) {
			return (gameOver.booleanValue() == gameField.isPlayer1Turn()) ? Integer.MAX_VALUE : Integer.MIN_VALUE; 
		}
		
		boolean player1Turn = gameField.isPlayer1Turn();
		Player player1 = gameField.getPlayer1();
		Player player2 = gameField.getPlayer2();
		Player curPlayer = player1Turn ? player1: player2;
		Player oppPlayer = player1Turn ? player2: player1;
		Position curBase = player1Turn ? gameField.getBase1(): gameField.getBase2();
		Position oppBase = player1Turn ? gameField.getBase2(): gameField.getBase1();
		long score = 0;
		
		// SPECIAL CASE 1: if opp has crystals to win -> try to kill him
		int totalCrystals = player1.getCrystalsCollected() + player1.getCrystalsCarried()
				+ player2.getCrystalsCollected() + player2.getCrystalsCarried() + gameField.getCrystals().size();
		if (oppPlayer.getCrystalsCollected() + oppPlayer.getCrystalsCarried() > totalCrystals / 2) {
			// we can only win if we kill the opp player before he gets to the base
			return (-1000000L) * oppPlayer.getHealth() + (-1000L) * (curPlayer.getPosition().getManhattanDistance(oppPlayer.getPosition()));
		}
		
		// apply score for collected crystals
		int curCollected = player1Turn ? player1.getCrystalsCollected() : player2.getCrystalsCollected();
		int oppCollected = player1Turn ? player2.getCrystalsCollected() : player1.getCrystalsCollected();
		score += (curCollected - oppCollected) * collectedCrystalsWeight;

		// apply score for carried crystals
		boolean curCarried = curPlayer.getCrystalsCarried() > 0;
		boolean oppCarried = oppPlayer.getCrystalsCarried() > 0;
		if (curCarried) {
			score += carriedCrystalsWeight + (gameField.getWidth() + gameField.getHeight() - curPlayer.getPosition().getManhattanDistance(curBase)) * carriedProximityWeight;
		}
		if (oppCarried) {
			score -= carriedCrystalsWeight + (gameField.getWidth() + gameField.getHeight() - oppPlayer.getPosition().getManhattanDistance(oppBase)) * carriedProximityWeight;
		}

		if ((!curCarried) && (!gameField.getCrystals().isEmpty())) {
			// apply proximity to nearest crystal
			int nearestCrystalDistance = Integer.MAX_VALUE;
			for (Position crystal : gameField.getCrystals()) {
				int dist = crystal.getManhattanDistance(curPlayer.getPosition());
				if (dist < nearestCrystalDistance) {
					nearestCrystalDistance = dist;
				}
			}
			score += (gameField.getWidth() + gameField.getHeight() - nearestCrystalDistance) * proximityWeight;
		}
		if ((!oppCarried) && (!gameField.getCrystals().isEmpty())) {
			// apply proximity to nearest crystal
			int nearestCrystalDistance = Integer.MAX_VALUE;
			for (Position crystal : gameField.getCrystals()) {
				int dist = crystal.getManhattanDistance(oppPlayer.getPosition());
				if (dist < nearestCrystalDistance) {
					nearestCrystalDistance = dist;
				}
			}
			score -= (gameField.getWidth() + gameField.getHeight() - nearestCrystalDistance) * proximityWeight;
		}
		
		return score;
	}
	
	@Override
	public int chooseDepth(GameField gameField) {
		boolean player1Turn = gameField.isPlayer1Turn();
		Player player1 = gameField.getPlayer1();
		Player player2 = gameField.getPlayer2();
		Player curPlayer = player1Turn ? player1: player2;
		
		int agility = curPlayer.getStats().getAgility();
		if (agility <= 2) { 
			return 3;
		} else if (agility <= 4) {
			return 2;
		}
		return 1;
	}

}
