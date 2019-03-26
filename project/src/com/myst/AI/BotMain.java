package com.myst.AI;

import com.myst.GUI.GUI;
import com.myst.helper.Timer;
import com.myst.networking.EntityData;
import com.myst.rendering.Shader;
import com.myst.world.collisions.Line;
import com.myst.world.entities.Bot;
import com.myst.world.collisions.Bullet;
import com.myst.world.entities.Enemy;
import com.myst.world.entities.Entity;
import com.myst.world.view.Camera;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.entities.Player;
import com.myst.world.map.generating.MapGenerator;
import com.myst.world.map.rendering.Tile;


import com.myst.networking.clientside.ClientConnection;

import com.myst.world.map.rendering.TileRenderer;
import org.joml.*;
import org.lwjgl.opengl.GL;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class BotMain extends Thread{

    static int IDCounter = 0;
    static String clientID = String.valueOf(Math.random());
    static int resHeight;
    static int resWidth;
    public boolean shouldEnd;
    private int port;

    public BotMain(int port){
        this.port = port;
    }


    public void run(){


        ConcurrentHashMap<String,ConcurrentHashMap<Integer, Entity>> entities = new ConcurrentHashMap<>();
//        only the main can render and create items so this array hands stuff to the main to render
        ConcurrentHashMap<String,ConcurrentHashMap<Integer, EntityData>> toRender = new ConcurrentHashMap<>();

        //ArrayList<Line> playerBullets = new ArrayList<>();
        ArrayList<Line> botBullets = new ArrayList<>();
        ClientConnection connection = new ClientConnection(entities, toRender,"127.0.0.1",port,clientID);

        connection.run();

        Tile[][] map = connection.map;
        World world = new World(null,map);


        //Player player = new Player(playerBullets);
        //player.lightSource = true;
        //player.transform.pos.add(new Vector3f(1,-1,0));

        // player.localID = IDCounter;
        // player.owner = clientID;
        Bot bot = new Bot(new Vector2f(0.5f,0.5f), botBullets);
        System.out.println("I exist");

        bot.localID = IDCounter;
        bot.owner = clientID;
        IDCounter++;

        bot.transform.pos.add(2, -2, 1);

        bot.initialiseAI(world);
        System.out.println("trying to find a path");
        bot.setPath(new Vector3f(1,-1,1));
        System.out.println("I found a path");

        entities.put(clientID, new ConcurrentHashMap<Integer,Entity>());

        ConcurrentHashMap<Integer, Entity> myEntities = entities.get(clientID);

        myEntities.put(bot.localID, bot);

        double frame_cap = 1.0/60.0;

        double time = Timer.getTime();
        double unprocessed = 0;

        double frame_time = 0;
        int frames = 0;



        double debugCurrentTime = Timer.getTime();
        double debugLastTime = Timer.getTime();


        while (!shouldEnd){
            debugCurrentTime = Timer.getTime();
            double timeSinceLastUpdate = (debugCurrentTime - debugLastTime);
            debugLastTime = debugCurrentTime;

            bot.updateBot((float) timeSinceLastUpdate, world, entities);
            botBullets.clear();

        }
    }


    public static void calculateBullets(ConcurrentHashMap<Integer, Entity> myEntities, ArrayList<Line> bullets, Tile[][] map){
        for(Line bullet: bullets){
            Vector2f bulletVec = bullet.vector;
            Vector2f currentPos = bullet.position;
            boolean posXDirection = true;
            boolean posYDirection = true;
            int stepX = 1;
            int stepY = 1;

            bulletVec.normalize();

            if(bulletVec.x < 0){
                posXDirection = false;
                stepX = -1;
            }
            if(bulletVec.y < 0){
                posYDirection = false;
                stepY = -1;
            }



            float deltaX = Math.abs(1 /bulletVec.x);
            float deltaY = Math.abs(1 /bulletVec.y);

//          distance to the closest x edge
            float xDist;
            if(posXDirection){
                xDist = (float) Math.floor((double) currentPos.x) + 1;
            } else{
                xDist = (float) Math.floor((double) currentPos.x);
            }

            xDist = (xDist - currentPos.x) * deltaX;

//          distance to the closest y edge
            float yDist;
            if(posYDirection){
                yDist = (float) Math.floor((double) currentPos.y) + 1;
            } else {
                yDist = (float) Math.floor((double) currentPos.y);
            }

            yDist = (yDist - currentPos.y) * deltaY;


            int mapX = (int) Math.floor((double) currentPos.x);
            int mapY = (int) Math.floor((double) currentPos.y);

            boolean hitWall = false;
            int total = 0;
            // if if was an x side then 0 if it was a y side then side = 1
            int side = 0;
            while(!hitWall && total < 20){
                if(xDist < yDist){
                    xDist += deltaX;
                    mapX += stepX;
                    side = 0;
                    total++;
                }else{
                    yDist += deltaY;
                    mapY += stepY;
                    side = 1;
                    total++;
                }
                if(map[mapX][mapY].isSolid()){
                    hitWall = true;
                }
            }

            float wallDist;

            if (side == 0){
                wallDist = (mapX - currentPos.x + (1- stepX) / 2) / bulletVec.x;
            } else{
                wallDist = (mapY - currentPos.y + (1- stepY) / 2) / bulletVec.y;
            };

            Bullet newBullet = new Bullet(bullet,wallDist,100f);
            newBullet.owner = clientID;
            newBullet.localID = IDCounter;
            IDCounter++;

            newBullet.transform.rotation = (float) Math.atan(bulletVec.x / bulletVec.y);
            if (bulletVec.y > 0){
                newBullet.transform.rotation += Math.PI;
            }
            newBullet.transform.pos.x = currentPos.x;
            newBullet.transform.pos.y = -currentPos.y;


            myEntities.put(newBullet.localID,newBullet);
        }
    }

}