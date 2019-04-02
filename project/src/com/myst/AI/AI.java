package com.myst.AI;

import com.myst.world.World;
import com.myst.world.entities.Entity;
import com.myst.world.view.Transform;

import java.util.concurrent.ConcurrentHashMap;

/**
 * AI class - for bots
 */
public class AI {

    private Transform transform;
    private World world;

    /**
     * Default constructor for ai
     * @param transform Transforms AI
     * @param world World AI is in
     */
    public AI(Transform transform, World world) {
        this.transform = transform;
        this.world = world;
    }


    /**
     * Detects enemies nearby to AI
     * @param entities List of all entities
     * @param lightOn Whether other user has flashlight on
     * @param botOwner Owner of bot
     * @param botID ID of bot
     * @return
     */
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

    /**
     * Uupdates transform
     * @param transform Updated value for transform
     */
    public void updateTransform(Transform transform) {
        this.transform = transform;
    }

}