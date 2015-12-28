package com.devoler.ai3.model;

public final class FeatureBasedEvaluator implements Evaluator {
	private final int collectedCrystalsWeight;
	private final int curHealthWeight;
	private final int oppHealthWeight;
	
	public FeatureBasedEvaluator(final int collectedCrystalsWeight, final int curHealthWeight, final int oppHealthWeight) {
		this.collectedCrystalsWeight = collectedCrystalsWeight;
		this.curHealthWeight = curHealthWeight;
		this.oppHealthWeight = oppHealthWeight;
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
		long score = 0;
		
		// apply score for collected crystals
		int curCollected = player1Turn ? player1.getCrystalsCollected() : player2.getCrystalsCollected();
		int oppCollected = player1Turn ? player2.getCrystalsCollected() : player1.getCrystalsCollected();
		score += (curCollected - oppCollected) * collectedCrystalsWeight;
		
		// apply health, health is measured in opp attacks
		int curHealth = player1Turn ? player1.getHealth() / player2.getStats().getAttack() : player2.getHealth() / player1.getStats().getAttack();
		score += curHealth * curHealthWeight;
		int oppHealth = (!player1Turn) ? player1.getHealth() / player2.getStats().getAttack() : player2.getHealth() / player1.getStats().getAttack();
		score += oppHealth * oppHealthWeight;
		
		return score;
	}

}
