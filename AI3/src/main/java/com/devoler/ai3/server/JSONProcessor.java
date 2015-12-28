package com.devoler.ai3.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface JSONProcessor {
	public JsonElement process(JsonObject input) throws Exception;
}
