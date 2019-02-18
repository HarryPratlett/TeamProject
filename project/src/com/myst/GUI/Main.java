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
    HashMap<Rectangle2D.Float, String> settings_buttons = new HashMap<>();

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

    private Boolean controls_accessed = false;
    private Boolean settings_accessed = false;
    int volume = 3;
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
            else if(GUI.settings_accessed)  {
                GUI.accessSettings(window);
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
        if(filepath.contains("volume_assets")||filepath.contains("brightness_assets"))  {
            settings_buttons.put(bounds, filepath);
        }
        else {
            buttons.put(bounds, filepath);
        }
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
                            controls_accessed = true;
                            break;
                        case "settings_button.png": this.accessSettings(window);
                            settings_accessed = true;
                            break;
                        case "exit_button.png": this.exitGame();
                            break;
                     }
                }
            }
        }
    }

    public void checkSettingsButtons(Window window) {
        for (Rectangle2D.Float b : settings_buttons.keySet()) {

            double mouseX = ((window.getInput().getMouseCoordinates()[0]) / (window.getWidth() / 2)) - 1;
            double mouseY = -(((window.getInput().getMouseCoordinates()[1]) / (window.getHeight() / 2)) - 1);

            if (mouseX >= b.getX() && mouseX <= (b.getX() + b.getWidth()) && mouseY <= b.getY() && mouseY >= (b.getY() - b.getHeight())) {
                if (window.getInput().isMouseButtonDown(GLFW_MOUSE_BUTTON_1)) {
                    System.exit(1);
                }
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
    public void accessSettings(Window window){
        if (settings_accessed)  {
            glClear(GL_COLOR_BUFFER_BIT);
            Shader shader = new Shader("assets/shader");
            Texture[] settingsTextures = new Texture[]{new Texture("assets/brightness_button.png"), new Texture("assets/volume_button.png")};
            Matrix4f scale = new Matrix4f();
            float y = 0;
            for (Texture t : settingsTextures)   {
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
                this.renderImage(shader, t, -0.5f, y, scale, model);
                y -= -0.35f;
            }

            Texture[] audioTextures = new Texture[]{new Texture("assets/volume_assets/0.png"), new Texture("assets/volume_assets/1.png"),new Texture("assets/volume_assets/2.png"),
                    new Texture("assets/volume_assets/3.png"), new Texture("assets/volume_assets/4.png"), new Texture("assets/volume_assets/5.png"),
                    new Texture("assets/volume_assets/plus.png"), new Texture("assets/volume_assets/minus.png"),};

            float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
            vertices[0] *= audioTextures[6].getWidth() * 0.002;
            vertices[3] *= audioTextures[6].getWidth() * 0.002;
            vertices[6] *= audioTextures[6].getWidth() * 0.002;
            vertices[9] *= audioTextures[6].getWidth() * 0.002;

            vertices[1] *= audioTextures[6].getHeight() * 0.005;
            vertices[4] *= audioTextures[6].getHeight() * 0.005;
            vertices[7] *= audioTextures[6].getHeight() * 0.005;
            vertices[10] *= audioTextures[6].getHeight() * 0.005;

            Model model = new Model(vertices, textureDocks, indices);
            this.renderImage(shader, audioTextures[6], 0f, 0.35f, scale, model);
            this.addButton(0 + vertices[0], 0.35f + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], audioTextures[6].getPath());
            this.renderImage(shader, audioTextures[7], 0.5f, 0.35f, scale, model);
            this.addButton(0.5f + vertices[0], 0.35f + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], audioTextures[7].getPath());

            for (Texture t: audioTextures)  {
                if (t.getPath().contains(Integer.toString(volume))) {
                    this.renderImage(shader, t, 0.25f, 0.35f, scale, model);
                }
            }
            if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                settings_accessed = false;
            }
        }
    }
    public void exitGame()  {
        System.exit(1);
    }



}