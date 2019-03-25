package com.myst.world.entities;

import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.myst.AI.AI;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import com.myst.world.collisions.Line;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;

public class Bot extends Entity {
	
	private AI intelligence;
	private final float MOVEMENT_SPEED = 1f;
	private ArrayList<Vector3f> path;
	private ArrayList<Line> bullets;
	private Random randInt = null;
	
	public Bot(float[] vertices, float[] texture, int[] indices, Vector2f boundingBoxCoords, ArrayList<Line> bullets) {
		super(vertices, texture, indices, boundingBoxCoords);
		type = EntityTypes.BOT;
        this.visibleToEnemy = true;
        this.bullets = bullets;
	}

	public void initialiseAI(World world) {
		intelligence = new AI(transform, world);
	}
	
	@Override
	public void update(float deltaTime, Window window, Camera camera, World world) {
		//add enemy detection method here, so constantly checks for enemies as well as randomly turning on flashlight.
		intelligence.updateTransform(transform);
		Transform enemyTransform = intelligence.enemyDetection(entities, visibleToEnemy);
		if (enemyTransform!=null){
            Line line = new Line(new Vector2f(transform.pos.x, -transform.pos.y), new Vector2f((float) enemyTransform.pos.x,(float) enemyTransform.pos.y));
            bullets.add(line);
            System.out.println("Enemy Detected");
        }
		followPath(deltaTime);
		this.boundingBox.getCentre().set(transform.pos.x , transform.pos.y );
		int random = randInt.nextInt(3000);
		
		if(random == 1500){
			if(this.lightDistance == 0.25f){
				this.lightDistance = 2.5f;
				this.visibleToEnemy = true;
			}else{
				this.lightDistance = 0.25f;
				this.visibleToEnemy = false;
	        }
	    }
		
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
			Vector3f point = new Vector3f();
			if(path.size() != 0) {
				point = path.get(0);
			}else {
				return;
			}
			if((int)this.transform.pos.x > point.x && (int)this.transform.pos.y > point.y) {
				transform.pos.add(-MOVEMENT_SPEED * deltaTime, -MOVEMENT_SPEED * deltaTime, 0);
			}
			else if((int)this.transform.pos.x > point.x && (int)this.transform.pos.y < point.y) {
				transform.pos.add(-MOVEMENT_SPEED * deltaTime, MOVEMENT_SPEED * deltaTime, 0);
			}
			else if((int)this.transform.pos.x < point.x && (int)this.transform.pos.y > point.y) {
				transform.pos.add(MOVEMENT_SPEED * deltaTime, -MOVEMENT_SPEED * deltaTime, 0);
			}
			else if((int)this.transform.pos.x < point.x && (int)this.transform.pos.y < point.y) {
				transform.pos.add(MOVEMENT_SPEED * deltaTime, MOVEMENT_SPEED * deltaTime, 0);
			}
			else if((int)this.transform.pos.x > point.x) {
				transform.pos.x += -MOVEMENT_SPEED * deltaTime;
			}
			else if((int)this.transform.pos.x < point.x) {
				transform.pos.x += MOVEMENT_SPEED * deltaTime;
			}
			else if ((int)this.transform.pos.y > point.y) {
					transform.pos.y += -MOVEMENT_SPEED * deltaTime;
			}
			else if((int)this.transform.pos.y < point.y) {
					transform.pos.y += MOVEMENT_SPEED * deltaTime;
			}
			else if((int)this.transform.pos.x == point.x && (int)this.transform.pos.y == point.y) {
				path.remove(0);
			}
	}
	
	public void setPath(Vector3f goal) {
		intelligence.updateTransform(transform);
		path = intelligence.pathFind(goal);
		System.out.print(path);
	}
	
	public ArrayList<Vector3f> getPath(){
		return path;
	}

}