package com.myst.world.entities;

import com.myst.audio.Audio;
import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import com.myst.world.map.rendering.Shader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player {
    private Model model;

    private AABB boundingBox;
    private Texture texture;
    public Transform transform;
    private final float MOVEMENT_SPEED = 10f;

    public Player(){

        float[] vertices = new float[] {
                -0.5f, 0.5f, 0f, /*0*/  0.5f, 0.5f, 0f, /*1*/    0.5f, -0.5f, 0f, /*2*/
                -0.5f, -0.5f, 0f/*3*/
        };


        float[] texture = new float[] {
                0f, 0f,   1, 0f,  1f, 1f,
                0f, 1f
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };

        model = new Model(vertices, texture, indices);
        this.texture = new Texture("assets/survivor1_hold.png");


        transform = new Transform();
        transform.scale = new Vector3f(1,1,1);
        boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(0.5f,0.5f));
    }

    public void update(float deltaTime, Window window, Camera camera, World world) {
//        these needs fixing and entering into an entities class


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

        // TODO
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_M)) {
            Audio.getAudio().mute();
        }
        // TODO

        //now that the co-ordinate system has been redone this needs redoing
        boundingBox.getCentre().set(transform.pos.x , transform.pos.y );

        AABB[] boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
//              30 is the scale and 0.5f is half the width of the box and 2.5 is the scan width
                int x = (int) (transform.pos.x + (0.5f - 2.5 + i));
                int y = (int) (transform.pos.y + (0.5f - 2.5 + j));
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
    public void render(Shader shader, Camera camera){
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();

    }

    // TODO
    public AABB getBoundingBox() {
        return boundingBox;
    }
    // TODO
}
