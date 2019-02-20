package com.myst.AI;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import com.myst.world.view.Transform;

public class AI {

	private World world;
	
	public AI(World world) {
		this.world = world;
	}
	
	public ArrayList<Vector2f> pathFind(Vector2f position, Vector2f goal) {
		AStarSearch search = new AStarSearch(position, goal, world);
		System.out.print(search.getPath().toString());
		return search.getPath();
	}
	
	public boolean enemyDetection(Transform transform, AABB boundingBox) {
		AABB[] boxes = new AABB[200];
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
//              30 is the scale and 0.5f is half the width of the box and 2.5 is the scan width
                int x = (int) (transform.pos.x + (0.5f - 2.5 + i));
                int y = (int) (-transform.pos.y + (0.5f - 2.5 + j));
                boxes[i + (j * 5)] = new AABB(new Vector2f(x,-y),new Vector2f(0.5f,0.5f));
            }
        }
        
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                Collision data = boxes[i].getCollision(boundingBox);
                if (data.isIntersecting) {
                    return true;
                }
            }
        }
        return false;
	}
}