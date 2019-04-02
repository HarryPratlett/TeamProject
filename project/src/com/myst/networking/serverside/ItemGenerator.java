package com.myst.networking.serverside;

import com.myst.networking.EntityData;
import com.myst.networking.serverside.model.WorldModel;
import com.myst.world.collisions.AABB;
import com.myst.world.entities.EntityType;
import com.myst.world.entities.ItemData;
import com.myst.world.view.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Random;

/**
 * Class for generating items and traps
 */
public class ItemGenerator {

    static int IDCounter = 0;
    private boolean hasSpawnedItems= false;
    private long lastItemGen;
    private WorldModel wm;
    private Random r;

    /**
     * ItemGenerator constructor
     * @param wm The world model
     */
    public ItemGenerator(WorldModel wm) {
        this.wm = wm;
        r = new Random();
        genLightTraps();
    }

    /**
     * Generating new items every 3 minutes
     */
    public void update() {
        if (System.currentTimeMillis() - lastItemGen > 3 * 60 * 1000) {
            genItems();
            lastItemGen = System.currentTimeMillis();
        }
    }

    /**
     * Generating light traps, some healing
     */
    public void genLightTraps() {
        int healCount = 0;
        int minX = wm.map.length / 5;
        int maxX = minX * 4;
        int minY = wm.map[0].length / 5;
        int maxY = minY * 4;

        for(int i = 0; i < 10; i++) {
            int x = 0, y = 0;
            boolean heals = false;

            while(wm.map[x][y].getId() != 0) {
                x = (int) (r.nextFloat() * (maxX - minX) + minX);
                y = (int) (r.nextFloat() * (maxY - minY) + minY);
            }

            if (healCount < 3) {
                heals = true;
                healCount++;
            }

            addLightTrap(x, y, heals);
        }
    }

    /**
     * Generating items randomly on free locations on the map
     */
    public void genItems() {
        for (int i = 0; i < wm.map.length; i++) {
            for (int j = 0; j < wm.map[0].length; j++) {
                if (wm.map[i][j].getId() == 0) {
                    genItem(i, j);
                }
            }
        }
        hasSpawnedItems = true;
    }

    /**
     * Generating items
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void genItem(int x, int y) {
        int a = r.nextInt(100);
        if(a == 0) {
            int b = r.nextInt(10);
            switch(b) {
                case 0:
                    addMedKit(x, y);
                    break;
                case 1:
                case 2:
                    addApple(x, y);
                    break;
                case 3:
                case 4:
                    if (hasSpawnedItems) addApple(x, y);
                    else addSpikes(x, y);
                    break;
                case 5:
                case 6:
                    if (hasSpawnedItems) addBulletsSmall(x, y);
                    else addSpikes(x, y);
                    break;
                case 7:
                    addBulletsSmall(x, y);
                    break;
                case 8:
                    addBulletsBig(x, y);
                    break;
                case 9:
                    if(r.nextInt(2) == 0) addInvincibilityPotion(x, y);
                    else addInfiniteBulletsPotion(x, y);
                    break;
            }
        }
    }

    /**
     * Generating an apple
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void addApple(int x, int y) {
        EntityData apple = createBaseItem(x, y, false);
        apple.type = EntityType.ITEM_APPLE;
        wm.updateWorld(apple);
    }

    /**
     * Generating a med kit
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void addMedKit(int x, int y) {
        EntityData medKit = createBaseItem(x, y, false);
        medKit.type = EntityType.ITEM_MED_KIT;
        wm.updateWorld(medKit);
    }

    /**
     * Generating spikes (both hidden and revealed for the animation)
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void addSpikes(int x, int y) {
        EntityData spikesHidden = createBaseItem(x, y, false);
        spikesHidden.type = EntityType.ITEM_SPIKES_HIDDEN;

        EntityData spikesRevealed = createBaseItem(x, y, true);
        spikesRevealed.type = EntityType.ITEM_SPIKES_REVEALED;

        wm.updateWorld(spikesHidden);
        wm.updateWorld(spikesRevealed);
    }

    /**
     * Generating a small bullets pack
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void addBulletsSmall(int x, int y) {
        EntityData bulletsSmall = createBaseItem(x, y, false);
        bulletsSmall.type = EntityType.ITEM_BULLETS_SMALL;
        wm.updateWorld(bulletsSmall);
    }

    /**
     * Generating a large bullets pack
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void addBulletsBig(int x, int y) {
        EntityData bulletsBig = createBaseItem(x, y, false);
        bulletsBig.type = EntityType.ITEM_BULLETS_BIG;
        wm.updateWorld(bulletsBig);
    }

    /**
     * Generating an invincibility potion
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void addInvincibilityPotion(int x, int y) {
        EntityData invincibilityPotion = createBaseItem(x, y, false);
        invincibilityPotion.type = EntityType.ITEM_INVINCIBILITY_POTION;
        wm.updateWorld(invincibilityPotion);
    }

    /**
     * Generating an infinite bullets potion
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void addInfiniteBulletsPotion(int x, int y) {
        EntityData infiniteBulletsPotion = createBaseItem(x, y, false);
        infiniteBulletsPotion.type = EntityType.ITEM_INFINITE_BULLETS_POTION;
        wm.updateWorld(infiniteBulletsPotion);
    }

    /**
     * Generating a light trap
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     */
    public void addLightTrap(int x, int y, boolean heals) {
        EntityData lightTrap = createBaseItem(x, y, false);
        lightTrap.type = EntityType.ITEM_LIGHT_TRAP;
        lightTrap.lightSource = true;
        lightTrap.lightDistance = 2.5f;
        ((ItemData) lightTrap.typeData).isHealingLightTrap = heals;
        wm.updateWorld(lightTrap);
    }

    /**
     * Generating an item with the base item properties
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     * @param hidden True if the item is being rendered on the map, false if hidden
     * @return The item with base properties
     */
    public EntityData createBaseItem(int x, int y, boolean hidden) {
        EntityData item = new EntityData();
        item.ownerID = "items";
        item.localID = IDCounter++;

        item.transform = new Transform();
        item.transform.pos.add(x, -y, 0);
        item.transform.scale = new Vector3f(1,1,1);
        item.boundingBox = new AABB(new Vector2f(item.transform.pos.x, item.transform.pos.y), new Vector2f(0.5f, 0.5f));

        ItemData itemData = new ItemData();
        itemData.hidden = hidden;
        itemData.isChanged = true;

        item.typeData = itemData;
        item.exists = true;

        return item;
    }
}
