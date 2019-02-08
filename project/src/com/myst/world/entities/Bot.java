package com.myst.world.entities;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.myst.AI.AI;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.map.rendering.Shader;
import com.myst.world.view.Camera;

public class Bot extends Entity {
	
	private AI intelligence;
	private Vector2f position;
	private final float MOVEMENT_SPEED = 10f;
	
	public Bot(float[] vertices, float[] texture, int[] indices, Vector2f boundingBoxCoords, Shader shader) {
		super(vertices, texture, indices, boundingBoxCoords, shader);
		entityType = Type.Player;
		position = boundingBoxCoords;
	}

	public void initialiseAI(World world) {
		intelligence = new AI(position, world);
	}
	
	@Override
	public void update(float deltaTime, Window window, Camera camera, World world) {
		while(followPath(deltaTime)) {
			//add enemy detection method here, so constantly checks for enemies as well as randomly turning on flashlight.
		}
		followPath(deltaTime);

	}
	
	public boolean followPath(float deltaTime) {
		ArrayList<Vector2f> path = intelligence.pathFind(position);
		for(Vector2f point : path) {
			while(!position.equals(point)) {
				if(this.position.x > point.x && this.position.y > point.y) {
					transform.pos.add(-MOVEMENT_SPEED * deltaTime, -MOVEMENT_SPEED * deltaTime, 0);
				}
				else if(this.position.x > point.x && this.position.y < point.y) {
					transform.pos.add(-MOVEMENT_SPEED * deltaTime, MOVEMENT_SPEED * deltaTime, 0);
				}
				else if(this.position.x < point.x && this.position.y > point.y) {
					transform.pos.add(MOVEMENT_SPEED * deltaTime, -MOVEMENT_SPEED * deltaTime, 0);
				}
				else if(this.position.x < point.x && this.position.y < point.y) {
					transform.pos.add(MOVEMENT_SPEED * deltaTime, MOVEMENT_SPEED * deltaTime, 0);
				}
				else if(this.position.x > point.x) {
					transform.pos.x += -MOVEMENT_SPEED * deltaTime;
				}
				else if(this.position.x < point.x) {
					transform.pos.x += MOVEMENT_SPEED * deltaTime;
				}
				else if (this.position.y > point.y) {
					transform.pos.y += -MOVEMENT_SPEED * deltaTime;
				}
				else if(this.position.y < point.y) {
					transform.pos.y += MOVEMENT_SPEED * deltaTime;
				}
				return true;
			}
			position = point;
		}
		return false;
	}

}
