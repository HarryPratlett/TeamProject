package com.myst.GUI;

import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.rendering.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import java.util.HashMap;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;


public class GUI {

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

    private Boolean controls_accessed;
    private Boolean settings_accessed;
    private Boolean close_window;
    int volume;
    int brightness;

    public GUI()    {
        controls_accessed = false;
        settings_accessed = false;
        close_window = false;
        volume = 3;
        brightness = 3;
    }

    public static void main(String[] args){
        GUI GUI = new GUI();
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
                GUI.checkSettingsButtons(window);
            }
            else {
                glClear(GL_COLOR_BUFFER_BIT);
                GUI.renderGUI(shader);
                GUI.checkButtons(window);
            }
            window.swapBuffers();

            if (GUI.close_window)   {
                glfwSetWindowShouldClose(window.getWindow(), true);
                glClear(GL_COLOR_BUFFER_BIT);
            }
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
                vertices = this.alterVertices(vertices, t.getHeight(), t.getWidth(), 0.002, 0.005);
                Model model = new Model(vertices, textureDocks, indices);
                renderImage(shader, t, 0f, y, new Matrix4f(), model);
                this.addButton(0 + vertices[0], y + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], t.getPath());
                y += (-0.35f);
            }

    }

    public void addButton(float x, float y, float width, float height, String filepath) {
        Rectangle2D.Float bounds = new Rectangle2D.Float(x, y, width , height);
        if(settings_accessed)  {
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
                        case "resume_button.png": this.resumeGame(window);
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
                if (window.getInput().isMousePressed(GLFW_MOUSE_BUTTON_1)) {
                    String buttonName = settings_buttons.get(b);
                    switch(buttonName)  {
                        case "plus1.png":
                            if (brightness < 5) { brightness += 1; }
                            break;
                        case "minus1.png":
                            if (brightness > 0) { brightness -= 1; }
                            break;
                        case "plus.png":
                            if (volume < 5) { volume += 1;  }
                            break;
                        case "minus.png":
                            if (volume > 0) { volume -= 1; }
                            break;
                    }
                }
            }
        }
    }

    public void resumeGame(Window window){
        this.close_window = true;

    }
    public void accessControls(Window window) {
        if (controls_accessed) {
            glClear(GL_COLOR_BUFFER_BIT);
            Shader shader = new Shader("assets/shader");
            Texture controlsTexture = new Texture("assets/Keyboard_asset.png");
            float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
            vertices = this.alterVertices(vertices, controlsTexture.getHeight(), controlsTexture.getWidth(), 0.0007, 0.0025);
            Model model = new Model(vertices, textureDocks, indices);
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
                vertices = this.alterVertices(vertices, t.getHeight(), t.getWidth(), 0.002, 0.005);
                Model model = new Model(vertices, textureDocks, indices);
                this.renderImage(shader, t, -0.5f, y, scale, model);
                y -= -0.35f;
            }

            Texture[] audioTextures = new Texture[]{new Texture("assets/volume_assets/0.png"), new Texture("assets/volume_assets/1.png"),new Texture("assets/volume_assets/2.png"),
                    new Texture("assets/volume_assets/3.png"), new Texture("assets/volume_assets/4.png"), new Texture("assets/volume_assets/5.png")};

            Texture[] alterTextures = new Texture[]{new Texture ("assets/volume_assets/plus.png"), new Texture("assets/volume_assets/minus.png"), new Texture("assets/volume_assets/plus1.png"), new Texture("assets/volume_assets/minus1.png")};

            float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
            vertices = this.alterVertices(vertices, alterTextures[0].getWidth(), alterTextures[0].getHeight(), 0.002, 0.005);

            Model model = new Model(vertices, textureDocks, indices);
            this.renderImage(shader, alterTextures[0], 0f, 0.35f, scale, model);
            this.addButton(0 + vertices[0], 0.35f + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], alterTextures[0].getPath());
            this.renderImage(shader, alterTextures[1], 0.5f, 0.35f, scale, model);
            this.addButton(0.5f + vertices[0], 0.35f + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], alterTextures[1].getPath());
            this.renderImage(shader, alterTextures[2], 0f, 0f, scale, model);
            this.addButton(0 + vertices[0], 0f + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], alterTextures[2].getPath());
            this.renderImage(shader, alterTextures[3], 0.5f, 0f, scale, model);
            this.addButton(0.5f + vertices[0], 0f + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], alterTextures[3].getPath());

            for (Texture t: audioTextures)  {
                if (t.getPath().contains(Integer.toString(volume))) {
                    this.renderImage(shader, t, 0.25f, 0.35f, scale, model);
                }
                if (t.getPath().contains(Integer.toString(brightness))) {
                    this.renderImage(shader, t, 0.25f, 0f, scale, model);
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


    public float[] alterVertices(float[] vertices, int height, int width, double widthScale, double heightScale) {
        vertices[0] *= width * widthScale;
        vertices[3] *= width * widthScale;
        vertices[6] *= width * widthScale;
        vertices[9] *= width * widthScale;

        vertices[1] *= height * heightScale;
        vertices[4] *= height * heightScale;
        vertices[7] *= height * heightScale;
        vertices[10] *= height * heightScale;
        return vertices;
    }


}