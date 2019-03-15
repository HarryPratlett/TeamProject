
package com.myst.networking.serverside;

import com.myst.networking.EntityData;
import com.myst.networking.clientside.ClientConnection;
import com.myst.rendering.Window;
import com.myst.world.entities.*;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;

public class ItemGen {

    static int IDCounter = 0;
    static String clientID = "Item";

    public static void setUp() {
        Window.setCallbacks();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }
    }

    public static void main(String[] args) {
        setUp();

        Window window = new Window();

        ConcurrentHashMap<String, ConcurrentHashMap<Integer, Entity>> entities = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, EntityData>> toRender = new ConcurrentHashMap<>();

        ClientConnection connection = new ClientConnection(entities, toRender, "127.0.0.1");
        connection.startConnection(clientID);

        window.setFullscreen(false);
        window.createWindow("ITEM GENERATION");

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        entities.put(clientID, new ConcurrentHashMap<Integer, Entity>());

        ConcurrentHashMap<Integer, Entity> myEntities = entities.get(clientID);

        Item i = genItem(EntityType.ITEM_APPLE, Item.APPLE, 2,-2, false);
        myEntities.put(i.localID, i);

        Item i2 = genItem(EntityType.ITEM_SPIKES_HIDDEN, Item.SPIKES_HIDDEN, 3,-4, false);
        myEntities.put(i2.localID, i2);

        Item i3 = genItem(EntityType.ITEM_SPIKES_REVEALED, Item.SPIKES_REVEALED, 3,-4, true);
        myEntities.put(i3.localID, i3);

        Item i4 = genItem(EntityType.ITEM_APPLE, Item.APPLE, 1,-5, false);
        myEntities.put(i4.localID, i4);

        Item i5 = genItem(EntityType.ITEM_SPIKES_HIDDEN, Item.SPIKES_HIDDEN, 4,-3, false);
        myEntities.put(i5.localID, i5);

        Item i6 = genItem(EntityType.ITEM_SPIKES_REVEALED, Item.SPIKES_REVEALED, 4,-3, true);
        myEntities.put(i6.localID, i6);

//        clears everything we have used from memory
        glfwTerminate();
    }

    public static Item genItem(EntityType type, String tex, float x, float y, boolean hidden) {
        Item i = new Item(tex);
        i.owner = clientID;
        i.localID = IDCounter++;
        i.transform.pos.add(x, y, 0);
        i.hidden = hidden;
        return i;
    }
}
