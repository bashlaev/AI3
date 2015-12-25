package com.devoler.minimax;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.devoler.minimax.HardcodedTreeGame.TreeNode;

public class MinimaxSolverTest {

	@Test
	public void testDepth1() {
		TreeNode nodeL = new TreeNode(-5);
		TreeNode nodeR = new TreeNode(-10);
		TreeNode tree = new TreeNode(7, nodeL, nodeR);
		HardcodedTreeGame game = new HardcodedTreeGame(tree);
		assertEquals(1, MinimaxSolver.solve(game, 1));
		assertEquals(1, MinimaxSolver.solve(game, 2));
		assertEquals(1, MinimaxSolver.solve(game, 3));
		assertEquals(1, MinimaxSolver.solve(game, 4));
		assertEquals(1, MinimaxSolver.solveAB(game, 1));
		assertEquals(1, MinimaxSolver.solveAB(game, 2));
		assertEquals(1, MinimaxSolver.solveAB(game, 3));
		assertEquals(1, MinimaxSolver.solveAB(game, 4));
	}
	
	@Test
	public void testDepth2() {
		TreeNode nodeLL = new TreeNode(12);
		TreeNode nodeLR = new TreeNode(10);
		TreeNode nodeL = new TreeNode(-5, nodeLL, nodeLR);
		TreeNode nodeRL = new TreeNode(15);
		TreeNode nodeRR = new TreeNode(8);
		TreeNode nodeR = new TreeNode(-10, nodeRL, nodeRR);
		TreeNode tree = new TreeNode(7, nodeL, nodeR);
		HardcodedTreeGame game = new HardcodedTreeGame(tree);
		assertEquals(1, MinimaxSolver.solve(game, 1));
		assertEquals(0, MinimaxSolver.solve(game, 2));
		assertEquals(0, MinimaxSolver.solve(game, 3));
		assertEquals(0, MinimaxSolver.solve(game, 4));
		assertEquals(1, MinimaxSolver.solveAB(game, 1));
		assertEquals(0, MinimaxSolver.solveAB(game, 2));
		assertEquals(0, MinimaxSolver.solveAB(game, 3));
		assertEquals(0, MinimaxSolver.solveAB(game, 4));
	}

	@Test
	public void testDepth3() {
		TreeNode nodeLL = new TreeNode(12);
		TreeNode nodeLR = new TreeNode(10);
		TreeNode nodeL = new TreeNode(-5, nodeLL, nodeLR);
		TreeNode nodeRL = new TreeNode(15);
		TreeNode nodeRRL = new TreeNode(-20);
		TreeNode nodeRRR = new TreeNode(0);
		TreeNode nodeRR = new TreeNode(8, nodeRRL, nodeRRR);
		TreeNode nodeR = new TreeNode(-10, nodeRL, nodeRR);
		TreeNode tree = new TreeNode(7, nodeL, nodeR);
		HardcodedTreeGame game = new HardcodedTreeGame(tree);
		assertEquals(1, MinimaxSolver.solve(game, 1));
		assertEquals(0, MinimaxSolver.solve(game, 2));
		assertEquals(1, MinimaxSolver.solve(game, 3));
		assertEquals(1, MinimaxSolver.solve(game, 4));
		assertEquals(1, MinimaxSolver.solveAB(game, 1));
		assertEquals(0, MinimaxSolver.solveAB(game, 2));
		assertEquals(1, MinimaxSolver.solveAB(game, 3));
		assertEquals(1, MinimaxSolver.solveAB(game, 4));
	}
	
	@Test
	public void testDepth4() {
		TreeNode nodeLL = new TreeNode(12);
		TreeNode nodeLR = new TreeNode(10);
		TreeNode nodeL = new TreeNode(-5, nodeLL, nodeLR);
		TreeNode nodeRL = new TreeNode(15);
		TreeNode nodeRRL = new TreeNode(-20, new TreeNode(8), new TreeNode(100));
		TreeNode nodeRRR = new TreeNode(0);
		TreeNode nodeRR = new TreeNode(8, nodeRRL, nodeRRR);
		TreeNode nodeR = new TreeNode(-10, nodeRL, nodeRR);
		TreeNode tree = new TreeNode(7, nodeL, nodeR);
		HardcodedTreeGame game = new HardcodedTreeGame(tree);
		assertEquals(1, MinimaxSolver.solve(game, 1));
		assertEquals(0, MinimaxSolver.solve(game, 2));
		assertEquals(1, MinimaxSolver.solve(game, 3));
		assertEquals(0, MinimaxSolver.solve(game, 4));
		assertEquals(1, MinimaxSolver.solveAB(game, 1));
		assertEquals(0, MinimaxSolver.solveAB(game, 2));
		assertEquals(1, MinimaxSolver.solveAB(game, 3));
		assertEquals(0, MinimaxSolver.solveAB(game, 4));
	}

}
