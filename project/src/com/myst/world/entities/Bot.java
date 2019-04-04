package com.myst.world.entities;

import com.myst.AI.AI;
import com.myst.AI.AStarSearch;
import com.myst.audio.Audio;
import com.myst.networking.EntityData;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Collision;
import com.myst.world.collisions.Line;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements AI code to create bots
 */
public class Bot extends Entity {

    private AI intelligence;
    private final float MOVEMENT_SPEED = 1f;
    private ArrayList<Vector3f> path;
    private ArrayList<Line> bullets;
    private Random randInt = null;
    public float health = 50;
    private float maxHealth = 50;
    private long lastSpikeDamage = 0;
    private long timeToReRoute;
    private long lastReRoute;
    private Stack<int[]> route = null;
    private int[] nextLocation;
    public long lastMove = System.currentTimeMillis();
    public long moveTime = 50;
    private AStarSearch search;

    private boolean moved;

    private long spikeDamageDelay = 1000;

    /**
     * Constructor for a bot
     * @param boundingBoxCoords Co-ordinates of bounding box
     * @param bullets Given bullets for bot
     * @param search Starts a search for nearest players/enemies
     */
    public Bot(Vector2f boundingBoxCoords, ArrayList<Line> bullets, AStarSearch search) {
        super(boundingBoxCoords);
        type = EntityType.PLAYER;
        this.visibleToEnemy = true;
        this.bullets = bullets;
        this.exists = true;
        this.isBot = true;
        this.search = search;

    }


    /**
     * Initialises the AI within the world
     * @param world World AI is in
     */
    public void initialiseAI(World world) {
        intelligence = new AI(transform, world);
    }

    public void updateBot(float deltaTime, World world, Entity player) {
        this.boundingBox.getCentre().set(transform.pos.x, transform.pos.y);
        //add enemy detection method here, so constantly checks for enemies as well as randomly turning on flashlight.

        if (player != null) {
            float distToPlayer = this.transform.pos.distance(player.transform.pos);
            timeToReRoute = distPlayerRouteTime(distToPlayer);
            if (route == null) {
                route = search.findRoute(new int[]{(int) this.transform.pos.x, (int) -this.transform.pos.y},
                        new int[]{(int) player.transform.pos.x, (int) -player.transform.pos.y});
                lastReRoute = System.currentTimeMillis();
                if (!route.isEmpty()) {
                    nextLocation = route.pop();
                }
            } else if ((System.currentTimeMillis() - lastReRoute) >= timeToReRoute || route.isEmpty()) {
                route = search.findRoute(new int[]{(int) this.transform.pos.x, (int) -this.transform.pos.y},
                        new int[]{(int) player.transform.pos.x, (int) -player.transform.pos.y});
                lastReRoute = System.currentTimeMillis();
                if (!route.isEmpty()) {
                    nextLocation = route.pop();
                }
            }
        }


        if(System.currentTimeMillis() - lastMove > 1 && nextLocation != null){
            
        	this.transform.pos.x = nextLocation[0];
        	this.transform.pos.y = -nextLocation[1];
        	Audio.getAudio().play(Audio.FOOTSTEPS, this.transform.pos);
        	Audio.getAudio().stop(Audio.FOOTSTEPS);
            lastMove = System.currentTimeMillis();
            if(route.isEmpty()){
                nextLocation = null;
            }else{
                nextLocation = route.pop();
            }

        }
        if(player != null) {
        	Transform enemyTransform  = intelligence.enemyDetection(player, visibleToEnemy);
        	if(enemyTransform != null) {
        		/*if(enemyTransform.pos.y > this.transform.pos.y) {
        			this.transform.rotation += 0.1f;
        		}else {
        			this.transform.rotation -= 0.1f;
        		}*/
        		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//        		Line line = new Line(new Vector2f(transform.pos.x + 0.5f, transform.pos.y - 0.5f), new Vector2f((float) enemyTransform.pos.x, (float) -enemyTransform.pos.y));
//        		bullets.add(line);
                Audio.getAudio().play(Audio.GUN, this.transform.pos);
                
        	}
        }
        AABB[] boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
//	           30 is the scale and 0.5f is half the width of the box and 2.5 is the scan width
                int x = (int) (transform.pos.x + (0.5f - 2.5 + i));
                int y = (int) (-transform.pos.y + (0.5f - 2.5 + j));
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

    public static long distPlayerRouteTime(float distance){
        return (long) (1000 * Math.pow(2,distance));
    }

    @Override
    public EntityData getData() {
        EntityData data = super.getData();
        PlayerData playerData = new PlayerData();
        playerData.health = health;
        playerData.maxHealth = maxHealth;
        playerData.lastSpikeDamage = lastSpikeDamage;
        data.typeData = playerData;
        return data;
    }




    @Override
    public void update(float deltaTime, Window window, Camera camera, World world, ConcurrentHashMap<Integer, Entity> items) {


        for(int i=0; i < deltaTime; i++){

        }


        return;
    }
}
