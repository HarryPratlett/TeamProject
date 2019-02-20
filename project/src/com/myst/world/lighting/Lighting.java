package com.myst.world.lighting;

import com.myst.input.Input;
import com.myst.rendering.Window;
import org.lwjgl.glfw.GLFW;

public class Lighting {
    FlashlightOn flashOn;
    FlashlightOff flashOff;
    Input input;
    LightingState state = LightingState.FLASHLIGHT_OFF;

    public Lighting(Input input, Window window){
        this.input = input;
        flashOn = new FlashlightOn(window);
        flashOff = new FlashlightOff(window);
    }

    public void render(){
        switch(state){
            case FLASHLIGHT_ON:
                flashOn.render();
                break;
            case FLASHLIGHT_OFF:
                flashOff.render();
                break;
        }
    }

    public void update(){
        switch(state){
            case FLASHLIGHT_ON:
                if(input.isKeyPressed(GLFW.GLFW_KEY_F)){
                    state = LightingState.FLASHLIGHT_OFF;
                    System.out.println("I got here on");
                }

                break;
            case FLASHLIGHT_OFF:
                if(input.isKeyPressed(GLFW.GLFW_KEY_F)){
                    state = LightingState.FLASHLIGHT_ON;
                    System.out.println("I got here off");
                }

                break;
        }
    }


}
