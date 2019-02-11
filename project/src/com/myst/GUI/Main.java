package com.myst.GUI;

import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.map.rendering.Shader;
import com.myst.input.Input;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;


public class Main {

    static List<Rectangle2D.Float> buttons = new ArrayList<>();

    public static void main(String[] args){
        Window.setCallbacks();
        if (!glfwInit()){
            throw new IllegalStateException("Failed to initialise GLFW");
        }

        Window window = new Window();



        window.setFullscreen(false);
        window.createWindow("My GUI");
        glfwSetCursorPosCallback(window.getWindow(), window.getInput());


        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0f,0f,0f, 0f);

        Shader shader = new Shader("assets/shader");




        while (!window.shouldClose()){
            window.update();

            renderGUI(shader, new Matrix4f().scale(0.3f));
            checkButtons(window);
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

            addButton(0 -(t.getWidth()*0.009f)/2, y+(t.getHeight()*0.009f)/2, t); //needs changing to be positions of button
            y += (-1.5f);
        }


    }

    public static void addButton(float x, float y, Texture texture) {

        Rectangle2D.Float bounds = new Rectangle2D.Float(x, y, (texture.getWidth()*0.009f), (texture.getHeight()*0.009f));
        buttons.add(bounds);
    }

    public static void checkButtons(Window window)   {
        for (Rectangle2D.Float b : buttons)   {
            double mouseX = ((window.getInput().getMouseCoordinates()[0])/window.getWidth()/2)-0.25;
            double mouseY = ((window.getInput().getMouseCoordinates()[1])/window.getHeight()/2)-1;
            if (mouseX >= b.getX() && mouseX <= (b.getX()+b.getWidth()) && mouseY <= b.getY() && mouseY >= (b.getY()-b.getHeight()))  {
                if (window.getInput().isMouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                    System.out.println("mouse X : " + mouseX + "button x and x+width: " + b.getX() + " " + (b.getX()+b.getWidth()) + "mousey: " +  mouseY + "bY and bY-height: " + b.getY() + " " + (b.getY()-b.getHeight()));
                    System.exit(1);
                }
            }
        }
    }
//need to fix scaling with regards to mouse pointer maybe replace with quad

}