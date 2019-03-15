package com.myst.input;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;


public class Input extends GLFWCursorPosCallback {

    private long window;

    private boolean keys[];

    private boolean buttons[];

    private double[] coordinates = new double[] {0, 0};


    public Input(long window){
        this.window = window;
        this.keys = new boolean[GLFW_KEY_LAST];
        for (int i =0; i < GLFW_KEY_LAST; i++){
            keys[i] = false;
        }
        this.buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
        for (int i =0; i < GLFW_MOUSE_BUTTON_LAST; i++){
            buttons[i] = false;
        }
    }

    public void invoke(long window, double xpos, double ypos)   {
        coordinates = new double[] {xpos, ypos};
    }

    public double[] getMouseCoordinates() {
        return coordinates;
    }

    public boolean isKeyDown(int key)  {
        return glfwGetKey(window, key) == 1;
    }

    public boolean isKeyPressed(int key){
        return (isKeyDown(key) && !keys[key]);
    }

    public boolean isKeyReleased(int key){
        return (!isKeyDown(key) && keys[key]);
    }

    public boolean isMouseButtonDown(int button){
        return glfwGetMouseButton(window, button) == 1;
    }

    public boolean isMousePressed(int button){
        return (isMouseButtonDown(button) && !buttons[button]);
    }


    public void update(){
        for (int i =32; i < GLFW_KEY_LAST; i++){
            keys[i] = isKeyDown(i);
        }
        for (int i=0; i < GLFW_MOUSE_BUTTON_LAST; i++){
            buttons[i] = isMouseButtonDown(i);
        }
    }
}
