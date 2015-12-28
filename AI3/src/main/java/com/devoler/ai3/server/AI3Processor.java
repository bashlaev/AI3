package com.devoler.ai3.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devoler.ai3.model.Evaluator;
import com.devoler.ai3.model.Game;
import com.devoler.ai3.model.GameField;
import com.devoler.ai3.model.GreedyDefensiveEvaluator;
import com.devoler.ai3.model.Move;
import com.devoler.ai3.model.StatUpgrade;
import com.devoler.ai3.model.StatUpgrades;
import com.devoler.ai3.model.Stats;
import com.devoler.minimax.MinimaxSolver;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class AI3Processor implements JSONProcessor {
	private static Logger logger = LoggerFactory.getLogger(AI3Processor.class);
	
	private static final Stats firstTarget = new Stats(1, 6, 1, 15);
	private static final Stats secondTargetB = new Stats(3, 7, 2, 1);
	private static final Stats secondTargetC = new Stats(4, 6, 3, 1);
	

	@Override
	public JsonElement process(JsonObject input) throws Exception {
		String state = input.get("gameState").getAsString();
		if (state.equalsIgnoreCase("CHOOSING_STATS")) {
			Stats stats1 = Stats.fromJson(input.getAsJsonObject("playerStats").get("1").getAsJsonObject());
			Stats stats2 = Stats.fromJson(input.getAsJsonObject("playerStats").get("2").getAsJsonObject());
			int myId = input.get("yourID").getAsInt();
			Stats myStats = (myId == 1) ? stats1 : stats2;
			Stats oppStats = (myId == 1) ? stats2 : stats1;
			final StatUpgrade statUpgrade;
			if (myStats.ge(new Stats(1, 7, 1, 8))) {
				if (oppStats.canMakeAgility8()) {
					statUpgrade = StatUpgrades.AGILITY;
				} else {
					statUpgrade = myStats.upgradeToTarget(secondTargetB);
				}
			} else if (myStats.ge(firstTarget)) {
				if (oppStats.canMakeAgility8() || (oppStats.canMakeAgility7())) {
					statUpgrade = StatUpgrades.AGILITY; 
				} else {
					statUpgrade = myStats.upgradeToTarget(secondTargetC);
				}
			} else {
				statUpgrade = myStats.upgradeToTarget(firstTarget);
			}
			logger.info("Choosing stats request, 1: {}, 2: {}, upgrade: {}", stats1, stats2, statUpgrade);
			return statUpgrade.toJson();
		}
		if (state.equalsIgnoreCase("BATTLE")) {
			long startTime = System.currentTimeMillis();
			GameField gameField = GameField.fromJson(input.getAsJsonObject("gameField"), input.get("yourID").getAsInt());
			logger.info("Battle request, field: {}", gameField);
//			Evaluator evaluator = new AttackEvaluator(0, 10000);
//			Evaluator evaluator = new GreedyEvaluator(10000, 1000, 100, 10);
			Evaluator evaluator = new GreedyDefensiveEvaluator();
			Game game = new Game(evaluator, gameField);
			int numberOfMoves = game.getNumberOfMoves();
			logger.info("Moves: {}", numberOfMoves);
//			for(int i = 0; i < numberOfMoves; i++) {
//				logger.info("\tMove #{}: {} -> {}", i, game.getMove(i), ((Game) game.applyMove(i)).getGameField());
//			}
			int depth = 1;//evaluator.chooseDepth(gameField);
			int selectedMove = depth >1 ? MinimaxSolver.solveAB(game, depth) : MinimaxSolver.solve(game, depth);
			// int selectedMove = new Random().nextInt(numberOfMoves);
			logger.info("Selected move #{}: {}", selectedMove, game.getMove(selectedMove));
			JsonArray response = new JsonArray();
			for(Move move: game.getMove(selectedMove)) {
				response.add(move.toJson());
			}
			long endTime = System.currentTimeMillis();
			
			logger.info("Processed in {} ms", endTime - startTime);
			return response;
		}
		return null;
	}

}
