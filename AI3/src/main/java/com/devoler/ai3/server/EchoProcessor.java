package com.devoler.ai3.server;

import com.google.gson.JsonObject;

public final class EchoProcessor implements JSONProcessor {
	@Override
	public JsonObject process(JsonObject input) throws Exception {
		JsonObject result = new JsonObject();
		result.addProperty("echo", input.toString());
		return result;
	}
}
