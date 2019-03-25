
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

public class ItemGenerator {

    static int IDCounter = 0;
    private boolean hasSpawnedItems= false;

    private WorldModel wm;
    private long lastItemGen;
    private Random r;

    public ItemGenerator(WorldModel wm) {
        this.wm = wm;
        r = new Random();
    }

    public void update() {
        if (System.currentTimeMillis() - lastItemGen > 3 * 60 * 1000) {
            genItems();
            lastItemGen = System.currentTimeMillis();
        }
    }

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
                    addInvincibilityPotion(x, y);
                    break;
            }
        }
    }

    public void addApple(int x, int y) {
        EntityData apple = createBaseItem(x, y, false);
        apple.type = EntityType.ITEM_APPLE;
        wm.updateWorld(apple);
    }

    public void addMedKit(int x, int y) {
        EntityData medKit = createBaseItem(x, y, false);
        medKit.type = EntityType.ITEM_MED_KIT;
        wm.updateWorld(medKit);
    }

    public void addSpikes(int x, int y) {
        EntityData spikesHidden = createBaseItem(x, y, false);
        spikesHidden.type = EntityType.ITEM_SPIKES_HIDDEN;

        EntityData spikesRevealed = createBaseItem(x, y, true);
        spikesRevealed.type = EntityType.ITEM_SPIKES_REVEALED;

        wm.updateWorld(spikesHidden);
        wm.updateWorld(spikesRevealed);
    }

    public void addBulletsSmall(int x, int y) {
        EntityData bulletsSmall = createBaseItem(x, y, false);
        bulletsSmall.type = EntityType.ITEM_BULLETS_SMALL;
        wm.updateWorld(bulletsSmall);
    }

    public void addBulletsBig(int x, int y) {
        EntityData bulletsBig = createBaseItem(x, y, false);
        bulletsBig.type = EntityType.ITEM_BULLETS_BIG;
        wm.updateWorld(bulletsBig);
    }

    public void addInvincibilityPotion(int x, int y) {
        EntityData invincibilityPotion = createBaseItem(x, y, false);
        invincibilityPotion.type = EntityType.ITEM_INVINCIBILITY_POTION;
        wm.updateWorld(invincibilityPotion);
    }

    public void addHealingPlatform(int x, int y) {
        EntityData healingPlatform = createBaseItem(x, y, false);
        healingPlatform.type = EntityType.ITEM_HEALING_PLATFORM;
        healingPlatform.lightSource = true;
        healingPlatform.lightDistance = 2.5f;
        wm.updateWorld(healingPlatform);
    }

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
