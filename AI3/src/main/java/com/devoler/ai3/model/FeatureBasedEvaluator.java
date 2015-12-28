//package com.devoler.ai3.model;
//
//public final class FeatureBasedEvaluator implements Evaluator {
//	private final int collectedCrystalsWeight;
//	private final int curHealthWeight;
//	private final int oppHealthWeight;
//	
//	public FeatureBasedEvaluator(final int collectedCrystalsWeight, final int curHealthWeight, final int oppHealthWeight) {
//		this.collectedCrystalsWeight = collectedCrystalsWeight;
//		this.curHealthWeight = curHealthWeight;
//		this.oppHealthWeight = oppHealthWeight;
//	}
//	
//	@Override
//	public long evaluate(GameField gameField) {
//		// check for victory
//		Boolean gameOver = gameField.isGameOver();
//		if (gameOver != null) {
//			return (gameOver.booleanValue() == gameField.isPlayer1Turn()) ? Integer.MAX_VALUE : Integer.MIN_VALUE; 
//		}
//		
//		boolean player1Turn = gameField.isPlayer1Turn();
//		Player player1 = gameField.getPlayer1();
//		Player player2 = gameField.getPlayer2();
//		Player curPlayer = player1Turn ? player1: player2;
//		Player oppPlayer = player1Turn ? player2: player1;
//		long score = 0;
//		
//		// SPECIAL CASE 1: if opp has crystals to win -> try to kill him
//		int totalCrystals = player1.getCrystalsCollected() + player1.getCrystalsCarried()
//				+ player2.getCrystalsCollected() + player2.getCrystalsCarried() + gameField.getCrystals().size();
//		if (oppPlayer.getCrystalsCollected() + oppPlayer.getCrystalsCarried() > totalCrystals / 2) {
//			// we can only win if we kill the opp player before he gets to the base
//			return (-1000000L) * oppPlayer.getHealth() + (-1000L) * (curPlayer.getPosition().getManhattanDistance(oppPlayer.getPosition()));
//		}
//		
//		// apply score for collected crystals
//		int curCollected = player1Turn ? player1.getCrystalsCollected() : player2.getCrystalsCollected();
//		int oppCollected = player1Turn ? player2.getCrystalsCollected() : player1.getCrystalsCollected();
//		score += (curCollected - oppCollected) * collectedCrystalsWeight;
//		
//		// apply health, health is measured in opp attacks
//		int curHealth = player1Turn ? player1.getHealth() / player2.getStats().getAttack() : player2.getHealth() / player1.getStats().getAttack();
//		score += curHealth * curHealthWeight;
//		int oppHealth = (!player1Turn) ? player1.getHealth() / player2.getStats().getAttack() : player2.getHealth() / player1.getStats().getAttack();
//		score += oppHealth * oppHealthWeight;
//		
//		// add random metric - distance to nearest crystal
//		int nearestCrystalDistance = Integer.MAX_VALUE;
//		Position curPosition = player1Turn ? player1.getPosition() : player2.getPosition();
//		for (Position crystal : gameField.getCrystals()) {
//			int dist = crystal.getManhattanDistance(curPosition);
//			if (dist < nearestCrystalDistance) {
//				nearestCrystalDistance = dist;
//			}
//		}
//		score += nearestCrystalDistance * (-10);
//		
//		return score;
//	}
//
//	@Override
//	public int chooseDepth(GameField gameField) {
//		boolean player1Turn = gameField.isPlayer1Turn();
//		Player player1 = gameField.getPlayer1();
//		Player player2 = gameField.getPlayer2();
//		Player curPlayer = player1Turn ? player1: player2;
//		
//		int agility = curPlayer.getStats().getAgility();
//		if (agility <= 2) { 
//			return 3;
//		} else if (agility <= 4) {
//			return 2;
//		}
//		return 1;
//	}
//}
