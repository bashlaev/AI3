package com.devoler.minimax;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.Random;

import org.junit.Test;

public class MinimaxSolverTest {
	private static enum MinimaxSolverType {
		RANDOM {
			@Override
			public int solve(MinimaxGame game, int depth) {
				int numberOfMoves = game.getNumberOfMoves();
				return numberOfMoves == 0 ? MinimaxGame.NO_MOVE : new Random().nextInt(numberOfMoves);
			}
		},
		MINIMAX{
			@Override
			public int solve(MinimaxGame game, int depth) {
				return MinimaxSolver.solve(game, depth);
			}
		},
		MINIMAX_AB{
			@Override
			public int solve(MinimaxGame game, int depth) {
				return MinimaxSolver.solveAB(game, depth);
			}
		};
		
		public abstract int solve(MinimaxGame game, int depth);
	}
	
	private static final SecureRandom random = new SecureRandom();
	private static final TreeNode generateRandomBinaryTree(int depth) {
		return generateRandomBinaryBranch(0, depth, 0);
	}

	private static final TreeNode generateRandomBinaryBranch(long nodeValue, int depth, int level) {
		if (depth <= 0) {
			return new TreeNode(nodeValue);
		}
		long pivot = -nodeValue;
		int deviation = level + 1;
		long left = pivot + random.nextInt(deviation * 2 + 1) - deviation;
		long right = pivot + random.nextInt(deviation * 2 + 1) - deviation;
		TreeNode leftChild = generateRandomBinaryBranch(left, depth - 1, level + 1);
		TreeNode rightChild = generateRandomBinaryBranch(right, depth - 1, level + 1);
		return new TreeNode(nodeValue, leftChild, rightChild);
	}

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
	
	private static void assertSolver1Better(MinimaxSolverType solver1, int depth1, MinimaxSolverType solver2, int depth2) {
		final int ITERATIONS = 100;
		final int TREE_DEPTH = 12;
		long score = 0;
		for(int i = 0; i < ITERATIONS; i++) { 
			TreeNode tree = generateRandomBinaryTree(TREE_DEPTH);
			MinimaxGame game = new HardcodedTreeGame(tree);
			boolean player1Turn = random.nextBoolean();
			while(true) {
				int nextMove = player1Turn ? solver1.solve(game, depth1) : solver2.solve(game, depth2);
				if (nextMove == MinimaxGame.NO_MOVE) {
					break;
				}
				game = game.applyMove(nextMove);
				player1Turn = !player1Turn;
			}
			long gameScore = player1Turn ? game.evaluate() : -game.evaluate();
			score += gameScore;
		}
		assertTrue(score > 0);
	}
	
	@Test
	public void testSolvers() {
		assertSolver1Better(MinimaxSolverType.MINIMAX, 1, MinimaxSolverType.RANDOM, 0);
		for(int i = 1; i <= 5; i++) {
			assertSolver1Better(MinimaxSolverType.MINIMAX, i + 1, MinimaxSolverType.MINIMAX, i);
		}
		for(int i = 5; i <= 7; i++) {
			assertSolver1Better(MinimaxSolverType.MINIMAX_AB, i + 1, MinimaxSolverType.MINIMAX, i);
		}
	}

}
