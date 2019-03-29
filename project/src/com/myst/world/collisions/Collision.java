package com.myst.world.collisions;

import org.joml.Vector2f;

/**
 * Class that keeps collision properties
 */
public class Collision {
    public Vector2f distance;
    public boolean isIntersecting;
    public Collision(Vector2f distance, boolean intersects){
        this.distance = distance;
        this.isIntersecting = intersects;
    }

}
