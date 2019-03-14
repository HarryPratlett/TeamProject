package com.myst.world.entities;

import com.myst.audio.Audio;
import com.myst.rendering.Shader;
import com.myst.world.view.Camera;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import static com.myst.world.entities.EntityTypes.PLAYER;

/*
change character by pressing key v
charac==0--->survivor1
charac==1--->manBlue


change weapon by pressing key c
change==0--->gun
change==1--->machine

once change character,weapon will change to gun
*/
public class Player extends Entity{

    private final float MOVEMENT_SPEED = 5f;

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
        new Vector2f(0.5f,0.5f), new Shader("assets/Shader"));
        this.type = PLAYER;

    }

  //animation
    protected float xMove, yMove;
    //change weapon by pressing c
    public int change=0;
    protected int count=0;
    //change character by pressing v
    public int charac=0;
    protected int count1=0;
    
    public void changeweapon(Window window) {
    	//change weapon
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_C)) {
        	
        	count=count+1;//count times of pressing C
        	
        	//make sure when press the third time it will change to gun
        	if(count>1)
        	{count=count-2;}
        	
        	
        	if(count==1)
        		change=1;//change to machine
        	if(count==0)
        		change=0;//change to gun
        	
        	
        	//change the weapon as soon as press the C
        	
        	//need to identify current position
        	if(charac==0) {
        	
        		//up or down
        	 if(xMove==0) {
        		if(yMove==1)//up
        			{if(change==1)this.texture=this.texture6;
        			 if(change==0)this.texture=this.texture2;}
        		else {//down
        			 if(change==1)this.texture=this.texture9;
        			 if(change==0)this.texture=this.texture5;}
        	}
        	
        	 else//left or right
        	{
        		if(xMove==1)//right
        		    {if(change==1)this.texture=this.texture8;
        		     if(change==0)this.texture=this.texture4;}
        		else {//left
        			if(change==1)this.texture=this.texture7;
        			if(change==0)this.texture=this.texture3;
        		}
        	}
           }
        	
        	
        	if(charac==1) {
        		if(xMove==0) {
            		if(yMove==1)//up
            			{if(change==1)this.texture=this.texture14;
            			 if(change==0)this.texture=this.texture10;}
            		else {//down
            			if(change==1)this.texture=this.texture17;
            			if(change==0)this.texture=this.texture13;}
            	}
            	else//left or right
            	{
            		if(xMove==1)//right
            		    {if(change==1)this.texture=this.texture16;
            		     if(change==0)this.texture=this.texture12;}
            		else {//left
            		     if(change==1) this.texture=this.texture15;
            		     if(change==0)this.texture=this.texture11;
            		}
            	}
        	}
        }
    }
    
    public void changecharac(Window window) {
    	 //change character by pressing key V
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_V)) {
        	
        	
        	count1=count1+1;
        	//if keep pressing V,make sure after the last will be the first one
        	if(count1>1)
        	{count1=count1-2;}
        	
        	//when count1=1, change to manBlue
        	if(count1==1)
        		charac=1;
        	if(count1==0)
        		charac=0;
        	
        	//change character as soon as press key v
        	//need to specify current direction
        	//up or down
        	if(charac==1) {
        	if(xMove==0) {
        		if(yMove==1)//up
        			{this.texture=this.texture10;}
        		else {//down
        			this.texture=this.texture13;}
        	}
        	else//left or right
        	{
        		if(xMove==1)//right
        		{this.texture=this.texture12;}
        		else {//left
        		this.texture=this.texture11;}
        	}
        	}
        	
        	if(charac==0) {
        		if(xMove==0) {
            		if(yMove==1)//up
            			{this.texture=this.texture2;}
            		else {//down
            			this.texture=this.texture5;}
            	}
            	else//left or right
            	{
            		if(xMove==1)//right
            		{this.texture=this.texture4;}
            		else {//left
            		this.texture=this.texture3;}
            	}
        	}
        	
        	
        }
        
    }
    public void update(float deltaTime, Window window, Camera camera, World world) {
//        these needs fixing and entering into an entities class
//        the entities will then

        boolean moved = false;
        changeweapon(window);
        changecharac(window);
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
        	if(charac==0) {
       	     if(change==0) {
       		    //survivor1_gun
       	        this.texture=this.texture4;}
       	
       	     if(change==1) {
       		    //survivor1_machine
       	        this.texture=this.texture8;}     	
       	}
       	
       	if(charac==1) {
       		 if(change==0) {
        		    //manBlue_gun
        	        this.texture=this.texture12;}
        	
        	     if(change==1) {
        		    //manBlue_machine
        	        this.texture=this.texture16;} 
       	}
       	
       	transform.pos.add(MOVEMENT_SPEED * deltaTime, 0, 0); 
       	xMove=1;yMove=0;
       	moved = true;
        }
        
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
        	if(charac==0) {
     	       if(change==0) {
     		   //survivor1_gun
     	       this.texture = this.texture3;}
     	
     	       if(change==1) {
     		   //survivor1_machine
     	       this.texture=this.texture7;}
     	}
     	
     	if(charac==1) {
    		        if(change==0) {
     		    //manBlue_gun
     	        this.texture=this.texture11;}
     	
     	        if(change==1) {
     		    //manBlue_machine
     	        this.texture=this.texture15;} 
       	}
     	
         transform.pos.x += -MOVEMENT_SPEED * deltaTime;
         xMove=-1;yMove=0;
         moved = true;
        }
        
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {

        	if(charac==0) {
        	        if(change==0) {
        		    //survivor1_gun
        	        this.texture = this.texture2;}
        	
        	       if(change==1) {
        		   //survivor1_machine
        	       this.texture=this.texture6;}
        	}
        	
        	if(charac==1) {
              		 if(change==0) {
               		    //manBlue_gun
               	        this.texture=this.texture10;}
               	
               	     if(change==1) {
               		    //manBlue_machine
               	        this.texture=this.texture14;} 
              	}
        	       
        	
            transform.pos.y += MOVEMENT_SPEED * deltaTime;
            xMove=0;yMove=1;
            moved = true;
        }
        
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
        	if(charac==0) {
    	        if(change==0) {
    		       //survivor1_gun
    	           this.texture =this.texture5;}

    	        if(change==1) {
    		       //survivor1_machine
    	           this.texture=this.texture9;}
    		}
    		
    		if(charac==1) {
    			if(change==0) {
        		    //manBlue_gun
        	        this.texture=this.texture13;}
        	
        	     if(change==1) {
        		    //manBlue_machine
        	        this.texture=this.texture17;} 
    		}

        transform.pos.y += -MOVEMENT_SPEED * deltaTime;
        xMove=0;yMove=-1;
        moved = true;
        }

        
        

        
        
        
        
        
       
        if(moved)
            Audio.getAudio().play(Audio.FOOTSTEPS);
        else
            Audio.getAudio().stop(Audio.FOOTSTEPS);

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
