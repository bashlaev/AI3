package com.devoler.ai3.model;

public interface Evaluator {
	public long evaluate(GameField gameField);
	
	public int chooseDepth(GameField gameField);
}
