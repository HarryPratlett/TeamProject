package com.myst.world.entities;

import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.view.Camera;
import org.joml.Vector2f;

import java.util.concurrent.ConcurrentHashMap;

import static com.myst.world.entities.EntityType.*;

public class Item extends Entity {

    final public static String APPLE = "apple";
    final public static String MED_KIT = "med_kit";
    final public static String INVINCIBILITY_POTION = "invincibility_potion";
    final public static String INFINITE_BULLETS_POTION = "infinite_bullets_potion";
    final public static String SPIKES_HIDDEN = "spikes_hidden";
    final public static String SPIKES_REVEALED = "spikes_revealed";
    final public static String BULLETS_SMALL = "bullets_small";
    final public static String BULLETS_BIG = "bullets_big";
    final public static String LIGHT_TRAP = "healing_platform";

    final private String PATH = "assets/items/";
    final private String PNG = ".png";

    public Item(String itemType) {
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

        if (itemType.equals(APPLE))
            this.type = ITEM_APPLE;
        if (itemType.equals(MED_KIT))
            this.type = ITEM_MED_KIT;
        if (itemType.equals(INVINCIBILITY_POTION))
            this.type = ITEM_INVINCIBILITY_POTION;
        if (itemType.equals(INFINITE_BULLETS_POTION))
            this.type = ITEM_INFINITE_BULLETS_POTION;
        if (itemType.equals(SPIKES_HIDDEN))
            this.type = ITEM_SPIKES_HIDDEN;
        if (itemType.equals(SPIKES_REVEALED))
            this.type = ITEM_SPIKES_REVEALED;
        if (itemType.equals(BULLETS_SMALL))
            this.type = ITEM_BULLETS_SMALL;
        if (itemType.equals(BULLETS_BIG))
            this.type = ITEM_BULLETS_BIG;
        if (itemType.equals(LIGHT_TRAP))
            this.type = ITEM_LIGHT_TRAP;

        this.visibleToEnemy = true;
        this.texture = new Texture(PATH + itemType + PNG);
    }

    @Override
    public void update(float deltaTime, Window window, Camera camera, World world, ConcurrentHashMap<Integer, Entity> items) {

    }
}