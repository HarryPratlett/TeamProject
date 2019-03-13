package com.myst.GUI;


import com.myst.helper.Timer;
import com.myst.input.Input;
import com.myst.rendering.Model;
import com.myst.rendering.Shader;
import com.myst.rendering.Window;
import com.myst.rendering.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashMap;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;


public class Menu {
    private final float[] baseVertices = new float[] {
            -1f, 0.5f, 0f, /*0*/  1f, 0.5f, 0f, /*1*/    1f, -0.5f, 0f, /*2*/
            -1f, -0.5f, 0f/*3*/
    };
    private final float[] textureDocks = new float[] {
            0f, 0f,   1, 0f,  1f, 1f,
            0f, 1f
    };
    private final int[] indices = new int[] {
            0,1,2,
            2,3,0
    };
    private HashMap<Rectangle2D.Float, String> menuButtons = new HashMap<>();
    private HashMap<Rectangle2D.Float, String> multiplayerButtons = new HashMap<>();
    private HashMap<Rectangle2D.Float, String> joinGameButtons = new HashMap<>();
    private Window window;
    private Shader shader;
    private Input input;
    private MenuStates currentWindow;
    private Boolean multiplayerAccessed;
    private Boolean joinGameAccessed;
    private Boolean hostGameAccessed;
    private Boolean isIpAddress;
    private String ipAddress;
    private String port;


    public Menu(Window window, Input input)   {
        this.window = window;
        this.shader = new Shader ("assets/shader");
        this.input = input;
        this.currentWindow = MenuStates.MAIN_MENU;
        this.multiplayerAccessed = false;
        this.joinGameAccessed = false;
        this.ipAddress = "";
        this.port = "";
    }

    public static void main(String[] args) {
        Window.setCallbacks();
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }

            Window window = new Window();
            window.setFullscreen(false);
            window.createWindow("My game");

            GL.createCapabilities();
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glClearColor(0f, 0f, 0f, 0f);
            Boolean renderFrame;
            double frame_cap = 1.0 / 60.0;

            double time = Timer.getTime();
            double unprocessed = 0;

            double frame_time = 0;
            int frames = 0;

            double debugCurrentTime = Timer.getTime();
            double debugLastTime = Timer.getTime();
            Menu menu = new Menu(window, window.getInput());
            while (!window.shouldClose()) {

                renderFrame = false;
                double time2 = Timer.getTime();
                double deltaTime = time2 - time;
                time = time2;
                unprocessed += deltaTime;
                frame_time += deltaTime;
//            in the case you want to render a frame as you have gone over the frame_cap
//            a while is used instead of an if incase the performance is less than 30 FPS
                while (unprocessed >= frame_cap) {
//                look into effects of containing a thread.sleep();
//                take away the frame cap so that you account for the time you've taken of the next frame
                    unprocessed -= frame_cap;
                    renderFrame = true;
                    debugCurrentTime = Timer.getTime();
                    double timeSinceLastUpdate = (debugCurrentTime - debugLastTime);
                    debugLastTime = debugCurrentTime;
                    window.update();
                    menu.update();

                }
                if (frame_time >= 1) {
                    System.out.println(frames);
                    frame_time = 0;
                    frames = 0;
                }
                if (renderFrame) {
                    glClear(GL_COLOR_BUFFER_BIT);
                    menu.render();
                    window.swapBuffers();
                    frames += 1;
                }
            }

            glfwTerminate();
            System.exit(1);
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

    public void render(){
        switch(currentWindow){
            case MAIN_MENU:
                this.renderMenu();
                break;
            case MULTIPLAYER:
                this.renderMultiplayer();
                break;
            case HOST_GAME:
                this.renderHostGame();
                break;
            case HIDDEN:
                break;
            case JOIN_GAME:
                this.renderJoinGame();
                break;
            case ENTERING:
                this.renderJoinGame();
                this.renderText(ipAddress, 0.17f, 0.25f);
                this.renderText(port, 0.35f, -0.10f);
                break;
        }
    }

    public void update(){
        switch(currentWindow){
            case MAIN_MENU:
                this.mainMenuInput();
                break;
            case MULTIPLAYER:
                multiplayerAccessed = true;
                this.multiplayerInput();
                break;
            case HOST_GAME:
                //todo add the render text of the ip and port passed by the main and then add ok button at bottom
                hostGameAccessed = true;
                this.hostGameInput();
            case JOIN_GAME:
                //todo add the submit button and checks when integrating
                joinGameAccessed = true;
                this.joinGameInput();
                break;
            case ENTERING:
                this.joinGameInput();
                this.takeInput();
            case HIDDEN:
                break;
        }
    }


    public void renderMenu()    {
        glClear(GL_COLOR_BUFFER_BIT);
        this.renderBackground();
        Texture[] menuTextures = new Texture[]{new Texture("assets/main_menu/singleplayer_button.png"),
                new Texture("assets/main_menu/multiplayer_button.png"), new Texture("assets/main_menu/quit_button.png")};
        setupImages(menuTextures, 0f, 0.35f, true);
    }

