package com.myst.AI;

import java.util.ArrayList;
import java.util.Optional;
import java.util.PriorityQueue;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.myst.datatypes.TileCoords;
import com.myst.datatypes.WorldCoords;
import com.myst.world.World;

public class AStarSearch {
	
	private World world;
	private PriorityQueue<MapNode> openList = new PriorityQueue<MapNode>();
	private ArrayList<MapNode> closedList = new ArrayList<MapNode>();
	private Vector3f position;
	private Vector3f goal;
	
	public AStarSearch(Vector3f position, Vector3f goal, World world) {
		this.position = position;
		this.goal = goal;
		this.world = world;
	}
	
	public ArrayList<Vector3f> getPath(){
		ArrayList<Vector3f> routeAsCoords = new ArrayList<Vector3f>();
		ArrayList<MapNode> route = search();
		for(MapNode node : route) {
			routeAsCoords.add(node.getPosition());
			
		}
		return routeAsCoords;
	}
	
	private ArrayList<MapNode> search(){
		MapNode start = new MapNode(position, goal, null, world);
		openList.add(start);
		while(!openList.isEmpty()) {
			MapNode searchNode = openList.poll();
			if(searchNode.getPosition().equals(goal)) {
				return getRoute(searchNode);
			}
			ArrayList<MapNode> children = generateChildren(searchNode);
	
			
			for(MapNode child:children) {
				if(!closedList.contains(child)) {
					if(child.getF() != Integer.MAX_VALUE) {
						openList.add(child);
					}
				}
			}
			closedList.add(searchNode);
		}
		return null;
	}
	
	private ArrayList<MapNode> generateChildren(MapNode parent){
		ArrayList<MapNode> children = new ArrayList<MapNode>();
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				Vector3f childPosition = new Vector3f(parent.getPosition().x + x, parent.getPosition().y + y, 0);
				if(world.getTile(new TileCoords((int)childPosition.x,(int)-childPosition.y)) != null && world.getTile(new TileCoords((int)childPosition.x,(int)-childPosition.y)).isSolid() == false) {
					children.add(new MapNode(childPosition, goal, parent, world));
					System.out.println(childPosition.toString());
				}
			}
		}
		return children;
	}
	
	public ArrayList<MapNode> getRoute(MapNode node){
		ArrayList<MapNode> route = new ArrayList<MapNode>();
		Optional<MapNode> parent = node.getParent();
		if(parent.isPresent()) {
			route.addAll(getRoute(parent.get()));
		}
		route.add(node);
		return route;
	}

}
