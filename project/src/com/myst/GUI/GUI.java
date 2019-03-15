/**
 * This program generates the GUI that appears when the users presses the escape key during the game.
 * The user can access the controls, settings and exit the game from this menu. They can also resume the game if they
 * wish. The volume and brightness can be altered within the settings.
 *
 * @author Harry Pratlett
 * @version 1.0
 * @since 2019-03-12
 */
package com.myst.GUI;


import com.myst.input.Input;
import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.rendering.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import java.util.HashMap;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;



public class GUI {

    private HashMap<Rectangle2D.Float, String> buttons = new HashMap<>();
    private HashMap<Rectangle2D.Float, String> settings_buttons = new HashMap<>();

    private final float[] baseVertices = new float[] {
            -1f, 0.5f, 0f, /*0*/  1f, 0.5f, 0f, /*1*/    1f, -0.5f, 0f, /*2*/
            -1f, -0.5f, 0f/*3*/
    };

    private float[] textureDocks = new float[] {
            0f, 0f,   1, 0f,  1f, 1f,
            0f, 1f
    };

    private int[] indices = new int[] {
            0,1,2,
            2,3,0
    };

    private Window window;
    private GUIStates currentWindow;
    private Boolean controls_accessed;
    private Boolean settings_accessed;
    private Shader shader;
    private Input input;
    private int volume;
    private int brightness;

    /** @param window The window which the GUI will be displayed in
     * @param input The input via which the mouse and keyboard inputs are taken
     * @param audio The audio which is played and can be changed in settings**/
    public GUI(Window window, Input input, Audio audio)    {
        settings_accessed = false;
        volume = 3;
        brightness = 3;
        currentWindow = GUIStates.HIDDEN;
        this.window = window;
        this.input = input;
    }

