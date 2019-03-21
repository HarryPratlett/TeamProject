
package com.myst.networking.serverside;

import com.myst.networking.EntityData;
import com.myst.networking.serverside.model.WorldModel;
import com.myst.world.collisions.AABB;
import com.myst.world.entities.EntityType;
import com.myst.world.entities.ItemData;
import com.myst.world.map.rendering.Tile;
import com.myst.world.view.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Random;

public class ItemGen {

    static int IDCounter = 0;
    static String clientID = "Item";
    private int appleCount = 16;
    private int healthPotion = 9;

    private WorldModel wm;
    private long lastItemGen;
    private Random r;

    Tile[][] freeTiles;

    public ItemGen(WorldModel wm) {
        this.wm = wm;
        r = new Random();
    }

    public void update() {
        if (System.currentTimeMillis() - lastItemGen > 5 * 60 * 1000) {
            genItems();
            lastItemGen = System.currentTimeMillis();
        }
    }

    public void genItems() {
        System.out.println("MAKING APPLES GO APPLES");
        for (int i = 0; i < wm.map.length; i++) {
            for (int j = 0; j < wm.map[0].length; j++) {
                if (wm.map[i][j].getId() == 0) {
                    if(r.nextInt(50) == 0)
                        genItem(i, j);
                }
            }
        }
    }

    public void genItem(int x, int y) {
        EntityData item = new EntityData();
        item.ownerID = "items";
        item.localID = IDCounter++;

        item.transform = new Transform();
        item.transform.pos.add(x, -y, 0);
        item.transform.scale = new Vector3f(1,1,1);
        item.boundingBox = new AABB(new Vector2f(item.transform.pos.x, item.transform.pos.y), new Vector2f(0.5f, 0.5f));
        item.type = getItemType();

        item.lightSource = false;
        item.lightDistance = 25f;

        ItemData itemData = new ItemData();
        itemData.hidden = false;
        itemData.isChanged = true;
        itemData.spikeTimer = 2;

        item.typeData = itemData;
        item.exists = true;

        wm.updateWorld(item);
    }

    public EntityType getItemType() {
        return EntityType.ITEM_APPLE;
    }

//    public Tile[][] addItemsToWorldMap(Tile[][] map) {
//
//        freeTiles = map;
//
//        for (int i=0; i<freeTiles.length; i++){
//            for (int j=0; j<freeTiles[i].length) {
//                if (i>5 && i<25 && i>40 && i<60 && i>75 && i<95 &&
//                        j>5 && j<25 && j>40 && j<60 && j>75 && j<95) {
//                    if (map[i][j].getId() == 0) {
//                        freeTiles[i][j] = map[i][i];
//                    }
//                }
//            }
//        }
//
//        Tile[][] map = new Tile[width][height];
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                map[x][y] = new Tile(0, textures[0]);
//            }
//        }
//
//        return map;
//    }
//
//    public static void setUp() {
//        Window.setCallbacks();
//        if (!glfwInit()) {
//            throw new IllegalStateException("Failed to initialise GLFW");
//        }
//    }
//
//    public static void main(String[] args) {
//        setUp();
//
//        Window window = new Window();
//
//        ConcurrentHashMap<String, ConcurrentHashMap<Integer, Entity>> entities = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, ConcurrentHashMap<Integer, EntityData>> toRender = new ConcurrentHashMap<>();
//
//        ClientConnection connection = new ClientConnection(entities, toRender, "127.0.0.1");
//        connection.startConnection(clientID);
//
//        window.setFullscreen(false);
//        window.createWindow("ITEM GENERATION");
//
//        GL.createCapabilities();
//
//        glEnable(GL_TEXTURE_2D);
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//
//        entities.put(clientID, new ConcurrentHashMap<Integer, Entity>());
//
//        ConcurrentHashMap<Integer, Entity> myEntities = entities.get(clientID);
//
//
//
//        Item i = genItem(EntityType.ITEM_APPLE, Item.APPLE, 2,-2, false);
//        myEntities.put(i.localID, i);
//
//        Item i2 = genItem(EntityType.ITEM_SPIKES_HIDDEN, Item.SPIKES_HIDDEN, 10,-4, false);
//        myEntities.put(i2.localID, i2);
//
//        Item i3 = genItem(EntityType.ITEM_SPIKES_REVEALED, Item.SPIKES_REVEALED, 10,-4, true);
//        myEntities.put(i3.localID, i3);
//
//        Item i4 = genItem(EntityType.ITEM_APPLE, Item.APPLE, 1,-5, false);
//        myEntities.put(i4.localID, i4);
//
//        Item i5 = genItem(EntityType.ITEM_SPIKES_HIDDEN, Item.SPIKES_HIDDEN, 4,-3, false);
//        myEntities.put(i5.localID, i5);
//
//        Item i6 = genItem(EntityType.ITEM_SPIKES_REVEALED, Item.SPIKES_REVEALED, 4,-3, true);
//        myEntities.put(i6.localID, i6);
//
////        clears everything we have used from memory
//        glfwTerminate();
//    }
//
//    public static Item genItem(EntityType type, String tex, float x, float y, boolean hidden) {
//        Item i = new Item(tex);
//        i.owner = clientID;
//        i.localID = IDCounter++;
//        i.transform.pos.add(x, y, 0);
//        i.hidden = hidden;
//        return i;
//    }
}
