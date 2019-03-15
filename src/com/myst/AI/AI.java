package com.myst.AI;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.myst.world.World;
import com.myst.world.view.Transform;

public class AI {

	private Transform transform;
	private World world;
	
	public AI(Transform transform, World world) {
		this.transform = transform;
		this.world = world;
	}
	
	public ArrayList<Vector3f> pathFind(Vector3f goal) {
		AStarSearch search = new AStarSearch(new Vector3f(transform.pos.x, transform.pos.y, transform.pos.z), goal, world);
		return search.getPath();
	}
	
	public boolean enemyDetection() {
		return false;
		
	}
	
	public void updateTransform(Transform transform) {
		this.transform = transform;
	}

}