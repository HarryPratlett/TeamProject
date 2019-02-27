
package com.myst;

import com.myst.GUI.GUI;
import com.myst.audio.Audio;
import com.myst.helper.Timer;
import com.myst.input.Input;
import com.myst.networking.EntityData;
import com.myst.rendering.Shader;
import com.myst.world.entities.Enemy;
import com.myst.world.entities.Entity;
import com.myst.world.lighting.Darkness;
import com.myst.world.lighting.FlashlightOn;
import com.myst.world.lighting.Lighting;
import com.myst.world.view.Camera;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.entities.Player;
import com.myst.world.map.generating.MapGenerator;
import com.myst.world.map.rendering.Tile;

import com.myst.networking.clientside.ClientConnection;

import com.myst.world.map.rendering.TileRenderer;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main {


    public static void setUp() {
        Window.setCallbacks();


        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }
    }


    public static void main(String[] args) {
        setUp();

        Window window = new Window();

        ConcurrentHashMap<String, HashMap<Integer, Entity>> entities = new ConcurrentHashMap<>();
//        only the main can render and create items so this array hands stuff to the main to render
        ConcurrentHashMap<String, HashMap<Integer, EntityData>> toRender = new ConcurrentHashMap<>();

        ClientConnection connection = new ClientConnection(entities, toRender, "127.0.0.1");

        String clientID = "Base2";

        connection.startConnection(clientID);

        window.setFullscreen(false);
        window.createWindow("My game");

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


//        glfwSetWindowPos(window,(videoMode.width() - 640) / 2, (videoMode.height() - 480) / 2);

//        glfwShowWindow(window);

        glClearColor(0f, 0f, 0f, 0f);

        Shader shader = new Shader("assets/shader");

        String[] textures = new String[21];
        String path = ("assets/tile/");
        textures[0] = path + "tile_01";
        textures[1] = path + "tile_02";
        textures[2] = path + "tile_03";
        textures[3] = path + "tile_04";
        textures[4] = path + "tile_05";
        textures[5] = path + "tile_06";
        textures[6] = path + "tile_07";
        textures[7] = path + "tile_08";
        textures[8] = path + "tile_09";
        textures[9] = path + "tile_10";
        textures[10] = path + "tile_11";
        textures[11] = path + "tile_12";
        textures[12] = path + "tile_13";
        textures[13] = path + "tile_14";
        textures[14] = path + "tile_15";
        textures[15] = path + "tile_16";
        textures[16] = path + "tile_17";
        textures[17] = path + "tile_18";
        textures[18] = path + "tile_19";
        textures[19] = path + "tile_20";
        textures[20] = path + "tile_479";

        Tile[][] map = new MapGenerator(textures).generateMap(100, 100);

        TileRenderer tiles = new TileRenderer(textures);

        World world = new World(tiles, map);

        Player player = new Player();

        player.localID = 1;
        player.owner = clientID;


        player.transform.pos.add(new Vector3f(1, -1, 0));

        Camera camera = new Camera(window.getWidth(), window.getHeight());

        entities.put(clientID, new HashMap<Integer, Entity>());

        HashMap<Integer, Entity> myEntities = entities.get(clientID);

        myEntities.put(player.localID, player);

        double frame_cap = 1.0 / 60.0;

        double time = Timer.getTime();
        double unprocessed = 0;

        double frame_time = 0;
        int frames = 0;

        Boolean renderFrame;

        double debugCurrentTime = Timer.getTime();
        double debugLastTime = Timer.getTime();

        camera.bindPlayer(player);

        GUI gui = new GUI(window, window.getInput());

        Darkness dark = new Darkness(window);

        Audio.getAudio().initInput(window.getInput());

        Lighting lights = new Lighting(window.getInput(), window);

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

                window.update();


                camera.updatePosition();
                debugCurrentTime = Timer.getTime();
                double timeSinceLastUpdate = (debugCurrentTime - debugLastTime);
                debugLastTime = debugCurrentTime;

                Audio.getAudio().update();

                player.update((float) timeSinceLastUpdate, window, camera, world);
                lights.update();
                gui.update();


            }

            if (frame_time >= 1) {
                System.out.println(frames);
                frame_time = 0;
                frames = 0;
            }


            if (renderFrame) {
                glClear(GL_COLOR_BUFFER_BIT);

                world.render(shader, camera, window);

                for (String owner : entities.keySet()) {
                    for (Integer entityID : entities.get(owner).keySet()) {
                        entities.get(owner).get(entityID).render(camera);
                    }
                }
                createAndRender(toRender, entities);

                lights.render();
                dark.render();
                gui.render();

                window.swapBuffers();

                frames += 1;

            }

        }

//        clears everything we have used from memory
        glfwTerminate();

//        sloppy and needs tidying
        System.exit(1);
    }


    //    make this render all the objects in the hashmap then set the hasmap to null
    public static void createAndRender(ConcurrentHashMap<String, HashMap<Integer, EntityData>> items, ConcurrentHashMap<String, HashMap<Integer, Entity>> entities) {
        for (String owner : items.keySet()) {
            for (Integer id : items.get(owner).keySet()) {
                EntityData entitiesData = items.get(owner).get(id);
                if (entitiesData != null) {
                    Entity ent = new Enemy();
                    ent.readInEntityData(entitiesData);
                    entities.get(owner).put(id, ent);
                    items.get(owner).put(id, null);
                }
            }
        }
    }
}

