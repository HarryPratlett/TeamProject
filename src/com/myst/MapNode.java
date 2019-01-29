package com.myst;

import java.util.Optional;

import org.joml.Vector2f;

public class MapNode implements Comparable<MapNode> {

	private Vector2f position;
	private Vector2f goal;
	private Optional<MapNode> parent;
	
	private int f;
	private int g;
	private int h;
	
	public MapNode(Vector2f position, Vector2f goal, MapNode parent) {
		this.position = position;
		this.goal = goal;
		this.parent = Optional.ofNullable(parent);
		
		g = this.parent.isPresent() ? parent.g + 1 : 0;
		h = euclideanDistance();
		f = g + h;
	}
	
	public int getF() {
		return f;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Optional<MapNode> getParent(){
		return parent;
	}
	
	private int euclideanDistance() {
		int x = (int)Math.abs(position.x - goal.x);
		int y = (int)Math.abs(position.y - goal.y);
		return x + y;
	}
	
	@Override
	public int compareTo(MapNode node) {
		return f - node.getF();
	}

}
