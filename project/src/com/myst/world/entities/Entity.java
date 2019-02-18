package com.myst.world.entities;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.myst.rendering.Model;
<<<<<<< HEAD
import com.myst.rendering.Shader;
=======
<<<<<<< HEAD
=======
import com.myst.rendering.Shader;
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
<<<<<<< HEAD
=======
<<<<<<< HEAD
import com.myst.world.map.rendering.Shader;
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;

public abstract class Entity {
    protected Model model;
    public Transform transform;
    protected Texture texture;
<<<<<<< HEAD
    public AABB boundingBox;
    private Shader shader;
    protected boolean killable;
    protected Enum<Type> entityType;
    
    enum Type{
    	Bot, Player
    }

    public Entity(float[] vertices, float[] texture, int[] indices, Vector2f boundingBoxCoords, Shader shader){
        model = new Model(vertices, texture, indices);
        this.texture = new Texture("assets/survivor1_hold.png");

        this.shader = shader;
=======
<<<<<<< HEAD
    protected AABB boundingBox;
=======
    public AABB boundingBox;
    private Shader shader;
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2




<<<<<<< HEAD
    public Entity(float[] vertices, float[] texture, int[] indices, Vector2f boundingBoxCoords){
        model = new Model(vertices, texture, indices);
        this.texture = new Texture("assets/survivor1_hold.png");


=======
    public Entity(float[] vertices, float[] texture, int[] indices, Vector2f boundingBoxCoords, Shader shader){
        model = new Model(vertices, texture, indices);
        this.texture = new Texture("assets/survivor1_hold.png");

        this.shader = shader;
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
        transform = new Transform();
        transform.scale = new Vector3f(1,1,1);
        boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), boundingBoxCoords);
    }

    public abstract void update(float deltaTime, Window window, Camera camera, World world);

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
    public void render(Shader shader, Camera camera){
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
<<<<<<< HEAD

=======
=======
    public void render(Camera camera){
        this.shader.bind();
        this.shader.setUniform("sampler", 0);
        this.shader.setUniform("projection", transform.getProjection(camera.getProjection()));
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
        texture.bind(0);
        model.render();

    }
    
    public boolean isKillable() {
		return killable;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Model getModel() {
		return model;
	}
	
	public Enum<Type> getType() {
		return entityType;
	}
	
	@Override
	public String toString(){
		return "[" + entityType.toString() + "]";
	}

}
