package com.myst.world.entities;

import com.myst.audio.Audio;
import com.myst.networking.EntityData;
import com.myst.rendering.Model;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.world.collisions.Bullet;
import com.myst.world.collisions.Line;
import com.myst.world.view.Camera;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import com.myst.world.collisions.Line;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static com.myst.world.entities.EntityType.PLAYER;

public class Player extends Entity {
    private ArrayList<Line> bullets;
    private final float MOVEMENT_SPEED = 10f;


    private float lastHealth = 100;
    private long spikeDamageDelay = 1000;
    private long lastSpikeDamage;
    public float health = 100;
    private float maxHealth = 100;
    private int bulletCount = 30;
    private int maxBulletCount = 100;
    private long lastPlatformHeal;
    private long lastInvincibilityPickup;
    private long lastInfiniteBulletsPickup;
    private boolean isInvincible = false;
    private boolean hasInfiniteBullets = false;
    Texture healthBar;
    float[] baseVertices = new float[]{
            -0.5f, 0.5f, 0f, /*0*/  0.5f, 0.5f, 0f, /*1*/    0.5f, -0.5f, 0f, /*2*/
            -0.5f, -0.5f, 0f/*3*/
    };
    float[] textureDocks = new float[]{
            0f, 0f, 1, 0f, 1f, 1f,
            0f, 1f
    };
    int[] indices = {
            0, 1, 2,
            2, 3, 0
    };
    Transform healthBarTransform;
    Model healthBarFiller = null;
    Texture healthBarFillerTexture;

    Model healthBarModel = null;


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
        this.healthBarModel = new Model(baseVertices, textureDocks,indices);
        this.healthBar = new Texture("assets/sprites/health_bar/0.png");
        this.healthBarTransform = new Transform();
        float healthPercentage = health / maxHealth;
        healthBarFiller = new Model(
                new float[]{
                        -0.5f, 0.5f , 0f, /*0*/  -0.5f+healthPercentage, 0.5f, 0f, /*1*/    -0.5f+healthPercentage, -0.5f, 0f, /*2*/
                        -0.5f, -0.5f, 0f/*3*/
                },textureDocks,indices);
        this.healthBarFillerTexture = new Texture("assets/sprites/health_bar/singlePixel.png");
    }

//    this is when you don't want to render the classes and is only for use of background AI / bot threads
    public Player(){
        super(new Vector2f(0.5f,0.5f));
        this.type = PLAYER;
        this.visibleToEnemy = true;
    }

    public void update(float deltaTime, Window window, Camera camera, World world, ConcurrentHashMap<Integer, Entity> items) {
//        these needs fixing and entering into an entities class
//        the entities will then
        if(lastHealth != health){
            lastHealth = health;
            System.out.println("health has changed");
            float healthPercentage = health / maxHealth;
            System.out.println(healthPercentage);
            this.healthBarFiller = new Model(
                    new float[]{
                            -0.5f, 0.5f , 0f, /*0*/  -0.5f+healthPercentage, 0.5f, 0f, /*1*/    -0.5f+healthPercentage, -0.5f, 0f, /*2*/
                            -0.5f, -0.5f, 0f/*3*/
                    },textureDocks,indices);
        }

        boolean moved = false;

        double xMouse = ((window.getInput().getMouseCoordinates()[0] * 2) / window.getWidth()) - 1;
        double yMouse = -(((window.getInput().getMouseCoordinates()[1] * 2) / window.getHeight()) - 1);

        transform.rotation = (float) Math.atan(yMouse / xMouse);

        if (xMouse < 0) {
            transform.rotation += Math.PI;
        }


        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
            transform.pos.add(MOVEMENT_SPEED * deltaTime, 0, 0);
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
            transform.pos.x += -MOVEMENT_SPEED * deltaTime;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
            transform.pos.y += MOVEMENT_SPEED * deltaTime;
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
            transform.pos.y += -MOVEMENT_SPEED * deltaTime;
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


        if (window.getInput().isMousePressed(GLFW.GLFW_MOUSE_BUTTON_1) && bulletCount > 0) {
            Line line = new Line(new Vector2f(transform.pos.x + 0.5f, transform.pos.y - 0.5f), new Vector2f((float) xMouse, (float) -yMouse));
            bullets.add(line);
            Audio.getAudio().play(Audio.GUN, transform.pos);
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
        readInPlayerData((PlayerData) data.typeData);
    }

    public void readInPlayerData(PlayerData data) {
        this.health = data.health;
        this.maxHealth = data.maxHealth;
        this.bulletCount = data.bulletCount;
        this.maxBulletCount = data.maxBulletCount;
        this.lastSpikeDamage = data.lastSpikeDamage;
        this.lastPlatformHeal = data.lastHealOnLightTrap;
        this.lastInvincibilityPickup = data.lastInvincibilityPickup;
        this.lastInfiniteBulletsPickup = data.lastInfiniteBulletsPickup;
        this.isInvincible = data.isInvincible;
        this.hasInfiniteBullets = data.hasInfiniteBullets;
    }

    @Override
    public EntityData getData() {
        EntityData data = super.getData();
        PlayerData playerData = new PlayerData();
        playerData.health = health;
        playerData.maxHealth = maxHealth;
        playerData.bulletCount = bulletCount;
        playerData.maxBulletCount = maxBulletCount;
        playerData.lastSpikeDamage = lastSpikeDamage;
        playerData.lastHealOnLightTrap = lastPlatformHeal;
        playerData.lastInvincibilityPickup = lastInvincibilityPickup;
        playerData.lastInfiniteBulletsPickup = lastInfiniteBulletsPickup;
        playerData.isInvincible = isInvincible;
        playerData.hasInfiniteBullets = hasInfiniteBullets;
        data.typeData = playerData;
        return data;
    }

    @Override
    public void render(Camera cam, Shader shader){
        super.render(cam,shader);
        shader.bind();
//        healthBarTransform.pos.x = transform.pos.x;
//        healthBarTransform.pos.y = transform.pos.y;
//        shader.setUniform("sampler", 0);
//        shader.setUniform("projection", healthBarTransform.getProjection(cam.getProjection()));
//        healthBar.bind(0);
//        healthBarModel.render();
//        shader.bind();
        healthBarTransform.pos.x = transform.pos.x;
        healthBarTransform.pos.y = transform.pos.y;
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", healthBarTransform.getProjection(cam.getProjection()));
        healthBarFillerTexture.bind(0);
        healthBarFiller.render();
        shader.bind();
    }
}
