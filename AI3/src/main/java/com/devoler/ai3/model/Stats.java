package com.devoler.ai3.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.JsonObject;

public final class Stats {
	public static final int HEALTH_POINTS = 15;

	private final int health;
	private final int agility;
	private final int attack;
	private final int sparePoints;
	
	private final int hashCode;
	
	Stats(final int health, final int agility, final int attack, final int sparePoints) {
		this.health = health;
		this.agility = agility;
		this.attack = attack;
		this.sparePoints = sparePoints;
		hashCode = new HashCodeBuilder().append(health).append(agility).append(attack).append(sparePoints).toHashCode();
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { 
			return true;
		}
		if (obj instanceof Stats) {
			Stats that = (Stats) obj;
			return (this.health == that.health) && (this.attack == that.attack) && (this.agility == that.agility)
					&& (this.sparePoints == that.sparePoints);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return hashCode;
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
