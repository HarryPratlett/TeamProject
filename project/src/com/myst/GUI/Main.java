package com.myst.GUI;

import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.map.rendering.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import java.awt.Rectangle;

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


        float[] vertices = new float[] {
                -0.5f, 0.5f, 0f, /*0*/  0.5f, 0.5f, 0f, /*1*/    0.5f, -0.5f, 0f, /*2*/
                -0.5f, -0.5f, 0f/*3*/
        };

        float[] textureDocks = new float[] {
                0f, 0f,   1, 0f,  1f, 1f,
                0f, 1f
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };


        Shader shader = new Shader("assets/shader");
        Model model = new Model(vertices, textureDocks, indices);


        while (!window.shouldClose()){
            window.update();

            renderGUI(model, shader, new Matrix4f().scale(0.3f));
            window.swapBuffers();
        }

    }


    public static void renderImage(Shader shader, Texture texture, int x, int y, Matrix4f scale, Model model){
        shader.bind();
        texture.bind(0);
        Matrix4f target = new Matrix4f();

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x,y,0));
        scale.mul(tile_pos, target);


        shader.setUniform("sampler",0);
        shader.setUniform("projection", target);
        model.render();


    }

    public static void renderGUI(Model model, Shader shader, Matrix4f scale) {
        Texture[] menuTextures = new Texture[]  {
                new Texture("assets/resume_button.png"), new Texture("assets/controls_button.png"),
                new Texture("assets/settings_button.png"), new Texture("assets/exit_button.png")
        };
        int y = 2;
        for (Texture t : menuTextures)   {
            renderImage(shader, t, 0, y, scale, model);
            //addButton(x, y, t); //needs changing to be positions of button
            y -= 1;
        }


    }

    public static Rectangle addButton(int x, int y, Texture texture) {
        Rectangle bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        return bounds;
    }


}
