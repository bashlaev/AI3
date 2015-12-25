package com.devoler.minimax;

import java.util.ArrayList;
import java.util.List;

public class HardcodedTreeGame implements MinimaxGame {
	public static class TreeNode {
		private final List<TreeNode> children = new ArrayList<>();
		private final long value;
		
		public TreeNode(long value, TreeNode... children) {
			this.value = value;
			for(TreeNode child: children) {
				this.children.add(child);
			}
		}
		
		public long getValue() {
			return value;
		}
		
		public int getChildCount() {
			return children.size();
		}
		
		public TreeNode getChildAt(int childIndex) {
			return children.get(childIndex);
		}
	}
	
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
