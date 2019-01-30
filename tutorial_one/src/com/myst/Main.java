package com.myst;

import com.myst.datatypes.TileCoords;
import com.myst.helper.Timer;
import com.myst.rendering.Texture;
import com.myst.world.view.Camera;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.entities.Player;
import com.myst.world.map.generating.MapGenerator;
import com.myst.world.map.rendering.Shader;
import com.myst.world.map.rendering.Tile;

import com.myst.world.map.rendering.TileRenderer;
import org.lwjgl.opengl.GL;
import org.joml.Matrix4f;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main {



    public static void main(String[] args){
        Window.setCallbacks();

//        AABB box1 = new AABB(new Vector2f(0,0), new Vector2f(1,1));
//        AABB box2 = new AABB(new Vector2f(1,0), new Vector2f(1,1));
//
//        if (box1.isIntersecting(box2)){
//            System.out.println("the boxes are intersecting");
//        }

        if (!glfwInit()){
            throw new IllegalStateException("Failed to initialise GLFW");
        }

        Window window = new Window();

        window.setFullscreen(false);
        window.createWindow("My game");



        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


//        glfwSetWindowPos(window,(videoMode.width() - 640) / 2, (videoMode.height() - 480) / 2);

//        glfwShowWindow(window);

        glClearColor(0f,0f,0f, 0f);

        Shader shader = new Shader("assets/shader");

        Tile test_tile = new Tile(  0,  "assets/tile_18");
        Tile test_tile2 = new Tile(1,"assets/tile_186");

        Tile[] tileSet = new Tile[2];
        tileSet[0] = test_tile;
        tileSet[1] = test_tile2;

        Tile[][] map = new MapGenerator(tileSet).generateMap(100,100);


        TileRenderer tiles = new TileRenderer(map);

        World world = new World(tiles);

        Player player = new Player();

        Camera camera = new Camera(window.getWidth(), window.getHeight());






        world.setTile(test_tile2.setSolid(),5,0 );
        world.setTile(test_tile2.setSolid(),6,0 );


//        world.se
//
//        Matrix4f projection = new Matrix4f().ortho2D(640 / 2, -640 / 2, -480 / 2, 480 / 2);
//
////        projection.rotate(20.5f, 0,0,0);
////        projection.scale(200);
//        Matrix4f scale = new Matrix4f()
//                .translate(new Vector3f(0,0,0))
//                .scale(16);


        Matrix4f target = new Matrix4f();

//        projection = projection.mul(scale,target);






        double frame_cap = 1.0/60.0;

        double time = Timer.getTime();
        double unprocessed = 0;

        double frame_time = 0;
        int frames = 0;

        Boolean renderFrame;

        double debugCurrentTime = Timer.getTime();
        double debugLastTime = Timer.getTime();

        camera.bindPlayer(player);

        while (!window.shouldClose()){
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

                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(window.getWindow(),true);
                }
                camera.updatePosition();





            }

            if (frame_time >= 1) {
                System.out.println(frames);
                System.out.println(camera.position);
                System.out.println(player.transform.pos);
                frame_time = 0;
                frames = 0;
            }


            debugCurrentTime = Timer.getTime();
            double timeSinceLastUpdate = (debugCurrentTime - debugLastTime);
            debugLastTime = debugCurrentTime;

            player.update((float) timeSinceLastUpdate, window, camera, world);

            window.update();

            if (renderFrame) {
                glClear(GL_COLOR_BUFFER_BIT);

//                tiles.renderTile(test_tile,new TileCoords(0,0),shader, new Matrix4f().scale(30),camera);

                world.render(shader,camera, window);


                player.render(shader,camera);

                window.swapBuffers();

                frames += 1;

            }

        }

//        clears everything we have used from memory
        glfwTerminate();
    }
}
