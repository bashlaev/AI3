package com.devoler.ai3.server;

import com.google.gson.JsonObject;

public interface JSONProcessor {
	public JsonObject process(JsonObject input) throws Exception;
}
