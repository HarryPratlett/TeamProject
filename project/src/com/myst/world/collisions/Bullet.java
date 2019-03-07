package com.myst.world.collisions;

import com.myst.rendering.Model;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.world.view.Transform;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Bullet {
    private static final float MAX_LENGTH = 20;
    private Texture texture;
    private Shader shader;
    private Transform transform;


//
    public Bullet(Line line) {
        float[] vertices = new float[]{
                -0.1f, 4f, 0f, /*0*/  0.1f, 4f, 0f, /*1*/    0.1f, 0.1f, 0f, /*2*/
                -0.1f, 0.1f, 0f/*3*/
        };


    }

    public f
//

//
//
//
//        float angle = vector.angle(new Vector2f(0,1));
//
//        Matrix3f rotationMatrix = new Matrix3f();
//
//        rotationMatrix.m00 = cos(-angle);
//        rotationMatrix.m01 = -sin(-angle);
//        rotationMatrix.m10 = sin(-angle);
//        rotationMatrix.m11 = cos(-angle);
//
//
//        float[] texture, int[] indices, Shader shader
//
//        model = new Model(vertices, texture, indices);
//        this.texture = new Texture("assets/survivor1_hold.png");
//
//        Shader shader = new Shader("assets/shader");
//        this.shader = shader;
//        transform = new Transform();
//    }
}
