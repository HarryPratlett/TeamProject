package com.myst;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;

import org.lwjgl.opengl.GL;
import org.joml.Matrix4f;

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




        World world = new World();

        Player player = new Player();



        world.setTile(Tile.test_tile2.setSolid(),5,0 );
        world.setTile(Tile.test_tile2.setSolid(),6,0 );


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

        Camera camera = new Camera(window.getWidth(), window.getHeight());

        TileRenderer tiles = new TileRenderer();

        Shader shader = new Shader("assets/shader");




        double frame_cap = 1.0/60.0;

        double time = Timer.getTime();
        double unprocessed = 0;

        double frame_time = 0;
        int frames = 0;

        Boolean renderFrame;

        double debugCurrentTime = Timer.getTime();
        double debugLastTime = Timer.getTime();

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
                if (window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    camera.addPosition(new Vector3f((camera.getWidth() / 2), 0, 0));
                    System.out.println(camera.getWidth() / 2);
                }
                if (window.getInput().isKeyDown(GLFW_KEY_UP)){
                    camera.addPosition(new Vector3f(0,-1,0));
                }
                if (window.getInput().isKeyDown(GLFW_KEY_DOWN)){
                    camera.addPosition(new Vector3f(0,1,0));
                }
                if (window.getInput().isKeyDown(GLFW_KEY_LEFT)){
                    camera.addPosition(new Vector3f(1,0,0));
                }
                if (window.getInput().isKeyDown(GLFW_KEY_RIGHT)){
                    camera.addPosition(new Vector3f(-1,0,0));
                }

                world.correctCamera(camera, window);





            }

            if (frame_time >= 1) {
                System.out.println(frames);
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




                world.render(tiles,shader,camera, window);

                player.render(shader,camera);

                window.swapBuffers();

                frames += 1;

            }

        }

//        clears everything we have used from memory
        glfwTerminate();
    }
}
