
package com.myst.networking.serverside;

import com.myst.GUI.GUI;
import com.myst.audio.Audio;
import com.myst.helper.Timer;
import com.myst.networking.EntityData;
import com.myst.networking.clientside.ClientConnection;
import com.myst.rendering.Shader;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.Bullet;
import com.myst.world.collisions.Line;
import com.myst.world.entities.Enemy;
import com.myst.world.entities.Entity;
import com.myst.world.entities.Item;
import com.myst.world.entities.Player;
import com.myst.world.lighting.Darkness;
import com.myst.world.map.generating.MapGenerator;
import com.myst.world.map.rendering.Tile;
import com.myst.world.map.rendering.TileRenderer;
import com.myst.world.view.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;

public class ItemGen {

    static int IDCounter = 0;
    static String clientID = "items";

    public static void setUp() {
        Window.setCallbacks();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }
    }

    public static void main(String[] args) {
        setUp();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        System.out.println(width);
        System.out.println(height);

        Window window = new Window();

        ConcurrentHashMap<String, ConcurrentHashMap<Integer, Entity>> entities = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, EntityData>> toRender = new ConcurrentHashMap<>();

        ClientConnection connection = new ClientConnection(entities, toRender, "127.0.0.1");
        connection.startConnection(clientID);

        window.setFullscreen(false);
        window.createWindow("My game");

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

//        Shader environmentShader = new Shader("assets/shader");
//        Shader menuShader = new Shader("assets/shader2");

//        String[] textures = new String[21];
//        String path = ("assets/tile/");
//        textures[0] = path + "tile_01";
//        textures[1] = path + "tile_02";
//        textures[2] = path + "tile_03";
//        textures[3] = path + "tile_04";
//        textures[4] = path + "tile_05";
//        textures[5] = path + "tile_06";
//        textures[6] = path + "tile_07";
//        textures[7] = path + "tile_08";
//        textures[8] = path + "tile_09";
//        textures[9] = path + "tile_10";
//        textures[10] = path + "tile_11";
//        textures[11] = path + "tile_12";
//        textures[12] = path + "tile_13";
//        textures[13] = path + "tile_14";
//        textures[14] = path + "tile_15";
//        textures[15] = path + "tile_16";
//        textures[16] = path + "tile_17";
//        textures[17] = path + "tile_18";
//        textures[18] = path + "tile_19";
//        textures[19] = path + "tile_20";
//        textures[20] = path + "tile_479";
//
//        Tile[][] map = new MapGenerator(textures).generateMap(100, 100);
//
//        TileRenderer tiles = new TileRenderer(textures);
//
//        World world = new World(tiles, map);
//
//        Player player = new Player(playerBullets);
//        player.lightSource = true;
//
//        player.transform.pos.add(new Vector3f(1, -1, 0));
//
//        player.localID = IDCounter;
//        player.owner = clientID;
//
//        IDCounter++;
//        Camera camera = new Camera(window.getWidth(), window.getHeight());
//
        entities.put(clientID, new ConcurrentHashMap<Integer, Entity>());

        ConcurrentHashMap<Integer, Entity> myEntities = entities.get(clientID);

        Item item = new Item();
        item.owner = clientID;
        item.localID = IDCounter++;
        item.transform.pos.add(1, -4, 0);
        myEntities.put(44, item);

        Item item2 = new Item();
        item2.owner = clientID;
        item2.localID = IDCounter++;
        item2.transform.pos.add(3, -4, 0);
        myEntities.put(55, item2);

        System.out.println("hi");

//        if(!someoneConnected) {
//            // add items here
//            clientTable.addClient("items");
//            BlockingQueue<Object> itemQueue = clientTable.getQueue("items");
//
//            ArrayList<EntityData> items = new ArrayList<EntityData>();
//
//            Item item = new Item();
//            item.transform.pos.add(1, -2, 0);
//
//            items.add(new EntityData("items", -1, item.transform, item.boundingBox, item.getType(), 0, item.lightSource, item.lightDistance));
//
//            for (int i = 0; i < items.size(); i++) {
//                world.updateWorld(items.get(i));
//            }
//
//            someoneConnected = true;
//        }

//        Lighting lights = new Lighting(window.getInput(), window);

//        Item item = new Item();
//        item.transform.pos.x = player.transform.pos.x + 2;
//        item.transform.pos.y = player.transform.pos.y - 1;
//        myEntities.put(9999, item);

//        while (!window.shouldClose()) {
//
//            renderFrame = false;
//
//            double time2 = Timer.getTime();
//            double deltaTime = time2 - time;
//            time = time2;
//
//            unprocessed += deltaTime;
//            frame_time += deltaTime;
//
////            in the case you want to render a frame as you have gone over the frame_cap
////            a while is used instead of an if incase the performance is less than 30 FPS
//            while (unprocessed >= frame_cap) {
////                look into effects of containing a thread.sleep();
////                take away the frame cap so that you account for the time you've taken of the next frame
//                unprocessed -= frame_cap;
//
//                renderFrame = true;
//
//                window.update();
//
//                camera.updatePosition();
//                debugCurrentTime = Timer.getTime();
//                double timeSinceLastUpdate = (debugCurrentTime - debugLastTime);
//                debugLastTime = debugCurrentTime;
//
//                Audio.getAudio().update();
//                player.update((float) timeSinceLastUpdate, window, camera, world);
//
//                gui.update();
//                calculateBullets(myEntities, playerBullets, map);
//                playerBullets.clear();
//            }
//
//            if (frame_time >= 1) {
//                System.out.println(frames);
//                frame_time = 0;
//                frames = 0;
//            }
//
//            if (renderFrame) {
//                glClear(GL_COLOR_BUFFER_BIT);
//
//                calculateLighting(entities, camera, environmentShader, window);
//                world.render(environmentShader, camera, window);
//
//                for (String owner : entities.keySet()) {
//                    for (Integer entityID : entities.get(owner).keySet()) {
//                        entities.get(owner).get(entityID).render(camera, environmentShader);
//                    }
//                }
//
//                createAndRender(toRender, entities);
//
//                gui.render(menuShader);
//
//                window.swapBuffers();
//
//                frames += 1;
//            }
//        }

//        clears everything we have used from memory
        glfwTerminate();
    }
}
