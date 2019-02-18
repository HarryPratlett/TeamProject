package com.myst.world.entities;

import java.awt.geom.Line2D;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;
import com.myst.rendering.Shader;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import org.joml.Vector3f;

public class Player extends Entity{

    private final float MOVEMENT_SPEED = 10f;

//    private static final float[] vertices =
//
//
//    private static final float[] textureFloats =
//
//    private static final int[] indices = ;

    public Player(){
        super(new float[]{
            -0.5f, 0.5f, 0f, /*0*/  0.5f, 0.5f, 0f, /*1*/    0.5f, -0.5f, 0f, /*2*/
                    -0.5f, -0.5f, 0f/*3*/
        },
                new float[] {
                0f, 0f,   1, 0f,  1f, 1f,
                0f, 1f
        },
        new int[] {
                0,1,2,
                2,3,0
        },
        new Vector2f(0.5f,0.5f), new Shader("project/assets/Shader"));
    }

    public void update(float deltaTime, Window window, Camera camera, World world) {
//        these needs fixing and entering into an entities class
//        the entities will then
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
            transform.pos.add(MOVEMENT_SPEED * deltaTime, 0, 0);
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
            transform.pos.x += -MOVEMENT_SPEED * deltaTime;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
            transform.pos.y += MOVEMENT_SPEED * deltaTime;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
            transform.pos.y += -MOVEMENT_SPEED * deltaTime;
        }
        if(window.getInput().isMouseButtonDown(0)) {
        	if(attack(world)) {
        		//add code to send hit confirmation to server
        	}
        }

        //now that the co-ordinate system has been redone this needs redoing
        this.boundingBox.getCentre().set(transform.pos.x , transform.pos.y );

        AABB[] boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
//              30 is the scale and 0.5f is half the width of the box and 2.5 is the scan width
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

	@Override
	public boolean attack(World world) {
		
		Line2D.Float bulletLine = new Line2D.Float(transform.pos.x, transform.pos.y, transform.pos.x + 100, transform.pos.y + 100);
		AABB[] line = new AABB[(int) transform.pos.x + 100];
		for (int i = 0; i < line.length; i++) {
			int x = (int) transform.pos.x + i;
			int y = (int) transform.pos.y + i;
			line[i] = world.getBoundingBox(x,y);
		}
		
		for(int i = 0; i< line.length; i++) {
			if(line[i] != null) {
				Collision collision = boundingBox.getCollision(line[i]);
				if(collision.isIntersecting) {
					return true;
				}
			}
		}
		return false;
	}
}
