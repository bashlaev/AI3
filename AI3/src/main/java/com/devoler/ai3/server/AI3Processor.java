package com.devoler.ai3.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devoler.ai3.model.FeatureBasedEvaluator;
import com.devoler.ai3.model.Game;
import com.devoler.ai3.model.GameField;
import com.devoler.ai3.model.Move;
import com.devoler.ai3.model.StatUpgrade;
import com.devoler.ai3.model.Stats;
import com.devoler.minimax.MinimaxSolver;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class AI3Processor implements JSONProcessor {
	private static Logger logger = LoggerFactory.getLogger(AI3Processor.class);

	@Override
	public JsonElement process(JsonObject input) throws Exception {
		String state = input.get("gameState").getAsString();
		if (state.equalsIgnoreCase("CHOOSING_STATS")) {
			Stats stats1 = Stats.fromJson(input.getAsJsonObject("playerStats").get("1").getAsJsonObject());
			Stats stats2 = Stats.fromJson(input.getAsJsonObject("playerStats").get("2").getAsJsonObject());
			int myId = input.get("yourID").getAsInt();
			StatUpgrade statUpgrade = (myId == 1) ? stats1.upgrade(stats2): stats2.upgrade(stats1); 
			logger.info("Choosing stats request, 1: {}, 2: {}, upgrade: {}", stats1, stats2, statUpgrade);
			return statUpgrade.toJson();
		}
		if (state.equalsIgnoreCase("BATTLE")) {
			long startTime = System.currentTimeMillis();
			GameField gameField = GameField.fromJson(input.getAsJsonObject("gameField"), input.get("yourID").getAsInt());
			logger.info("Battle request, field: {}", gameField);
			Game game = new Game(new FeatureBasedEvaluator(1000, 100, -100), gameField);
			int numberOfMoves = game.getNumberOfMoves();
			logger.info("Moves: {}", numberOfMoves);
			for(int i = 0; i < numberOfMoves; i++) {
				logger.info("\tMove #{}: {} -> {}", i, game.getMove(i), ((Game) game.applyMove(i)).getGameField());
			}
			int selectedMove = MinimaxSolver.solveAB(game, 4);
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
