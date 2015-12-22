package com.devoler.ai3.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class JSONServer {
	private static Logger logger = LoggerFactory.getLogger(JSONServer.class);
	
	public JSONServer(final JSONProcessor processor) throws IOException{
		HttpServer server = HttpServer.create(new InetSocketAddress(8181), 0);
		HttpHandler handler = new HttpHandler() {			
			public void handle(HttpExchange exchange) throws IOException {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				try (InputStream is = exchange.getRequestBody()) {
					int nRead;
					byte[] data = new byte[256];
					while ((nRead = is.read(data, 0, data.length)) != -1) {
						buffer.write(data, 0, nRead);
					}
					buffer.flush();
				}
				String in = new String(buffer.toByteArray());
				logger.info("Received: \"{}\"", in);
				
				try {
					JsonObject inJson = new JsonParser().parse(in).getAsJsonObject();
					logger.info("Parsed: \"{}\"", inJson);
					JsonObject outJson = processor.process(inJson);
					logger.info("Processed: \"{}\"", outJson);
					byte[] outContent = outJson.toString().getBytes();

					exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
					exchange.getResponseHeaders().add("Access-Control-Allow-Headers",
							"Accept, X-Access-Token, X-Application-Name, X-Request-Sent-Time");
					exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
					exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
					exchange.sendResponseHeaders(200, outContent.length);
					logger.debug("Headers sent");
					exchange.getResponseBody().write(outContent);
					exchange.getResponseBody().close();
					logger.info("Response sent");
				} catch (Exception exc) {
					logger.warn("Could not handle request", exc);
				}
			}
		};
		server.createContext("/", handler);
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
	}

	public static void main(String[] args) throws Exception {
		new JSONServer(new EchoProcessor());
	}

}
