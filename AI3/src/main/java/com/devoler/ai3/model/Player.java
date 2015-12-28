package com.devoler.ai3.model;

import com.google.gson.JsonObject;

public class Player {
	private final int id;
	private final Stats stats;
	private final int health;
	private final Position position;
	private final int movesLeft;
	private final int crystalsCarried;
	private final int crystalsCollected;

	Player(final int id, final int health, final Stats stats, final Position position, final int movesLeft,
			final int crystalsCarried, final int crystalsCollected) {
		this.id = id;
		this.health = health;
		this.stats = stats;
		this.position = position;
		this.movesLeft = movesLeft;
		this.crystalsCarried = crystalsCarried;
		this.crystalsCollected = crystalsCollected;
	}

	public static Player fromJson(JsonObject json) {
		int id = json.get("id").getAsInt();
		Stats stats = Stats.fromJson(json.getAsJsonObject("stats"));
		int health = json.get("healthPoints").getAsInt();
		int movesLeft = json.get("actionsPoints").getAsInt();
		Position position = Position.fromJson(json.getAsJsonObject("position"));
		int crystalsCarried = json.get("gatheredCrystals").getAsInt();
		int crystalsCollected = json.get("crystalsCount").getAsInt();
		return new Player(id, health, stats, position, movesLeft, crystalsCarried, crystalsCollected);
	}
	
	public int getId() {
		return id;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public Stats getStats() {
		return stats;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getCrystalsCarried() {
		return crystalsCarried;
	}
	
	public int getCrystalsCollected() {
		return crystalsCollected;
	}
	
	public int getMovesLeft() {
		return movesLeft;
	}
	
	@Override
	public String toString() {
		return String.format("Player %d: %s", id, stats.toString());
	}
}
