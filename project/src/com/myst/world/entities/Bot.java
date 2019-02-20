package com.myst.world.entities;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.myst.AI.AI;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import com.myst.rendering.Shader;
import com.myst.world.view.Camera;

public class Bot extends Entity {
	
	private AI intelligence;
	private Vector2f position;
	private final float MOVEMENT_SPEED = 10f;
	private ArrayList<Vector2f> path;
	
	public Bot(float[] vertices, float[] texture, int[] indices, Vector2f boundingBoxCoords, Shader shader) {
		super(vertices, texture, indices, boundingBoxCoords, shader);
		type = EntityTypes.BOT;
		position = new Vector2f(transform.pos.x, transform.pos.y);
	}

	public void initialiseAI(World world) {
		intelligence = new AI(position, world);
	}
	
	@Override
	public void update(float deltaTime, Window window, Camera camera, World world) {
		//add enemy detection method here, so constantly checks for enemies as well as randomly turning on flashlight.
		this.boundingBox.getCentre().set(transform.pos.x , transform.pos.y );
			
	    AABB[] boxes = new AABB[25];
	    for (int i = 0; i < 5; i++) {
	         for (int j = 0; j < 5; j++) {
//	           30 is the scale and 0.5f is half the width of the box and 2.5 is the scan width
	            int x = (int) (transform.pos.x + (0.5f - 2.5 + i));
	            int y = (int) (-transform.pos.y + (0.5f - 2.5 + j));
	            boxes[i + (j * 5)] = world.getBoundingBox(x, y);
	        }
	    }



	   for (int i = 0; i < boxes.length; i++) {
		   if (boxes[i] != null) {
	            Collision data = boundingBox.getCollision(boxes[i]);
	            if (data.isIntersecting) {
	                boundingBox.correctPosition(boxes[i], data);
	                transform.pos.set(boundingBox.getCentre(), 0);
	                boundingBox.getCentre().set(transform.pos.x, transform.pos.y);
	            }
	        }
	    }
	}
	
	public void followPath(float deltaTime) {
		while(!path.isEmpty()) {
			Vector2f point = path.get(0);
		
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
			else if(this.position.x == point.x && this.position.y == point.y) {
				path.remove(0);
			}
			position = point;
		}
	}
	
	public void setPath(Vector2f goal) {
		path = intelligence.pathFind(goal);
		System.out.print(path);
	}
	
	public ArrayList<Vector2f> getPath(){
		return path;
	}

	@Override
	public boolean attack(World world) {
		return isKillable();
		// TODO Auto-generated method stub
		
	}

}
