package com.myst.world.collisions;

import com.myst.world.entities.Player;
import org.joml.Vector2f;

import java.io.Serializable;

//axis aligned bounding box
public class AABB implements Serializable{
    private Vector2f centre, halfExtent;

    public AABB(Vector2f centre, Vector2f halfExtent){
        this.centre = centre;
        this.halfExtent = halfExtent;
    }


    public Collision getCollision(AABB box2){
        Vector2f distance = box2.centre.sub(centre, new Vector2f());
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);
        distance.sub(halfExtent.add(box2.halfExtent, new Vector2f()));

        return new Collision(distance,distance.x < 0 && distance.y < 0);
    }

    public void correctPosition(AABB box2, Collision data){
        Vector2f correction = box2.centre.sub(centre, new Vector2f());
        if(data.distance.x > data.distance.y){
            if(correction.x > 0){
                centre.add(data.distance.x,0);
            } else{
                centre.add(-data.distance.x,0);
            }
        }else{
            if(correction.y > 0){
                centre.add(0,data.distance.y);
            } else{
                centre.add(0,-data.distance.y);
            }
        }
    }

    public Vector2f getCentre() {
        return centre;
    }

    public Vector2f getHalfExtent(){
        return halfExtent;
    }
}
