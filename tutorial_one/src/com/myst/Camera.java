package com.myst;

import org.joml.Matrix4f;
import org.joml.Vector3f;


//as a note the co-ordinate system is flipped so left is negative and right is positive
public class Camera {
    public Vector3f position;
    public Matrix4f projection;
    private int width;
    private int height;

    public Camera(int width, int height){
        position = new Vector3f(0,0,0);
        projection = new Matrix4f().setOrtho2D(-width/2, width/2,-height / 2, height / 2);
        this.width = width;
        this.height = height;
    }

    public void setPosition(Vector3f position){
        this.position = position;

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

        target = projection.mul(pos,target);
        return target;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
