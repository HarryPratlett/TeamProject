package com.myst;

import com.myst.GUI.GUI;
import com.myst.GUI.Menu;
import com.myst.helper.Timer;
import com.myst.networking.EntityData;
import com.myst.networking.clientside.ClientConnection;
import com.myst.rendering.Window;
import com.myst.world.entities.Entity;
import org.lwjgl.opengl.GL;

import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;
public class Main {



        public static void main(String[] args) {
//        setting up opengl
        Window.setCallbacks();
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }
        Window window = new Window();
        window.createWindow("MySt");
        window.setFullscreen(false);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0f, 0f, 0f, 0f);


        System.out.println("starting game");


        Menu menu = new Menu(window,window.getInput());
        GameMain gm;
        ClientConnection connection = new ClientConnection(null,null,null, null);
        ProgramState state = ProgramState.MAIN_MENUS;

        ConcurrentHashMap<String, ConcurrentHashMap<Integer, Entity>> entities = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, EntityData>> toRender = new ConcurrentHashMap<>();






        Boolean renderFrame;
        double frame_cap = 1.0 / 60.0;

        double time = Timer.getTime();
        double unprocessed = 0;

        double frame_time = 0;
        int frames = 0;

        double debugCurrentTime;
        double debugLastTime = Timer.getTime();
        while (!window.shouldClose()) {

            renderFrame = false;
            double time2 = Timer.getTime();
            double deltaTime = time2 - time;
            time = time2;
            unprocessed += deltaTime;
            frame_time += deltaTime;

            while (unprocessed >= frame_cap) {
                unprocessed -= frame_cap;
                renderFrame = true;
                debugCurrentTime = Timer.getTime();
                double timeSinceLastUpdate = (debugCurrentTime - debugLastTime);
                debugLastTime = debugCurrentTime;
                window.update();

                switch(state){
                    case MAIN_MENUS:
                        state = updateMenu(menu);
                        break;
                    case SWITCH_TO_GAME_FROM_MENU:
                        String server = menu.ipAddress + ":" + menu.port;
                        entities = new ConcurrentHashMap<>();
                        toRender = new ConcurrentHashMap<>();

                        connection = new ClientConnection(entities,toRender,menu.ipAddress, "MYID");
                        connection.run();
                        state = ProgramState.IN_GAME;
                        break;
                    case IN_GAME:
                        GameMain.main(window,connection.map,entities,toRender,"MYID");
                        break;
                }

            }

//          this is purely for debugging the frame rate
            if (frame_time >= 1) {
                System.out.println(frames);
                frame_time = 0;
                frames = 0;
            }

            if (renderFrame) {
                glClear(GL_COLOR_BUFFER_BIT);
                switch(state){
                    case MAIN_MENUS:
                        renderMenu(menu);
                        break;

                }
                window.swapBuffers();
                frames += 1;
            }
        }

        glfwTerminate();
        System.exit(1);

    }
    public static ProgramState updateMenu(Menu menu){
        return menu.update();
    }
    public static void renderMenu(Menu menu){
            menu.render();
    }






}
