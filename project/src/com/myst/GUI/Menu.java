package com.myst.GUI;

import com.myst.audio.Audio;
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
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class Menu {
    private HashMap<Rectangle2D.Float, String> menuButtons = new HashMap<>();

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

    private Window window;
    private Shader shader;
    private Input input;

    public Menu(Window window, Input input)   {
        this.window = window;
        this.shader = new Shader ("assets/shader");
        this.input = input;
    }

    public static void main(String[] args)  {
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
                menu.mainMenuInput();

            }
            if (frame_time >= 1) {
                System.out.println(frames);
                frame_time = 0;
                frames = 0;
            }


            if (renderFrame) {
                glClear(GL_COLOR_BUFFER_BIT);
                menu.renderMenu();
                window.swapBuffers();
                frames += 1;

            }


        }
        //        clears everything we have used from memory
        glfwTerminate();

//        sloppy and needs tidying
        System.exit(1);

    }


    public void renderMenu()    {
        Texture[] menuTextures = new Texture[]{new Texture("assets/main_menu/singleplayer_button.png"),
                new Texture("assets/main_menu/multiplayer_button.png"), new Texture("assets/main_menu/quit_button.png")};
        float x = 0;
        float y = 0.5f;

        for (Texture t: menuTextures)   {
            float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
            vertices = this.alterVertices(vertices, t.getHeight(), t.getWidth(), 0.002, 0.005);
            Model model = new Model(vertices, textureDocks, indices);
            renderImage(shader, t, x, y, new Matrix4f(), model);
            this.addButton(0 + baseVertices[0], y + baseVertices[1], baseVertices[3] - baseVertices[0], baseVertices[1] - baseVertices[7], t.getPath());
            y += (-0.35f);
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

    public void addButton(float x, float y, float width, float height, String filepath) {
        Rectangle2D.Float bounds = new Rectangle2D.Float(x, y, width , height);
        menuButtons.put(bounds, filepath);
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
                            break;
                        case "quit_button.png":
                            System.exit(1);
                            break;
                    }
                }
            }
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
}
