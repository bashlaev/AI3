package com.devoler.ai3.model;

import com.google.gson.JsonObject;

public final class Stats {
	public static final int HEALTH_POINTS = 15;

	private final int health;
	private final int agility;
	private final int attack;
	private final int sparePoints;
	
	Stats(final int health, final int agility, final int attack, final int sparePoints) {
		this.health = health;
		this.agility = agility;
		this.attack = attack;
		this.sparePoints = sparePoints;
	}
	
	public static Stats fromJson(JsonObject json) {
		int health = json.get("health").getAsInt();
		int agility = json.get("agility").getAsInt();
		int attack = json.get("attack").getAsInt();
		int sparePoints = json.get("statsPoints").getAsInt();
		return new Stats(health, agility, attack, sparePoints);
	}
	
	public int getNumberOfMoves(int crystalsCarried) {
		int moves = agility;
		for(int i = 0; i < crystalsCarried; i++) {
			moves = Math.max(1, moves - moves/2);
		}
		return moves;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getAgility() {
		return agility;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getSparePoints() {
		return sparePoints;
	}
	
	@Override
	public String toString() {
		return String.format("stats(h: %d, ag: %d, at: %d, pts: %d)", health, agility, attack, sparePoints);
	}
}
