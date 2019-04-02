package com.myst.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.myst.world.World;
import com.myst.world.entities.Entity;
import com.myst.world.entities.Player;
import com.myst.world.view.Transform;

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
    public Transform enemyDetection(Entity player, boolean lightOn) {
    	if(player.visibleToEnemy || lightOn) {
    		if((Math.abs(player.transform.pos.x - transform.pos.x) < 2.5f) && (Math.abs(-player.transform.pos.y - (-transform.pos.y)) < 2.5f)) {
    			System.out.println("Found you "+player.transform.pos);
    			return player.transform;
    		}else {       
    			return null;        
    		}       
    	}else {   
    		if((Math.abs(player.transform.pos.x - transform.pos.x) < 0.25f) && (Math.abs(-player.transform.pos.y - (-transform.pos.y)) < 0.25f)) {        
    			System.out.println("Found you");
    			return player.transform;            
    		}else {      
    			return null;           
    		}
    	}      
    }
   


    /**
     * Updates transform
     * @param transform Updated value for transform
     */
    public void updateTransform(Transform transform) {
        this.transform = transform;
    }

}