    public void renderMultiplayer() {
        glClear(GL_COLOR_BUFFER_BIT);
        this.renderBackground();
        Texture[] multiplayerTextures = new Texture[]{new Texture("assets/main_menu/host_game_button.png"),
        new Texture("assets/main_menu/join_game_button.png")};
        setupImages(multiplayerTextures, 0f, 0.33f, true);
    }

    public void renderHostGame()    {
        glClear(GL_COLOR_BUFFER_BIT);
        this.renderBackground();
        Texture[] hostGameTextures = new Texture[]{new Texture("assets/main_menu/IP.png"), new Texture("assets/main_menu/port.png"), new Texture("assets/main_menu/text_box_1.png"), new Texture("assets/main_menu/text_box_2.png")};
        setupImages(Arrays.copyOfRange(hostGameTextures, 0, 2), -0.5f, 0.25f, false);
        setupImages(Arrays.copyOfRange(hostGameTextures, 2, 4), 0.5f, 0.25f, false);
    }
    public void renderJoinGame()    {
        glClear(GL_COLOR_BUFFER_BIT);
        this.renderBackground();
        Texture[] joinGameTextures = new Texture[]
                {new Texture("assets/main_menu/IP.png"), new Texture("assets/main_menu/port.png"), new Texture("assets/main_menu/text_box_1.png"), new Texture("assets/main_menu/text_box_2.png")};
        setupImages(Arrays.copyOfRange(joinGameTextures, 0, 2), -0.5f, 0.25f, false);
        setupImages(Arrays.copyOfRange(joinGameTextures, 2, 4), 0.5f, 0.25f, true);
    }

    public void addButton(float x, float y, float width, float height, String filepath) {
        Rectangle2D.Float bounds = new Rectangle2D.Float(x, y, width , height);
        if (multiplayerAccessed)    {
            multiplayerButtons.put(bounds, filepath);
        }
        else if(joinGameAccessed)   {
            joinGameButtons.put(bounds, filepath);
        }
        else    {
            menuButtons.put(bounds, filepath);
        }
    }

    public void mainMenuInput()   {
        for (Rectangle2D.Float b : menuButtons.keySet())   {

            double mouseX = ((this.input.getMouseCoordinates()[0])/(window.getWidth()/2))-1;
            double mouseY = -(((this.input.getMouseCoordinates()[1])/(window.getHeight()/2))-1);

            if (mouseX >= b.getX() && mouseX <= (b.getX()+b.getWidth()) && mouseY <= b.getY() && mouseY >= (b.getY()-b.getHeight()))  {
                if (window.getInput().isMouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                    String buttonName = menuButtons.get(b);
                    switch(buttonName) {
                        case "singleplayer_button.png":
                            break;
                        case "multiplayer_button.png":
                            try {
                                Thread.sleep(70);
                            } catch (Exception e)   {
                                e.printStackTrace();
                            }
                            this.currentWindow = MenuStates.MULTIPLAYER;
                            break;
                        case "quit_button.png":
                            System.exit(1);
                            break;
                    }
                }
            }
        }
    }

