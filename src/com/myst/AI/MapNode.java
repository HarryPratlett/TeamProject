package com.myst.AI;

import java.util.Optional;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.myst.datatypes.TileCoords;
import com.myst.datatypes.WorldCoords;
import com.myst.world.World;

public class MapNode implements Comparable<MapNode> {

	private Vector3f position;
	private Vector3f goal;
	private Optional<MapNode> parent;
	private World world;
	
	private int f;
	private int g;
	private int h;
	
	public MapNode(Vector3f position, Vector3f goal, MapNode parent, World world) {
		this.position = position;
		this.goal = goal;
		this.parent = Optional.ofNullable(parent);
		this.world = world;
		
		g = this.parent.isPresent() ? parent.g + 1 : 0;
		h = euclideanDistance();
		f = g + h;
	}
	
	public int getF() {
		return f;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Optional<MapNode> getParent(){
		return parent;
	}
	
	private int euclideanDistance() {
		TileCoords coords = new TileCoords((int)position.x,(int)-position.y);
		if(world.getTile(coords) == null) {
			return Integer.MAX_VALUE;
		}
		
		else if (world.getTile(coords).isSolid()) {
			return Integer.MAX_VALUE;
		}
		
		else {
			int x = (int)Math.abs(position.x - goal.x);
			int y = (int)Math.abs((-position.y) - (-goal.y));
			return x + y;
		}
	}
	
	@Override
	public int compareTo(MapNode node) {
		return f - node.getF();
	}

}
