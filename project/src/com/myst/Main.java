
package com.myst;

import com.myst.GUI.GUI;
import com.myst.audio.Audio;
import com.myst.helper.Timer;
import com.myst.input.Input;
import com.myst.networking.EntityData;
import com.myst.rendering.Shader;
import com.myst.world.collisions.Bullet;
import com.myst.world.collisions.Line;
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
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main {

    static int IDCounter = 0;
    static String clientID = "Base2";

    public static void setUp() {
        Window.setCallbacks();


        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }
    }


    public static void main(String[] args) {
        setUp();

        Window window = new Window();

    ConcurrentHashMap<String,ConcurrentHashMap<Integer, Entity>> entities = new ConcurrentHashMap<>();
//        only the main can render and create items so this array hands stuff to the main to render
    ConcurrentHashMap<String,ConcurrentHashMap<Integer, EntityData>> toRender = new ConcurrentHashMap<>();ArrayList<Line> playerBullets = new ArrayList<>();

        ClientConnection connection = new ClientConnection(entities, toRender, "127.0.0.1");



    connection.startConnection(clientID);

        window.setFullscreen(false);
        window.createWindow("My game");

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


//        glfwSetWindowPos(window,(videoMode.width() - 640) / 2, (videoMode.height() - 480) / 2);

//        glfwShowWindow(window);

        glClearColor(0f, 0f, 0f, 0f);

        Shader environmentShader = new Shader("assets/shader");
    Shader menuShader = new Shader("assets/shader2");

        String[] textures = new String[21];
        String path = ("assets/tile/");
        textures[0] = path + "tile_01";
        textures[1] = path + "tile_02";
        textures[2] = path + "tile_03";
        textures[3] = path + "tile_04";
        textures[4] = path + "tile_05";
        textures[5] = path + "tile_06";
        textures[6] = path + "tile_07";
        textures[7] = path + "tile_08";
        textures[8] = path + "tile_09";
        textures[9] = path + "tile_10";
        textures[10] = path + "tile_11";
        textures[11] = path + "tile_12";
        textures[12] = path + "tile_13";
        textures[13] = path + "tile_14";
        textures[14] = path + "tile_15";
        textures[15] = path + "tile_16";
        textures[16] = path + "tile_17";
        textures[17] = path + "tile_18";
        textures[18] = path + "tile_19";
        textures[19] = path + "tile_20";
        textures[20] = path + "tile_479";

        Tile[][] map = new MapGenerator(textures).generateMap(100, 100);

        TileRenderer tiles = new TileRenderer(textures);

        World world = new World(tiles, map);


    Player player= new Player(playerBullets);
    player.lightSource = true;


    player.transform.pos.add(new Vector3f(1,-1,0));

player.localID = IDCounter;
    player.owner = clientID;

    IDCounter++;    Camera camera = new Camera(window.getWidth(), window.getHeight());

        entities.put(clientID, new ConcurrentHashMap<Integer, Entity>());

    ConcurrentHashMap<Integer, Entity> myEntities = entities.get(clientID);

        myEntities.put(player.localID, player);

        double frame_cap = 1.0 / 60.0;

        double time = Timer.getTime();
        double unprocessed = 0;

        double frame_time = 0;
        int frames = 0;

        Boolean renderFrame;

        double debugCurrentTime = Timer.getTime();
        double debugLastTime = Timer.getTime();

        camera.bindPlayer(player);

        GUI gui = new GUI(window, window.getInput());

        Darkness dark = new Darkness(window);

        Audio.getAudio().initInput(window.getInput());

        Lighting lights = new Lighting(window.getInput(), window);

        while (!window.shouldClose()) {

            renderFrame = false;

            double time2 = Timer.getTime();
            double deltaTime = time2 - time;
            time = time2;

            unprocessed += deltaTime;
            frame_time += deltaTime;


//            in the case you want to render a frame as you have gone over the frame_cap
//            a while is used instead of an if incase the performance is less than 30 FPS
            while (unprocessed >= frame_cap) {
//                look into effects of containing a thread.sleep();
//                take away the frame cap so that you account for the time you've taken of the next frame
                unprocessed -= frame_cap;

                renderFrame = true;

                window.update();


                camera.updatePosition();
                debugCurrentTime = Timer.getTime();
                double timeSinceLastUpdate = (debugCurrentTime - debugLastTime);
                debugLastTime = debugCurrentTime;

        Audio.getAudio().update();player.update((float) timeSinceLastUpdate, window, camera, world);

        gui.update();
calculateBullets(myEntities, playerBullets, map);
        playerBullets.clear();

      }

      if (frame_time >= 1) {
        System.out.println(frames);
        frame_time = 0;
        frames = 0;





}      if (renderFrame) {
        glClear(GL_COLOR_BUFFER_BIT);

        calculateLighting(entities,camera,environmentShader,window);world.render(environmentShader,camera, window);

        for(String owner: entities.keySet()){
          for(Integer entityID: entities.get(owner).keySet()){
            entities.get(owner).get(entityID).render(camera, environmentShader);
              }
          }


        createAndRender(toRender, entities);


        gui.render(menuShader);

        window.swapBuffers();

        frames += 1;
}


        }

//        clears everything we have used from memory
        glfwTerminate();

//        sloppy and needs tidying
        System.exit(1);
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


  public static void calculateLighting(ConcurrentHashMap<String,ConcurrentHashMap<Integer, Entity>> entities,Camera camera, Shader environmentShader, Window window){
      float[] lightPositions = new float[16];
      int[] lightsOn = new int[8];
      float[] lightRadiai = new float[8];

      for(int i=0;i <lightsOn.length;i++){
          lightsOn[i] = 0;
          lightRadiai[i] = 0;
      }

//                getting which players have their lights on and then passing that to opengl
//                this is for the shadows and lighting
      int d=0;
      for(String owner: entities.keySet()){
          if(d>7)break;
          for(Integer entityID: entities.get(owner).keySet()){
              if(d>7)break;
              if(entities.get(owner).get(entityID).getData().lightSource){
                  Matrix4f projection = entities.get(owner).get(entityID).transform.getProjection(camera.getProjection());
                  Vector4f lightPos = new Vector4f(0,0,0,1).mul(projection);
                  lightRadiai[d] = entities.get(owner).get(entityID).lightDistance;
                  lightPositions[d*2] = lightPos.x * 2;
                  lightPositions[d*2 + 1] = lightPos.y * 2;
                  lightsOn[d] = 1;
                  d++;
              }
          }
      }

      environmentShader.setUniform("lightPositions", lightPositions);
      environmentShader.setUniform("lightsOn", lightsOn);
      environmentShader.setUniformArray("lightRadius", lightRadiai);

      environmentShader.setUniform("winHeight", window.getHeight());
      environmentShader.setUniform("winWidth", window.getWidth());
  }//    make this render all the objects in the hashmap then set the hasmap to null
  public static void createAndRender(ConcurrentHashMap<String,ConcurrentHashMap<Integer, EntityData>> items,ConcurrentHashMap<String,ConcurrentHashMap<Integer, Entity>> entities){
    for(String owner: items.keySet()){
      for(Integer id: items.get(owner).keySet()){
        EntityData entitiesData = items.get(owner).get(id);
        if (entitiesData != null){
          Entity ent = new Enemy();
          ent.readInEntityData(entitiesData);
          entities.get(owner).put(id, ent);
          items.get(owner).put(id,null);
        }
      }
    }
  }
}

