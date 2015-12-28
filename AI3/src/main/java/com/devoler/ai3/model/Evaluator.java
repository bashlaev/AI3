package com.devoler.ai3.model;

public interface Evaluator {
	public long evaluate(GameField gameField, boolean maximizingPlayer);
	
	public int chooseDepth(GameField gameField);
	
	public boolean isGatheringMode(GameField gameField, boolean maxPlayer);
}
