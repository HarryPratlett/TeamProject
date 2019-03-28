package com.myst.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.myst.world.World;
import com.myst.world.entities.Entity;
import com.myst.world.view.Transform;

public class AI {

    private Transform transform;
    private World world;

    public AI(Transform transform, World world) {
        this.transform = transform;
        this.world = world;
    }

    public ArrayList<Vector3f> pathFind(Vector3f goal) {
        if(world.getTile((int)goal.x,(int)goal.y) == null) return null;
        AStarSearch search = new AStarSearch(new Vector3f(transform.pos.x, transform.pos.y, transform.pos.z), goal, world);
        return search.getPath();
    }

    public Transform enemyDetection(ConcurrentHashMap<String,ConcurrentHashMap<Integer, Entity>> entities, boolean lightOn, String botOwner, int botID) {
        for(String owner: entities.keySet()) {
            for(Integer localID: entities.get(owner).keySet()){
                if(owner.equals(botOwner) && localID == botID) continue;
                Transform enemyTransform = entities.get(owner).get(localID).transform;
                if(lightOn || entities.get(owner).get(localID).visibleToEnemy) {
                    if((Math.abs(enemyTransform.pos.x - transform.pos.x) < 2.5f) && (Math.abs(-enemyTransform.pos.y - (-transform.pos.y)) < 2.5f)) {
                        return enemyTransform;
                    }else {
                        return null;
                    }
                }else {
                    if((Math.abs(enemyTransform.pos.x - transform.pos.x) < 0.25f) && (Math.abs(-enemyTransform.pos.y - (-transform.pos.y)) < 0.25f)) {
                        return enemyTransform;
                    }else {
                        return null;
                    }
                }
            }

        }
        return null;
    }

    public void updateTransform(Transform transform) {
        this.transform = transform;
    }

}