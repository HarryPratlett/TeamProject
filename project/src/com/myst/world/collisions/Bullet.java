package com.myst.world.collisions;

import org.joml.Vector2f;

public class Bullet {

	private static final float MAX_LENGTH = 20;
	private Vector2f vector;
	private Vector2f position;
	
	public Bullet(Vector2f vector, Vector2f position) {
		this.vector = vector;
		this.position = position;
	}
	
}
