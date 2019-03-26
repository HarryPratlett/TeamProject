/**
 * @author Aled Jackson
 */
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

    GLFWVidMode vidMode;

    /**
     * Sets callbacks for window
     */
    public static void setCallbacks(){
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int errorCode, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

    /**
     * Default constructor with default window size of 640x480
     */
    public Window(){
        setSize(640,480);
        setFullscreen(false);
    }

    /**
     * Creates a new window
     * @param title The title of the window
     */
    public void createWindow(String title){
//        gets information about the monitor
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.vidMode = videoMode;

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
        glfwSetCursorPosCallback(window, input);
    }

    /**
     * Method returns whether or not the window should close
     * @return The boolean whether it should close
     */
    public boolean shouldClose(){
        return glfwWindowShouldClose(window);
    }

    /**
     * Swaps the window buffers
     */
    public void swapBuffers(){
        glfwSwapBuffers(window);
    }

    /**
     * Sets the window to fullscreen
     * @param fullscreen The boolean which decides whether it should be fullscreen
     */
    public void setFullscreen(boolean fullscreen){
        this.fullscreen = fullscreen;
    }

    /**
     * Returns whether or not the window is full screen
     * @return The boolean which is true if the window is full screen and false if it isn't
     */
    public boolean isFullscreen(){
        return fullscreen;
    }

    /**
     * Sets the size of a window
     * @param width Desired width of the window
     * @param height Desired height of the window
     */
    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the width of the window
     * @return The width of the window in int
     */
    public int getWidth(){
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(this.window,width,height);
        this.width = width[0];
        return width[0];
    }

    /**
     * Gets the height of the window
     * @return The height of the window in int
     */
    public int getHeight(){
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(this.window,width,height);
        this.height = height[0];
        return height[0];
    }

    /**
     * Returns the window location in long format for OpenGL
     * @return The long that represents the location
     */
    public long getWindow(){
        return window;
    }

    /**
     * Gets the input for that window
     * @return The input for the window
     */
    public Input getInput(){ return input; }

    /**
     * Updates all inputs e.g. mouse and keyboard
     */
    public void update(){
        input.update();
        glfwPollEvents();
    }
}
