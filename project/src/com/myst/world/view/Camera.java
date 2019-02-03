package com.myst.world.view;

import com.myst.world.entities.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;


//as a note the co-ordinate system is flipped so left is negative and right is positive
public class Camera {
    public Vector3f position;
    public Matrix4f projection;
    private int width;
    private int height;
    private Player player;
    public int scale = 20;

    public Camera(int width, int height){
        position = new Vector3f(0,0,1);
        projection = new Matrix4f().setOrtho2D(-width/2, width/2,-height / 2, height / 2);
        this.width = width;
        this.height = height;
}

    public void setPosition(Vector3f position){
        this.position = position;
    }


    public void bindPlayer(Player player){
        this.player = player;
    }

    public void unBindPlayer(){
        this.player = null;
    }


//    now that the co-ordinate system has been redone so must this
    public void updatePosition(){
        if(this.player != null){
            this.setPosition(new Vector3f(-this.player.transform.pos.x * this.scale, -this.player.transform.pos.y * this.scale, 1));
        }
    }


    public void addPosition(Vector3f position){
        this.position.add(position);
    }

    public Vector3f getPosition(){
        return position;
    }


    public Matrix4f getProjection(){
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().setTranslation(position);

        Matrix4f scale = new Matrix4f().scale(this.scale);

        target = projection.mul(pos,target);
        target.mul(scale);
        return target;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
