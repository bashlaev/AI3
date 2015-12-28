package com.devoler.minimax;

/**
 * An interface that denotes a class of 2-player turn-based games that can be solved by minimax.
 * 
 * @author homer
 * 
 */
public interface MinimaxGame {
	/**
	 * Denotes the absence of a move.
	 */
	public static final int NO_MOVE = -1;
	
	/**
	 * Evaluates the game for current player. The higher the score, the better his situation is supposed to be. 
	 * 
	 * @return the game's evaluation for the current player.
	 */
	public long evaluate(boolean maximizingPlayer);
	
	/**
	 * Returns the number of currently available moves.
	 * 
	 * @return the number of available moves, non-negative.
	 */
	public int getNumberOfMoves();
	
	/**
	 * Applies the move with the specified index. This method should have no effect on this instance, instead it should
	 * create the new one and return it. In the new instance, the move should already be done, and the turn switched.
	 * 
	 * @param moveIndex The index of the move to apply.
	 * @return a new game instance if the move was successfully applied
	 * @throws IllegalArgumentException if the moveIndex is not appropriate.
	 */
	public MinimaxGame applyMove(int moveIndex);
}
