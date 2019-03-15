package com.myst.world.view;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.Serializable;

public class Transform implements Serializable {
    public Vector3f pos;
    public Vector3f scale;
    public float rotation;

    public Transform(){
        pos = new Vector3f();
        scale = new Vector3f(1,1,1);
        rotation = 0f;
    }

    public Matrix4f getProjection(Matrix4f target){
        target.translate(pos);
        target.scale(scale);
        target.rotate(rotation, new Vector3f(0,0,1));
        return target;
    }

}
