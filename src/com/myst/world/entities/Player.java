package com.myst.world.entities;
import com.myst.animation.*;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.world.view.Camera;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
//import com.myst.animation.Assets;
import static com.myst.world.entities.EntityTypes.PLAYER;

import java.awt.image.BufferedImage;

import java.awt.Graphics;
public class Player extends Entity{

	

	//Animations
		private animation animDown, animUp, animLeft, animRight;
		
    private final float MOVEMENT_SPEED = 10f;
    
    public Player(){

        super(new float[]{
            -0.5f, 0.5f, 0f, /*0*/  0.5f, 0.5f, 0f, /*1*/    0.5f, -0.5f, 0f, /*2*/
                    -0.5f, -0.5f, 0f/*3*/
        },
                new float[] {
                0f, 0f,   1, 0f,  1f, 1f,
                0f, 1f
        },
                new float[] {
                        0f, 0f,   1, 0f,  1f, 1f,
                        0f, 1f
                },     new float[] {
                        0f, 0f,   1, 0f,  1f, 1f,
                        0f, 1f
                },     new float[] {
                        0f, 0f,   1, 0f,  1f, 1f,
                        0f, 1f
                }, new float[] {
                        0f, 0f,   1, 0f,  1f, 1f,
                        0f, 1f
                }, new float[] {
                        0f, 0f,   1, 0f,  1f, 1f,
                        0f, 1f
                },new int[] {
                0,1,2,
                2,3,0
        },
        

        new Vector2f(0.5f,0.5f), new Shader("assets/Shader"));
        this.type = PLAYER;
    	
    	//animation
    			animDown = new animation(500,Assets.p1_down);
    			animUp = new animation(500,Assets.p1_up);
    			animLeft = new animation(500,Assets.p1_left);
    			animRight = new animation(500,Assets.p1_right);
    			

    }
    
    //animation
    protected float xMove, yMove;
    
   
    @Override
	public void tick() {
		//Animations
		animDown.tick();
		animUp.tick();
		animRight.tick();
		animLeft.tick();
		//Movement
		//getInput(Window);
		//move();
		//handler.getGameCamera().centerOnEntity(this);
		// Attack
		//checkAttacks();
		// Inventory
		//inventory.tick();
	}
    
    public void getInput(Window window){
		xMove = 0;
		yMove = 0;

		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W))
			yMove = -MOVEMENT_SPEED;
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S))
			yMove = MOVEMENT_SPEED;
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D))
			xMove = -MOVEMENT_SPEED;
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A))
			xMove = MOVEMENT_SPEED;
	}
    private BufferedImage getCurrentAnimationFrame(){
		if(xMove < 0){
			return animLeft.getCurrentFrame();
		}else if(xMove > 0){
			return animRight.getCurrentFrame();
		}else if(yMove < 0){
			return animUp.getCurrentFrame();
		}else{
			return animDown.getCurrentFrame();
		}
	}
    
    public void render(Graphics g) {
		g.drawImage(getCurrentAnimationFrame()
		, (int)transform.pos.x, (int)transform.pos.y, 
		32, 32, null
		);
	}
    
   // , (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), 
   
    
    
    public void update(float deltaTime, Window window, Camera camera, World world) {

        //right
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {      
        	transform.pos.add(MOVEMENT_SPEED * deltaTime, 0, 0);
        	//this.texture=this.texture2;
        	xMove=1;yMove=0;
        	
        }
        //left
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
            //this.texture = this.texture2;
            transform.pos.x += -MOVEMENT_SPEED * deltaTime;
            xMove=-1;yMove=0;
        }
        //up
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
        	//this.texture = this.texture4;
            transform.pos.y += MOVEMENT_SPEED * deltaTime;
            xMove=0;yMove=1;
        }
        //down
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
        	//this.texture =this.texture2 ;
            transform.pos.y += -MOVEMENT_SPEED * deltaTime;
            xMove=0;yMove=-1;
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

 

}
