package com.myst;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;

import org.lwjgl.opengl.GL;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main {



    public static void main(String[] args){
        Window.setCallbacks();

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





        System.out.println(new Matrix4f());

        Matrix4f projection = new Matrix4f().ortho2D(640 / 2, -640 / 2, -480 / 2, 480 / 2);

//        projection.rotate(20.5f, 0,0,0);
//        projection.scale(200);
        Matrix4f scale = new Matrix4f()
                .translate(new Vector3f(0,0,0))
                .scale(16);


        Matrix4f target = new Matrix4f();

        projection = projection.mul(scale,target);

        Camera cam = new Camera(window.getWidth(), window.getHeight());

        TileRenderer tiles = new TileRenderer();

        Shader shader = new Shader("assets/shader");

        Texture tex = new Texture("assets/Untitled.png");


        double frame_cap = 1.0/60.0;

        double time = Timer.getTime();
        double unprocessed = 0;

        double frame_time = 0;
        int frames = 0;

        Boolean renderFrame;

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
                else if (window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    cam.addPosition(new Vector3f(1, 0, 0));
                }
            }

            if (frame_time >= 1) {
                System.out.println(frames);
                frame_time = 0;
                frames = 0;
            }



            window.update();

            if (renderFrame) {
                glClear(GL_COLOR_BUFFER_BIT);

//                target = scale;

//            tex.bind();
//                shader.bind();
//                shader.setUniform("sampler", 0);
//                shader.setUniform("projection", cam.getProjection().mul(target));
//                tex.bind(0);
//                model1.render();
                for( int i=0; i < 8 ; i++){
                    tiles.renderTile((byte)0,i,0,shader,scale, cam);
                }

                window.swapBuffers();
                frames += 1;
            }

        }

//        clears everything we have used from memory
        glfwTerminate();
    }
}
