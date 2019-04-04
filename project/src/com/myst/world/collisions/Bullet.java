package com.myst.world.collisions;

import com.myst.networking.EntityData;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.entities.BulletData;
import com.myst.world.entities.Entity;
import com.myst.world.view.Camera;
import org.joml.Vector2f;

import java.util.concurrent.ConcurrentHashMap;

import static com.myst.world.entities.EntityType.BULLET;

/**
 * Bullet class, controls damaging entities
 */
public class Bullet extends Entity {
    private static final float MAX_LENGTH = 20;
    private Texture texture;
    private Shader shader;
    private Line line;
    private float length;
    private float damage = 25;
    private float life = 100000000;
    private float timeOfCreation;

    /**
     * Constructor for a new bullet
     * @param line Line bullet follows
     * @param length Length of line
     * @param damage Damage bullet will do
     */
    public Bullet(Line line, float length, float damage){
        super( new float[]{
                -0.1f, 1f * length, 0f, /*0*/  0.1f, 1f * length, 0f, /*1*/    0.1f, 0f, 0f, /*2*/
                -0.1f, 0f, 0f/*3*/
        },new float[] {
                        0f, 0f,   1, 0f,  1f, 1f,
                        0f, 1f
        },
        new int[] {
                0,1,2,
                2,3,0
        },
        new Vector2f(0.5f,0.5f));
        this.length = length;
        this.line = line;
        this.type = BULLET;
        this.lightDistance = 0f;
        this.lightSource = false;
        this.timeOfCreation = System.nanoTime();
        this.texture = new Texture("assets/sprites/player.png");

    }

    /**
     * Constructor for a new bullet
     * @param line Line bullet follows
     * @param length Length of line
     * @param damage Damage bullet will do
     * @param renderable a last parameter which doesn't matter and is purely for the sake of having a constructor that
     *                   doesn't use the OpenGL API
     */

    public Bullet(Line line, float length, float damage, Object renderable){
        super(new Vector2f(0.5f,0.5f));
        this.length = length;
        this.line = line;
        this.type = BULLET;
        this.lightDistance = 0f;
        this.lightSource = false;
        this.timeOfCreation = System.nanoTime();

    }

    public void update(float deltaTime, Window window, Camera camera, World world, ConcurrentHashMap<Integer,Entity> items){

    }

    @Override
    public void readInEntityData(EntityData data) {
        super.readInEntityData(data);
        BulletData bData = (BulletData) data.typeData;
        this.damage = bData.damage;
    }

    /**
     * Renders given bullet
     * @param camera Users camera
     * @param shader Users shader
     */
    @Override
    public void render(Camera camera, Shader shader){
        if((System.nanoTime() - timeOfCreation) < life){
            super.render(camera,shader);
        }
    }

    @Override
    public EntityData getData() {
        EntityData data = super.getData();
        BulletData bData = new BulletData();
        bData.damage = this.damage;
        bData.length = this.length;
        bData.line = this.line;
        data.typeData = bData;
        return data;
    }
}
