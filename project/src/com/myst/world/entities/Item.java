package com.myst.world.entities;

import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.view.Camera;
import org.joml.Vector2f;

import java.io.File;

import static com.myst.world.entities.EntityTypes.ITEM;

public class Item extends Entity {


    final public String HEALTH_UP = "apple";
    final public String HEALTH_DOWN = "healthDown";

    final private String PATH = ("assets/items/");
    final private String PNG = (".png");


    private File healthUp = new File(PATH + HEALTH_UP + PNG);
    private File healthDown = new File(PATH + HEALTH_DOWN + PNG);

    public Item() {
        super(new float[]{
                        -0.5f, 0.5f, 0f, /*0*/  0.5f, 0.5f, 0f, /*1*/    0.5f, -0.5f, 0f, /*2*/
                        -0.5f, -0.5f, 0f/*3*/
                },
                new float[]{
                        0f, 0f, 1, 0f, 1f, 1f,
                        0f, 1f
                },
                new int[]{
                        0, 1, 2,
                        2, 3, 0
                },
                new Vector2f(0.5f, 0.5f));
        this.type = ITEM;
        this.visibleToEnemy = true;
        this.texture = new Texture(PATH + HEALTH_UP + PNG);
        }

    @Override
    public void update(float deltaTime, Window window, Camera camera, World world) {

    }

}
