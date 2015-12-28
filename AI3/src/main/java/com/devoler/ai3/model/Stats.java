package com.devoler.ai3.model;

import java.util.Random;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.JsonObject;

public final class Stats {
	public static final int HEALTH_POINTS = 15;

	private final int health;
	private final int agility;
	private final int attack;
	private final int sparePoints;
	
	private final int hashCode;
	
	public Stats(final int health, final int agility, final int attack, final int sparePoints) {
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
	
	public boolean ge(Stats that) {
		return (this.health >= that.health) && (this.attack >= that.attack) && (this.agility >= that.agility);
	}
	
	public boolean canMakeAgility7() {
		int neededPoints = 0;
		int ag = agility;
		while(ag < 7) {
			ag++;
			neededPoints += ag;
		}
		return sparePoints >= neededPoints;
	}
	
	public boolean canMakeAgility8() {
		int neededPoints = 0;
		int ag = agility;
		while(ag < 8) {
			ag++;
			neededPoints += ag;
		}
		return sparePoints >= neededPoints;
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
	
	public StatUpgrade upgrade(Stats oppStats) {
		if ((health == 1) && (agility == 1) && (attack == 1)) {
			return StatUpgrades.AGILITY;
		}
		// copy the other player
		if (oppStats.getAttack() > attack) {
			return StatUpgrades.ATTACK;
		}
		if (oppStats.getHealth() > health) {
			return StatUpgrades.HEALTH;
		}
		return StatUpgrades.AGILITY;
	}
	
	public StatUpgrade upgradeToTarget(Stats targetStats) {
		// copy the other player
		if (targetStats.getAttack() > attack) {
			return StatUpgrades.ATTACK;
		}
		if (targetStats.getHealth() > health) {
			return StatUpgrades.HEALTH;
		}
		return StatUpgrades.AGILITY;
	}
	
	public static Stats getRandomStats() {
		Stats result = new Stats(1, 1, 1, 35);
		Random random = new Random();
		while(true) {
			if ((result.sparePoints <= result.health) && (result.sparePoints <= result.attack) && (result.sparePoints <= result.agility)) {
				return result;
			}
			StatUpgrades upgrade = StatUpgrades.values()[random.nextInt(StatUpgrades.values().length)]; 
			switch(upgrade) {
			case AGILITY:
				if (result.sparePoints  > result.agility) {
					result = new Stats(result.health, result.agility + 1, result.attack, result.sparePoints - result.agility - 1);
				}
				break;
			case ATTACK:
				if (result.sparePoints  > result.attack) {
					result = new Stats(result.health, result.agility, result.attack + 1, result.sparePoints - result.attack - 1);
				}
				break;
			case HEALTH:
				if (result.sparePoints  > result.health) {
					result = new Stats(result.health + 1, result.agility, result.attack, result.sparePoints - result.health - 1);
				}
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("stats(h: %d, ag: %d, at: %d, pts: %d)", health, agility, attack, sparePoints);
	}
}
