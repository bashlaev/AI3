//package com.devoler.ai3.model;
//
//public final class AttackEvaluator implements Evaluator {
//	private final int curHealthWeight;
//	private final int oppHealthWeight;
//
//	public AttackEvaluator(final int curHealthWeight, final int oppHealthWeight) {
//		this.curHealthWeight = curHealthWeight;
//		this.oppHealthWeight = oppHealthWeight;
//	}
//
//	@Override
//	public long evaluate(GameField gameField, boolean maximizingPlayer) {
//		// check for victory
//		Boolean gameOver = gameField.isGameOver();
//		if (gameOver != null) {
//			// System.out.println("GameOver!");
//			return (gameOver.booleanValue() == gameField.isPlayer1Turn()) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
//		}
//		
//		boolean player1Turn = gameField.isPlayer1Turn();
//		Player player1 = gameField.getPlayer1();
//		Player player2 = gameField.getPlayer2();
//		Player curPlayer = player1Turn ? player1 : player2;
//		Player oppPlayer = player1Turn ? player2 : player1;
//		Position curBase = player1Turn ? gameField.getBase1() : gameField.getBase2();
//		Position oppBase = player1Turn ? gameField.getBase2() : gameField.getBase1();
//		long score = 0;
//
//		if (curPlayer.getCrystalsCarried() > 0) {
//			return -10000000;
//		}
//
//		// apply health, health is measured in opp attacks
//		int curHealth = player1Turn ? player1.getHealth() / player2.getStats().getAttack() : player2.getHealth()
//				/ player1.getStats().getAttack();
//		score += curHealth * curHealthWeight;
//		int oppHealth = (!player1Turn) ? player1.getHealth() / player2.getStats().getAttack() : player2.getHealth()
//				/ player1.getStats().getAttack();
//		score -= oppHealth * oppHealthWeight;
//
//		long proximityScore = curPlayer.getPosition().getManhattanDistance(oppPlayer.getPosition());
//		if (maximizingPlayer) {
//			score -= proximityScore;
//		} else {
//			score += proximityScore;
//		}
//
//		System.out.println(proximityScore + ", " + score);
//		return maximizingPlayer ? score: -score;
//	}
//
//	@Override
//	public int chooseDepth(GameField gameField) {
//		boolean player1Turn = gameField.isPlayer1Turn();
//		Player player1 = gameField.getPlayer1();
//		Player player2 = gameField.getPlayer2();
//		Player curPlayer = player1Turn ? player1 : player2;
//
//		int agility = curPlayer.getMovesLeft();
//		if (agility <= 2) {
//			return 4;
//		} else if (agility <= 3) {
//			return 3;
//		} else if (agility <= 4) {
//			return 2;
//		}
//		return 1;
//	}
//
//}