    public void multiplayerInput()   {
        for (Rectangle2D.Float b : multiplayerButtons.keySet())   {

            double mouseX = ((this.input.getMouseCoordinates()[0])/(window.getWidth()/2))-1;
            double mouseY = -(((this.input.getMouseCoordinates()[1])/(window.getHeight()/2))-1);

            if (mouseX >= b.getX() && mouseX <= (b.getX()+b.getWidth()) && mouseY <= b.getY() && mouseY >= (b.getY()-b.getHeight()))  {
                if (window.getInput().isMouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                    String buttonName = multiplayerButtons.get(b);
                    switch(buttonName) {
                        case "host_game_button.png":
                            multiplayerAccessed = false;
                            this.currentWindow = MenuStates.HOST_GAME;
                            break;
                        case "join_game_button.png":
                            multiplayerAccessed = false;
                            this.currentWindow = MenuStates.JOIN_GAME;
                            break;
                    }
                }
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
            multiplayerAccessed = false;
            currentWindow = MenuStates.MAIN_MENU;
        }
    }

    public void hostGameInput() {
    }

    public void joinGameInput()   {
        for (Rectangle2D.Float b : joinGameButtons.keySet())   {

            double mouseX = ((this.input.getMouseCoordinates()[0])/(window.getWidth()/2))-1;
            double mouseY = -(((this.input.getMouseCoordinates()[1])/(window.getHeight()/2))-1);

            if (mouseX >= b.getX() && mouseX <= (b.getX()+b.getWidth()) && mouseY <= b.getY() && mouseY >= (b.getY()-b.getHeight()))  {
                if (window.getInput().isMouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                    String buttonName = joinGameButtons.get(b);
                    switch(buttonName) {
                        case "text_box_1.png":
                            this.currentWindow = MenuStates.ENTERING;
                            this.isIpAddress = true;
                            break;
                        case "text_box_2.png":
                            this.currentWindow = MenuStates.ENTERING;
                            this.isIpAddress = false;
                            break;
                    }
                }
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
            this.ipAddress = "";
            this.port = "";
            this.joinGameAccessed = false;
            this.currentWindow = MenuStates.MULTIPLAYER;
        }
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

    public void setupImages(Texture[] textureArray, Float x, Float y, Boolean isButton)  {
        Float yPos = y;
        for (Texture t: textureArray)   {
            float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
            vertices = this.alterVertices(vertices, t.getHeight(), t.getWidth(), 0.002, 0.005);
            Model model = new Model(vertices, textureDocks, indices);
            renderImage(shader, t, x, yPos, new Matrix4f(), model);
            if(isButton) {
                this.addButton(0 + vertices[0], yPos + vertices[1], vertices[3] - vertices[0], vertices[1] - vertices[7], t.getPath());
            }
            yPos += (-0.35f);
        }
    }

    public void takeInput() {
        glfwPollEvents();
        if(isIpAddress) {
            if (this.ipAddress.length() <= 10) {
                if (input.isKeyPressed(GLFW_KEY_1)) {
                    this.ipAddress += "1";
                } else if (input.isKeyPressed(GLFW_KEY_2)) {
                    this.ipAddress += "2";
                } else if (input.isKeyPressed(GLFW_KEY_3)) {
                    this.ipAddress += "3";
                } else if (input.isKeyPressed(GLFW_KEY_4)) {
                    this.ipAddress += "4";
                } else if (input.isKeyPressed(GLFW_KEY_5)) {
                    this.ipAddress += "5";
                } else if (input.isKeyPressed(GLFW_KEY_6)) {
                    this.ipAddress += "6";
                } else if (input.isKeyPressed(GLFW_KEY_7)) {
                    this.ipAddress += "7";
                } else if (input.isKeyPressed(GLFW_KEY_8)) {
                    this.ipAddress += "8";
                } else if (input.isKeyPressed(GLFW_KEY_9)) {
                    this.ipAddress += "9";
                } else if (input.isKeyPressed(GLFW_KEY_0)) {
                    this.ipAddress += "0";
                } else if (input.isKeyPressed(GLFW_KEY_PERIOD)) {
                    this.ipAddress += ".";
                }
            }
            if (input.isKeyPressed(GLFW_KEY_BACKSPACE))  {
                if (this.ipAddress.length() >= 1) {
                    this.ipAddress = this.ipAddress.substring(0, ipAddress.length() - 1);
                }
            }
        }
        else if (!isIpAddress)    {
            if (this.port.length() <= 4) {
                if (input.isKeyPressed(GLFW_KEY_1)) {
                    this.port += "1";
                } else if (input.isKeyPressed(GLFW_KEY_2)) {
                    this.port += "2";
                } else if (input.isKeyPressed(GLFW_KEY_3)) {
                    this.port += "3";
                } else if (input.isKeyPressed(GLFW_KEY_4)) {
                    this.port += "4";
                } else if (input.isKeyPressed(GLFW_KEY_5)) {
                    this.port += "5";
                } else if (input.isKeyPressed(GLFW_KEY_6)) {
                    this.port += "6";
                } else if (input.isKeyPressed(GLFW_KEY_7)) {
                    this.port += "7";
                } else if (input.isKeyPressed(GLFW_KEY_8)) {
                    this.port += "8";
                } else if (input.isKeyPressed(GLFW_KEY_9)) {
                    this.port += "9";
                } else if (input.isKeyPressed(GLFW_KEY_0)) {
                    this.port += "0";
                }
            }
            if (input.isKeyPressed(GLFW_KEY_BACKSPACE))  {
                if (this.port.length() >= 1) {
                    this.port = this.port.substring(0, port.length() - 1);
                }
            }
        }

    }

    public void renderText(String input, float x, float y)    {
        char[] charInput = input.toCharArray();

        for(char c : charInput) {
            Texture texture;
            if (Character.toString(c).equals("."))   {
                texture = new Texture("assets/main_menu/typing/dot.png");
                float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
                vertices = this.alterVertices(vertices, texture.getHeight(), texture.getWidth(), 0.005, 0.006);
                Model model = new Model(vertices, textureDocks, indices);
                this.renderImage(shader, texture, x, y, new Matrix4f(), model);
            }
            else {
                texture = new Texture("assets/main_menu/typing/" + Character.toString(c) + ".png");
                float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
                vertices = this.alterVertices(vertices, texture.getHeight(), texture.getWidth(), 0.002, 0.006);
                Model model = new Model(vertices, textureDocks, indices);
                this.renderImage(shader, texture, x, y, new Matrix4f(), model);
            }
            x = x + 0.065f;
        }
    }

    public void renderBackground()  {
        Texture background = new Texture("assets/main_menu/NighBg0.jpg");
        float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
        vertices = this.alterVertices(vertices, background.getHeight(), background.getWidth(), 0.001, 0.003);
        Model model = new Model(vertices, textureDocks, indices);
        this.renderImage(shader, background, 0, 0, new Matrix4f(), model);
    }
}
