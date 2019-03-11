package com.myst.world.entities;

import java.awt.geom.Line2D;
import java.util.HashMap;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import com.myst.audio.Audio;
import com.myst.rendering.Shader;
import com.myst.world.collisions.Bullet;
import com.myst.world.collisions.Line;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import org.joml.Vector3f;


import java.util.ArrayList;

import static com.myst.world.entities.EntityTypes.PLAYER;

public class Player extends Entity{
    private ArrayList<Line> bullets;
    private final float MOVEMENT_SPEED = 10f;

//    private static final float[] vertices =
//
//
//    private static final float[] textureFloats =
//
//    private static final int[] indices = ;
<<<<<<< HEAD:src/com/myst/world/entities/Player.java
    
    public Player(){
=======

    public Player(ArrayList<Line> bullets){
>>>>>>> networking:project/src/com/myst/world/entities/Player.java
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
        new Vector2f(0.5f,0.5f));
        this.type = PLAYER;
        this.visibleToEnemy = true;
        this.bullets = bullets;
    }

    public void update(float deltaTime, Window window, Camera camera, World world) {
//        these needs fixing and entering into an entities class
//        the entities will then

<<<<<<< HEAD:src/com/myst/world/entities/Player.java
        boolean moved = false;
=======

        double xMouse = ((window.getInput().getMouseCoordinates()[0] * 2) / window.getWidth()) - 1;
        double yMouse = -(((window.getInput().getMouseCoordinates()[1] * 2) / window.getHeight()) - 1);

        transform.rotation = (float) Math.atan(yMouse / xMouse);

        if (xMouse < 0){
            transform.rotation += Math.PI;
        }


>>>>>>> networking:project/src/com/myst/world/entities/Player.java
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
            transform.pos.add(MOVEMENT_SPEED * deltaTime, 0, 0);
            moved = true;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
            transform.pos.x += -MOVEMENT_SPEED * deltaTime;
            moved = true;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
            transform.pos.y += MOVEMENT_SPEED * deltaTime;
            moved = true;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
            transform.pos.y += -MOVEMENT_SPEED * deltaTime;
            moved = true;
        }
        if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_F)){
            if(this.lightDistance == 0.25f){
                this.lightDistance = 2.5f;
                this.visibleToEnemy = true;
            }else{
                this.lightDistance = 0.25f;
                this.visibleToEnemy = false;
            }
        }

<<<<<<< HEAD:src/com/myst/world/entities/Player.java
        if(moved)
            Audio.getAudio().play(Audio.FOOTSTEPS);
        else
            Audio.getAudio().stop(Audio.FOOTSTEPS);
=======
        if (window.getInput().isMousePressed(GLFW.GLFW_MOUSE_BUTTON_1)){
            Line line = new Line(new Vector2f(transform.pos.x, -transform.pos.y), new Vector2f((float) xMouse,(float) -yMouse));
            bullets.add(line);
            System.out.println("mouse pressed");
        }


>>>>>>> networking:project/src/com/myst/world/entities/Player.java
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
                for(int j = 0; j < entities.keySet().size(); j++) {
            		Collision data = boundingBox.getCollision(boxes[i]);
            		if (data.isIntersecting) {
            			boundingBox.correctPosition(boxes[i], data);
            			transform.pos.set(boundingBox.getCentre(), 0);
            			boundingBox.getCentre().set(transform.pos.x, transform.pos.y);
            		}
                }
            }
        }
    }

	@Override
	public boolean attack(World world, int entityID) {
		//need to add orientation changes i.e. if player is facing towards negative, make line along negative axis.
		AABB[] line = new AABB[100];
		for (int i = 0; i < line.length; i++) {
			int x = (int) transform.pos.x + i;
			int y = (int) -transform.pos.y + i;
			line[i] = new AABB(new Vector2f(x,-y),new Vector2f(0f,0f));
		}
		
		for(int i = 0; i< line.length; i++) {
			if(line[i] != null) {
				Collision collision = entities.get(entityID).boundingBox.getCollision(line[i]);
				if(collision.isIntersecting) {
					return true;
				}
			}
		}
		return false;
	}
}
