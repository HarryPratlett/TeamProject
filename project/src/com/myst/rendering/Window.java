package com.myst.rendering;

import com.myst.input.Input;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;

    private Input input;
    private int width, height;

    private boolean fullscreen;


    public static void setCallbacks(){
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int errorCode, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

    public Window(){
        setSize(640,480);
        setFullscreen(false);
    }

    public void createWindow(String title){
//        gets information about the monitor
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        window = glfwCreateWindow(width,height,title,fullscreen ? glfwGetPrimaryMonitor() : 0,0);
        if (window == 0){
            throw new IllegalStateException("Failed to create window");
        }
        if (!fullscreen) {
            glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
            glfwShowWindow(window);
        }
        glfwMakeContextCurrent(window);
        input = new Input(window);
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers(){
        glfwSwapBuffers(window);
    }

    public void setFullscreen(boolean fullscreen){
        this.fullscreen = fullscreen;
    }

    public boolean isFullscreen(){
        return fullscreen;
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }

    public long getWindow(){
        return window;
    }

    public Input getInput(){ return input; }

    public void update(){
        input.update();
        glfwPollEvents();
    }
}
