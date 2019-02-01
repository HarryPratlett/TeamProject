package com.myst.GUI;

import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.map.rendering.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;


public class Main {

    public static void main(String[] args){
        Window.setCallbacks();
        if (!glfwInit()){
            throw new IllegalStateException("Failed to initialise GLFW");
        }

        Window window = new Window();

        window.setFullscreen(false);
        window.createWindow("My GUI");

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0f,0f,0f, 0f);

        Shader shader = new Shader("assets/shader");




        while (!window.shouldClose()){
            window.update();

            renderGUI(shader, new Matrix4f().scale(0.3f));
            window.swapBuffers();
            try {
                Thread.sleep(  100);
            }
            catch  (InterruptedException e) {
                System.exit(1);
            }
        }

    }


    public static void renderImage(Shader shader, Texture texture, float x, float y, Matrix4f scale, Model model){
        shader.bind();
        texture.bind(0);
        Matrix4f target = new Matrix4f();

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x,y,0));
        scale.mul(tile_pos, target);


        shader.setUniform("sampler",0);
        shader.setUniform("projection", target);
        model.render();


    }

    public static void renderGUI(Shader shader, Matrix4f scale) {

        final float[] baseVertices = new float[] {
                -1f, 0.5f, 0f, /*0*/  1f, 0.5f, 0f, /*1*/    1f, -0.5f, 0f, /*2*/
                -1f, -0.5f, 0f/*3*/
        };

        float[] textureDocks = new float[] {
                0f, 0f,   1, 0f,  1f, 1f,
                0f, 1f
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };


        Texture[] menuTextures = new Texture[]  {
                new Texture("assets/resume_button.png"), new Texture("assets/controls_button.png"),
                new Texture("assets/settings_button.png"), new Texture("assets/exit_button.png")
        };
        float y = 2.25f;
        for (Texture t : menuTextures)   {

            float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
            vertices[0] *= t.getWidth()*0.009;
            vertices[3] *= t.getWidth()*0.009;
            vertices[6] *= t.getWidth()*0.009;
            vertices[9] *= t.getWidth()*0.009;

            Model model = new Model(vertices, textureDocks, indices);
            renderImage(shader, t, 0f, y, scale, model);

            addButton(0, y, t); //needs changing to be positions of button
            y += (-1.5f);
        }


    }

    public static Rectangle2D.Float addButton(float x, float y, Texture texture) {
        Rectangle2D.Float bounds = new Rectangle2D.Float(x, y, texture.getWidth(), texture.getHeight());
        return bounds;
    }


}