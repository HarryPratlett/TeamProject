package com.myst.AI;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.myst.world.World;

public class AI {

	private Vector2f position;
	private World world;
	
	public AI(Vector2f position, World world) {
		this.position = position;
		this.world = world;
	}
	
	public ArrayList<Vector2f> pathFind(Vector2f goal) {
		AStarSearch search = new AStarSearch(position, goal, world);
		return search.getPath();
	}
	
	public void shootEnemy() {
		
	}

}