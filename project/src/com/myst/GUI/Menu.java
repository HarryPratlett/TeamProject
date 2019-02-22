package com.myst.GUI;

import com.myst.input.Input;
import com.myst.rendering.Shader;
import com.myst.rendering.Window;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class Menu {
    private HashMap<Rectangle2D.Float, String> menuButtons = new HashMap<>();

    private final float[] baseVertices = new float[] {
            -1f, 0.5f, 0f, /*0*/  1f, 0.5f, 0f, /*1*/    1f, -0.5f, 0f, /*2*/
            -1f, -0.5f, 0f/*3*/
    };

    private final float[] textureDocks = new float[] {
            0f, 0f,   1, 0f,  1f, 1f,
            0f, 1f
    };

    private final int[] indices = new int[] {
            0,1,2,
            2,3,0
    };

    private Window window;
    private Shader shader;
    private Input input;

    public Menu(Window window, Input input)   {
        this.window = window;
        this.shader = new Shader ("assets/shader");
        this.input = input;
    }

    public void renderMenu()    {

    }
}
