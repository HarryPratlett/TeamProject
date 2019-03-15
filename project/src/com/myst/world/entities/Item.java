package com.myst.world.entities;

import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.view.Camera;
import org.joml.Vector2f;

import java.util.concurrent.ConcurrentHashMap;

import static com.myst.world.entities.EntityType.ITEM_APPLE;
import static com.myst.world.entities.EntityType.ITEM_SPIKES_HIDDEN;
import static com.myst.world.entities.EntityType.ITEM_SPIKES_REVEALED;

public class Item extends Entity {

    final public static String APPLE = "apple";
    final public static String SPIKES_HIDDEN = "spikes_hidden";
    final public static String SPIKES_REVEALED = "spikes_revealed";

    final private String PATH = "assets/items/";
    final private String PNG = ".png";

    public Item(String t) {
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

            if (t.equals(APPLE))
                this.type = ITEM_APPLE;
            if (t.equals(SPIKES_HIDDEN))
                this.type = ITEM_SPIKES_HIDDEN;
            if (t.equals(SPIKES_REVEALED))
                this.type = ITEM_SPIKES_REVEALED;

            this.visibleToEnemy = true;
            this.texture = new Texture(PATH + t + PNG);
        }


    @Override
    public void update(float deltaTime, Window window, Camera camera, World world, ConcurrentHashMap<Integer,Entity> items) {

    }


}