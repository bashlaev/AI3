package com.devoler.ai3.model;

import com.devoler.ai3.model.Move.Direction;
import com.google.gson.JsonObject;

public final class Position {
	private final int x;
	private final int y;
	
	Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public static Position fromJson(JsonObject json) {
		int x = json.get("x").getAsInt();
		int y = json.get("y").getAsInt();
		return new Position(x, y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Position move(Direction direction, int width, int height) {
		switch(direction) {
		case DOWN:
			return new Position(x, Math.min(height - 1, y + 1));
		case UP:
			return new Position(x, Math.max(0, y - 1));
		case RIGHT:
			return new Position(Math.min(width - 1, x + 1), y);
		case LEFT:
			return new Position(Math.max(0, x - 1), y);
		}
		return this;
	}
	
	public int getManhattanDistance(Position that) {
		return Math.abs(x - that.x) + Math.abs(y - that.y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position) {
			Position that = (Position) obj;
			return (this.x == that.x) && (this.y == that.y);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 31*x + y;
	}
	
	@Override
	public String toString() {
		return String.format("[%d, %d]", x, y);
	}
}
