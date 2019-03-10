package com.myst.world.entities;

import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.view.Camera;
import org.joml.Vector2f;

import java.io.File;

import static com.myst.world.entities.EntityTypes.ITEM;

public class Item extends Entity {

    final public String TEST = "tile_444.png";
    final public String HEALTH = "health";
    final public String COIN = "coin";

    final private String PATH = ("assets/items/");
    final private String PNG = (".png");

    //private File health = new File(PATH + HEALTH + PNG);
    //private File coin = new File(PATH + COIN + PNG);

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
        }

    @Override
    public void update(float deltaTime, Window window, Camera camera, World world) {

    }
}
