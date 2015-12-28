package com.devoler.ai3.model;

import com.google.gson.JsonObject;

public enum StatUpgrades implements StatUpgrade {
	HEALTH, AGILITY, ATTACK;
	
	@Override
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		result.addProperty("stat", name());
		return result;
	}
}
