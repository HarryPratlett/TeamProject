package com.myst.world.entities;

import com.myst.audio.Audio;
import com.myst.networking.EntityData;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import com.myst.world.collisions.Line;
import com.myst.world.view.Camera;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static com.myst.world.entities.EntityType.PLAYER;

public class Player extends Entity {
    private ArrayList<Line> bullets;
    private final float MOVEMENT_SPEED = 10f;

    private float maxHealth = 100;
    private float health = 50;

    private long spikeDamageDelay = 1000;
    private long lastSpikeDamage = 0;

//    private static final float[] vertices =
//
//
//    private static final float[] textureFloats =
//
//    private static final int[] indices = ;

    public Player(ArrayList<Line> bullets) {
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
        this.type = PLAYER;
        this.visibleToEnemy = true;
        this.bullets = bullets;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        if (health < 0) health = 0;
        if (health > maxHealth) health = maxHealth;

        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void heal(int heal) {
        setHealth(health + heal);
    }

    public void damage(int dmg) {
        setHealth(health - dmg);
    }

    private boolean canTakeSpikeDamage() {
        return System.currentTimeMillis() - lastSpikeDamage > spikeDamageDelay;
    }

    public void update(float deltaTime, Window window, Camera camera, World world, ConcurrentHashMap<Integer, Entity> items) {
//        these needs fixing and entering into an entities class
//        the entities will then

        boolean moved = false;

        double xMouse = ((window.getInput().getMouseCoordinates()[0] * 2) / window.getWidth()) - 1;
        double yMouse = -(((window.getInput().getMouseCoordinates()[1] * 2) / window.getHeight()) - 1);

        transform.rotation = (float) Math.atan(yMouse / xMouse);

        if (xMouse < 0) {
            transform.rotation += Math.PI;
        }


        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
            transform.pos.add(MOVEMENT_SPEED * deltaTime, 0, 0);
            moved = true;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
            transform.pos.x += -MOVEMENT_SPEED * deltaTime;
            moved = true;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
            transform.pos.y += MOVEMENT_SPEED * deltaTime;
            moved = true;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
            transform.pos.y += -MOVEMENT_SPEED * deltaTime;
            moved = true;
        }
        if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_F)) {
            if (this.lightDistance == 0.25f) {
                this.lightDistance = 2.5f;
                this.visibleToEnemy = true;
            } else {
                this.lightDistance = 0.25f;
                this.visibleToEnemy = false;
            }
        }


        if (window.getInput().isMousePressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Line line = new Line(new Vector2f(transform.pos.x, -transform.pos.y), new Vector2f((float) xMouse, (float) -yMouse));
            bullets.add(line);
            Audio.getAudio().play(Audio.GUN, transform.pos);
            System.out.println("mouse pressed");
        }

        if (moved)
            Audio.getAudio().play(Audio.FOOTSTEPS, transform.pos);
        else
            Audio.getAudio().stop(Audio.FOOTSTEPS);

        //now that the co-ordinate system has been redone this needs redoing
        this.boundingBox.getCentre().set(transform.pos.x, transform.pos.y);

        AABB[] boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
//              30 is the scale and 0.5f is half the width of the box and 2.5 is the scan width
                int x = (int) (transform.pos.x + (0.5f - 2.5 + i));
                int y = (int) (-transform.pos.y + (0.5f - 2.5 + j));
                boxes[i + (j * 5)] = world.getBoundingBox(x, y);
            }
        }

        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                Collision data = boundingBox.getCollision(boxes[i]);
                if (data.isIntersecting) {
                    boundingBox.correctPosition(boxes[i], data);
                    transform.pos.set(boundingBox.getCentre(), 0);
                    boundingBox.getCentre().set(transform.pos.x, transform.pos.y);
                }
            }
        }
    }

    @Override
    public void readInEntityData(EntityData data) {
        super.readInEntityData(data);
        this.health = ((PlayerData) data.typeData).health;
        this.maxHealth = ((PlayerData) data.typeData).maxHealth;
        this.lastSpikeDamage = ((PlayerData) data.typeData).lastSpikeDamage;
    }

    @Override
    public EntityData getData() {
        EntityData data = super.getData();
        PlayerData playerData = new PlayerData();
        playerData.health = health;
        playerData.maxHealth = maxHealth;
        playerData.lastSpikeDamage = lastSpikeDamage;
        data.typeData = playerData;
        return data;
    }
}
