package com.myst.world.entities;

import com.myst.rendering.Model;
<<<<<<< HEAD
=======
import com.myst.rendering.Shader;
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
<<<<<<< HEAD
import com.myst.world.map.rendering.Shader;
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Entity {
    protected Model model;
    public Transform transform;
    protected Texture texture;
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
        transform = new Transform();
        transform.scale = new Vector3f(1,1,1);
        boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), boundingBoxCoords);
    }

    public abstract void update(float deltaTime, Window window, Camera camera, World world);

<<<<<<< HEAD
    public void render(Shader shader, Camera camera){
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
=======
    public void render(Camera camera){
        this.shader.bind();
        this.shader.setUniform("sampler", 0);
        this.shader.setUniform("projection", transform.getProjection(camera.getProjection()));
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
        texture.bind(0);
        model.render();

    }

}
