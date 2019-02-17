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
import java.util.HashMap;
import java.util.List;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;


public class Main {

    HashMap<Rectangle2D.Float, String> buttons = new HashMap<>();

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

    Boolean controls_accessed = false;

    public static void main(String[] args){
        Main GUI = new Main();
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

            if (GUI.controls_accessed)  {
                GUI.accessControls(window);
            }
            else {
                glClear(GL_COLOR_BUFFER_BIT);
                GUI.renderGUI(shader);
                GUI.checkButtons(window);
            }
            window.swapBuffers();

            try {
                Thread.sleep(  100);
            }
            catch  (InterruptedException e) {
                System.exit(1);
            }
        }

    }


    public void renderImage(Shader shader, Texture texture, float x, float y, Matrix4f scale, Model model){
        shader.bind();
        texture.bind(0);
        Matrix4f target = new Matrix4f();

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x,y,0));
        scale.mul(tile_pos, target);


        shader.setUniform("sampler",0);
        shader.setUniform("projection", target);
        model.render();


    }

    public void renderGUI(Shader shader) {

            Texture[] menuTextures = new Texture[]{
                    new Texture("assets/resume_button.png"), new Texture("assets/controls_button.png"),
                    new Texture("assets/settings_button.png"), new Texture("assets/exit_button.png")
            };

            float y = 0.55f;
            for (Texture t : menuTextures) {
                float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
                vertices[0] *= t.getWidth() * 0.002;
                vertices[3] *= t.getWidth() * 0.002;
                vertices[6] *= t.getWidth() * 0.002;
                vertices[9] *= t.getWidth() * 0.002;

                vertices[1] *= t.getHeight() * 0.005;
                vertices[4] *= t.getHeight() * 0.005;
                vertices[7] *= t.getHeight() * 0.005;
                vertices[10] *= t.getHeight() * 0.005;

                Model model = new Model(vertices, textureDocks, indices);
                renderImage(shader, t, 0f, y, new Matrix4f(), model);

//            -1 -- 0 -- 1

                this.addButton(0 + vertices[0], y + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], t.getPath());
                y += (-0.35f);
            }

    }

    public void addButton(float x, float y, float width, float height, String filepath) {

        Rectangle2D.Float bounds = new Rectangle2D.Float(x, y, width , height);
        buttons.put(bounds, filepath);
    }


    public void checkButtons(Window window)   {
        for (Rectangle2D.Float b : buttons.keySet())   {

            double mouseX = ((window.getInput().getMouseCoordinates()[0])/(window.getWidth()/2))-1;
            double mouseY = -(((window.getInput().getMouseCoordinates()[1])/(window.getHeight()/2))-1);

            if (mouseX >= b.getX() && mouseX <= (b.getX()+b.getWidth()) && mouseY <= b.getY() && mouseY >= (b.getY()-b.getHeight()))  {
                if (window.getInput().isMouseButtonDown(GLFW_MOUSE_BUTTON_1)){

                    String buttonName = buttons.get(b);
                    switch(buttonName) {
                        case "resume_button.png": this.resumeGame();
                            break;
                        case "controls_button.png": this.accessControls(window);
                            controls_accessed = true;;
                            break;
                        case "settings_button.png": System.exit(3);
                            break;
                        case "exit_button.png": this.exitGame();
                            break;
                     }
                }
            }
            else    {
                continue;
            }
        }
    }

    public void resumeGame(){}
    public void accessControls(Window window) {
        if (controls_accessed) {
            glClear(GL_COLOR_BUFFER_BIT);
            Shader shader = new Shader("assets/shader");
            Texture controlsTexture = new Texture("assets/Keyboard_asset.png");
            Model model = new Model(baseVertices, textureDocks, indices);
            Matrix4f scale = new Matrix4f();
            this.renderImage(shader, controlsTexture, 0, 0, scale, model);

            if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                controls_accessed = false;
            }
        }
    }
    public void accessSettings(){}
    public void exitGame()  {
        System.exit(1);
    }



}