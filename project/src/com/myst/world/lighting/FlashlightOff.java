package com.myst.world.lighting;

import com.myst.rendering.Model;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class FlashlightOff {
    private Shader shader;
    private int darkness = 88;
    private Matrix4f darknessMat;
    private Model model;
    private Texture texture;


    public FlashlightOff(Window window){
        shader = new Shader("assets/shader");
        Matrix4f projection = new Matrix4f().setOrtho2D(-window.getWidth()/2, window.getWidth()/2,-window.getHeight() / 2, window.getHeight() / 2);

        darknessMat = new Matrix4f();

        projection.scale(darkness, darknessMat);

        float[] vertices = new float[]{
                -4f, 3f, 0f, /*0*/  4f, 3f, 0f, /*1*/    4f, -3f, 0f, /*2*/
                -4f, -3f, 0f/*3*/
        };

        float[] textureDocks = new float[] {
                0f, 0f,   1, 0f,  1f, 1f,
                0f, 1f
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };
        this.model = new Model(vertices,textureDocks,indices);
        this.texture = new Texture("assets/flashlightOff.png");

    }

    public void render(){
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", darknessMat);
        texture.bind(0);
        model.render();
    }
}