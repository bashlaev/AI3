package com.devoler.ai3.server;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devoler.ai3.model.Game;
import com.devoler.ai3.model.GameField;
import com.devoler.ai3.model.Move;
import com.devoler.ai3.model.Stats;
import com.devoler.ai3.model.StubEvaluator;
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
			logger.info("Choosing stats request, 1: {}, 2: {}", stats1, stats2);
			return new JsonObject();
		}
		if (state.equalsIgnoreCase("BATTLE")) {
			GameField gameField = GameField.fromJson(input.getAsJsonObject("gameField"), input.get("yourID").getAsInt());
			logger.info("Battle request, field: {}", gameField);
			Game game = new Game(new StubEvaluator(), gameField);
			int numberOfMoves = game.getNumberOfMoves();
			logger.info("Moves: {}", numberOfMoves);
			int selectedMove = new Random().nextInt(numberOfMoves);
			logger.info("Move selected: {}", selectedMove);
			JsonArray response = new JsonArray();
			for(Move move: game.getMove(selectedMove)) {
				logger.info("Single move added: {}", move.toJson());
				response.add(move.toJson());
			}
			return response;
		}
		return null;
	}

}
