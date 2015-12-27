package com.devoler.minimax;

public class HardcodedTreeGame implements MinimaxGame {
	private final TreeNode tree;
	
	public HardcodedTreeGame(TreeNode tree) {
		this.tree = tree;
	}

	@Override
	public long evaluate() {
		return tree.getValue();
	}

	@Override
	public int getNumberOfMoves() {
		return tree.getChildCount();
	}

	@Override
	public MinimaxGame applyMove(int moveIndex) {
		return new HardcodedTreeGame(tree.getChildAt(moveIndex));
	}

}