    public void render(Shader shader){
    /** This is the main method that will render each of the potential windows in the GUI. It is called in the Main
     * class of this project. The window that is rendered depends on currentWindow.
     */
    public void render(){
        switch(currentWindow){
            case MAIN_MENU:
                this.renderGUI(shader);
                break;
            case CONTROLS:
                this.renderControls(shader);
                break;
            case SETTINGS:
                this.renderSettings(shader);
                break;
        }
    }

    /** This is other main method of the class which will update what needs to be rendered on screen. This is done by
     * calling other methods which will detect input on screen that will cause certain actions like changing the window
     * to be done in these methods
     */
    public void update(){
        switch(currentWindow){
            case MAIN_MENU:
                mainMenuInput();
                break;
            case CONTROLS:
                controlsMenuInput();
                break;
            case SETTINGS:
                settingsMenuInput();
                break;
            case HIDDEN:
                hiddenInput();
                break;
        }
    }

    public void hiddenInput(){
        if (this.input.isKeyPressed(GLFW_KEY_ESCAPE)) {
            currentWindow = GUIStates.MAIN_MENU;
        }
    }

    public void controlsMenuInput(){
        if (this.input.isKeyPressed(GLFW_KEY_ESCAPE)) {
            currentWindow = GUIStates.MAIN_MENU;
        }
    }

    /**
     * This is the main method for rendering images onto the canvas
     * @param shader
     * @param texture The image which will be rendered onto the screen
     * @param x The x co-ordinate of where the image will be rendered
     * @param y The y co-ordinate of where the image will be rendered
     * @param scale The scale of the image with regards to sizing
     * @param model
     */
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

    /**
     * This method adds a button when buttons have been rendered and we want input
     * @param x X co-ordinate of the button
     * @param y Y co-ordinae of the button
     * @param width Width of the buttons clickable area
     * @param height Height of the buttons clickable area
     * @param filepath The filepath of the button so it can be added to a hashmap which can be searched later
     */
    public void addButton(float x, float y, float width, float height, String filepath) {
        Rectangle2D.Float bounds = new Rectangle2D.Float(x, y, width , height);
        if(settings_accessed)  {
            settings_buttons.put(bounds, filepath);
        }
        else {
            buttons.put(bounds, filepath);
        }
    }

    /**
     * Handles the input in the main menu of the gui
     */
    public void mainMenuInput()   {
        for (Rectangle2D.Float b : buttons.keySet())   {

            double mouseX = ((this.input.getMouseCoordinates()[0])/(window.getWidth()/2))-1;
            double mouseY = -(((this.input.getMouseCoordinates()[1])/(window.getHeight()/2))-1);

            if (mouseX >= b.getX() && mouseX <= (b.getX()+b.getWidth()) && mouseY <= b.getY() && mouseY >= (b.getY()-b.getHeight()))  {
                if (window.getInput().isMouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                    String buttonName = buttons.get(b);
                    switch(buttonName) {
                        case "resume_button.png": this.currentWindow = GUIStates.HIDDEN;
                            break;
                        case "controls_button.png": this.currentWindow = GUIStates.CONTROLS;
                            break;
                        case "settings_button.png": this.currentWindow = GUIStates.SETTINGS;
                            settings_accessed = true;
                            break;
                        case "exit_button.png": this.exitGame();
                            break;
                     }
                }
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
            currentWindow = GUIStates.HIDDEN;
        }

    }

    /**
     * Handles the input in the settings menu of the GUI
     */
    public void settingsMenuInput() {
        for (Rectangle2D.Float b : settings_buttons.keySet()) {

            double mouseX = ((this.input.getMouseCoordinates()[0]) / (window.getWidth() / 2)) - 1;
            double mouseY = -(((this.input.getMouseCoordinates()[1]) / (window.getHeight() / 2)) - 1);

            if (mouseX >= b.getX() && mouseX <= (b.getX() + b.getWidth()) && mouseY <= b.getY() && mouseY >= (b.getY() - b.getHeight())) {
                if (window.getInput().isMousePressed(GLFW_MOUSE_BUTTON_1)) {
                    try {
                        Thread.sleep(70);
                    }
                    catch(Exception e)  {
                        e.printStackTrace();
                    }
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
        if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
            currentWindow = GUIStates.MAIN_MENU;
        }
    }

    /**
     * Renders the controls image
     */
    public void renderControls(Shader shader) {

        glClear(GL_COLOR_BUFFER_BIT);
        Texture controlsTexture = new Texture("assets/Keyboard_asset.png");
        float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
        vertices = this.alterVertices(vertices, controlsTexture.getHeight(), controlsTexture.getWidth(), 0.0007, 0.0025);
        Model model = new Model(vertices, textureDocks, indices);
        Matrix4f scale = new Matrix4f();
        this.renderImage(shader, controlsTexture, 0, 0, scale, model);
    }

    /**
     * Renders the settings menu
     */
    public void renderSettings(Shader shader){
        glClear(GL_COLOR_BUFFER_BIT);
        this.renderBackground();
        Shader shader = new Shader("assets/shader");
        Texture[] settingsTextures = new Texture[]{new Texture("assets/brightness_button.png"), new Texture("assets/volume_button.png")};
        Matrix4f scale = new Matrix4f();
        float y = 0;
        Texture menuTexture = new Texture("assets/settings_button.png");
        float[] titleVertices = Arrays.copyOf(baseVertices, baseVertices.length);
        titleVertices = this.alterVertices(titleVertices, menuTexture.getHeight(), menuTexture.getWidth(), 0.002, 0.005);
        Model titleModel = new Model(titleVertices, textureDocks, indices);
        this.renderImage(shader, menuTexture, 0, 0.75f, scale, titleModel);
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
    }

    /**
     * Neater method to exit the game
     */
    public void exitGame()  {
        System.exit(1);
    }


    /**
     * Alters a given float arrays variables
     * @param vertices The float array that will be altered
     * @param height The height of the image we want these vertices to be scaled to
     * @param width The width of the image we want the vertices to be scaled to
     * @param widthScale The scale of the width
     * @param heightScale The scale of the height
     * @return Returns an altered float array of the vertices given
     */
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

    /**
     * Renders the background in all parts except controls
     */
    public void renderBackground()  {
        Texture background = new Texture("assets/main_menu/NighBg.png");
        float [] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
        vertices = this.alterVertices(vertices, background.getHeight(), background.getWidth(), 0.001, 0.003);
        Model model = new Model(vertices, textureDocks, indices);
        this.renderImage(shader, background, 0, 0, new Matrix4f(), model);
    }
}