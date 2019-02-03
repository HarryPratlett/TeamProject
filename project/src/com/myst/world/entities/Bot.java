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
		// TODO Auto-generated method stub

	}
	
	public void followPath() {
		ArrayList<Vector2f> path = intelligence.pathFind(position);
		for(Vector2f point : path) {
			
			position = point;
		}
	}

}
