package com.myst.world.entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import com.myst.networking.EntityData;
import com.myst.rendering.Model;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    protected Model model;
    public Transform transform;
    protected Texture texture;
    public AABB boundingBox;
    private Shader shader;
    protected EntityTypes type;
    public String owner;
    public Integer localID;
    private boolean killable;

    public Entity(float[] vertices, float[] texture, int[] indices, Vector2f boundingBoxCoords, Shader shader){
        model = new Model(vertices, texture, indices);
        this.texture = new Texture("assets/survivor1_hold.png");
        killable = true;
        this.shader = shader;
        transform = new Transform();
        transform.scale = new Vector3f(1,1,1);
        boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), boundingBoxCoords);
    }

    public abstract void update(float deltaTime, Window window, Camera camera, World world);

    public void render(Camera camera){
        this.shader.bind();
        this.shader.setUniform("sampler", 0);
        this.shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

//    used in networking to get the entity data
    public EntityData getData(){
        EntityData data = new EntityData();
        data.localID = this.localID;
        data.ownerID = this.owner;
        data.boundingBox = this.boundingBox;
        data.transform = this.transform;
        return data;
    }

    public void readInEntityData(EntityData data){
        this.owner = data.ownerID;
        this.localID = data.localID;
        this.transform = data.transform;
        this.boundingBox = data.boundingBox;
    }
    
    public abstract boolean attack(World world);
    
    public boolean isKillable() {
		return killable;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Model getModel() {
		return model;
	}
	
	public Enum<EntityTypes> getType() {
		return type;
	}
	
	@Override
	public String toString(){
		return owner + " [" + type.toString() + "]";
	}

}
