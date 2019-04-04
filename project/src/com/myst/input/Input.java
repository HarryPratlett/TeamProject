/**
 * @author Aled Jackson, Harry Pratlett
 */
package com.myst.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Creates input listeners for a client
 */
public class Input extends GLFWCursorPosCallback {

    private long window;

    private boolean[] keys;

    private boolean[] buttons;

    private double[] coordinates = new double[] {0, 0};

    /**
     * Creates a new input listener for a window
     * @param window The window which it will be created for
     */
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

    /**
     * Mouse listener
     * @param window Window which the listener is for
     * @param xpos X co-ordinate for the mouse
     * @param ypos Y co-ordinate for the mouse
     */
    public void invoke(long window, double xpos, double ypos)   {
        coordinates = new double[] {xpos, ypos};
    }

    /**
     * Gets the mouse co-ordinates
     * @return Returns the mouse co-ordinates as an array double
     */
    public double[] getMouseCoordinates() {
        return coordinates;
    }

    /**
     * Returns if a given key is down
     * @param key The given key e.g 'A'
     * @return Returns a boolean if the key is down = true
     */
    public boolean isKeyDown(int key)  {
        return glfwGetKey(window, key) == 1;
    }

    /**
     * Returns if a key is pressed down and let go i.e. pressed
     * @param key The given key e.g 'B'
     * @return Returns a boolean which is true if it has false if it has not
     */
    public boolean isKeyPressed(int key){
        return (isKeyDown(key) && !keys[key]);
    }

    /**
     * Returns whether a key has been released
     * @param key The given key e.g. 'C'
     * @return Returns a boolean which is true if the key has been released, false if it has not
     */
    public boolean isKeyReleased(int key){
        return (!isKeyDown(key) && keys[key]);
    }

    /**
     * Returns if a mouse button is down
     * @param button The given mouse button: e.g. Mouse_Button_1
     * @return Returns a boolean which is true if the button is down
     */
    public boolean isMouseButtonDown(int button){
        return glfwGetMouseButton(window, button) == 1;
    }

    /**
     * Returns if a mouse button has been pressed and let go
     * @param button The given mouse button
     * @return A boolean which is true is a mouse button is pressed
     */
    public boolean isMousePressed(int button){
        return (isMouseButtonDown(button) && !buttons[button]);
    }

    /**
     * Updates the given inputs
     */
    public void update(){
        for (int i =32; i < GLFW_KEY_LAST; i++){
            keys[i] = isKeyDown(i);
        }
        for (int i=0; i < GLFW_MOUSE_BUTTON_LAST; i++){
            buttons[i] = isMouseButtonDown(i);
        }
    }
}
