package com.myst;

<<<<<<< HEAD
import com.myst.datatypes.TileCoords;
import com.myst.helper.Timer;
import com.myst.rendering.Texture;
=======
import com.myst.helper.Timer;
import com.myst.rendering.Shader;
import com.myst.world.entities.Entity;
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
import com.myst.world.view.Camera;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.entities.Player;
import com.myst.world.map.generating.MapGenerator;
<<<<<<< HEAD
import com.myst.world.map.rendering.Shader;
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
import com.myst.world.map.rendering.Tile;

import com.myst.world.map.rendering.TileRenderer;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import org.joml.Matrix4f;

<<<<<<< HEAD
import java.util.HashMap;

=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main {


    public static void setUp(){
        Window.setCallbacks();
<<<<<<< HEAD



        if (!glfwInit()){
            throw new IllegalStateException("Failed to initialise GLFW");
        }
    }
=======
        if (!glfwInit()){
            throw new IllegalStateException("Failed to initialise GLFW");
        }
     }
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2



    public static void main(String[] args){
        setUp();

<<<<<<< HEAD
        Window window = new Window();

        window.setFullscreen(false);
        window.createWindow("My game");


=======

//        set up connection
//        Connection con = asdf();
//        from server on thread 2
//        new ServerConn(Entities).run()


        Window window = new Window();
        Entity[] entities = new Entity[1];
        window.setFullscreen(false);
        window.createWindow("My game");
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


//        glfwSetWindowPos(window,(videoMode.width() - 640) / 2, (videoMode.height() - 480) / 2);

//        glfwShowWindow(window);

        glClearColor(0f,0f,0f, 0f);

<<<<<<< HEAD
        Shader shader = new Shader("/Users/seongheehan/Documents/myst/project/assets/shader");

        String[] textures = new String[21];
        String path = ("/Users/seongheehan/Documents/myst/project/assets/tile/");
=======
        Shader shader = new Shader("assets/shader");

        String[] textures = new String[21];
        String path = ("assets/tile/");
       
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
        textures[0] = path+"tile_01";
        textures[1] = path+"tile_02";
        textures[2] = path+"tile_03";
        textures[3] = path+"tile_04";
        textures[4] = path+"tile_05";
        textures[5] = path+"tile_06";
        textures[6] = path+"tile_07";
        textures[7] = path+"tile_08";
        textures[8] = path+"tile_09";
        textures[9] = path+"tile_10";
        textures[10] = path+"tile_11";
        textures[11] = path+"tile_12";
        textures[12] = path+"tile_13";
        textures[13] = path+"tile_14";
        textures[14] = path+"tile_15";
        textures[15] = path+"tile_16";
        textures[16] = path+"tile_17";
        textures[17] = path+"tile_18";
        textures[18] = path+"tile_19";
        textures[19] = path+"tile_20";
<<<<<<< HEAD
        
        textures[20] = path+"tile_479";
      
        
        
        
        
        
        
        
        
        
        
        
        


        Tile[][] map = new MapGenerator(textures).generateMap(100,100);


        TileRenderer tiles = new TileRenderer(map);

        World world = new World(tiles);

=======
         
        textures[20] = path+"tile_479";
     

        Tile[][] map = new MapGenerator(textures).generateMap(100,100);
       
       
        

        TileRenderer tiles = new TileRenderer(textures);

        World world = new World(tiles, map);
        
        
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
        Player player = new Player();

        player.transform.pos.add(new Vector3f(1,-1,0));

        Camera camera = new Camera(window.getWidth(), window.getHeight());








//        world.se
//
//        Matrix4f projection = new Matrix4f().ortho2D(640 / 2, -640 / 2, -480 / 2, 480 / 2);
//
////        projection.rotate(20.5f, 0,0,0);
////        projection.scale(200);
//        Matrix4f scale = new Matrix4f()
//                .translate(new Vector3f(0,0,0))
//                .scale(16);


        Matrix4f target = new Matrix4f();

//        projection = projection.mul(scale,target);






        double frame_cap = 1.0/60.0;

        double time = Timer.getTime();
        double unprocessed = 0;

        double frame_time = 0;
        int frames = 0;

        Boolean renderFrame;

        double debugCurrentTime = Timer.getTime();
        double debugLastTime = Timer.getTime();

        camera.bindPlayer(player);

        while (!window.shouldClose()){
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

                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(window.getWindow(),true);
                }
                camera.updatePosition();





            }

            if (frame_time >= 1) {
                System.out.println(frames);
                System.out.println(camera.position);
                frame_time = 0;
                frames = 0;
            }


            debugCurrentTime = Timer.getTime();
            double timeSinceLastUpdate = (debugCurrentTime - debugLastTime);
            debugLastTime = debugCurrentTime;

            player.update((float) timeSinceLastUpdate, window, camera, world);

            window.update();

            if (renderFrame) {
                glClear(GL_COLOR_BUFFER_BIT);

//                tiles.renderTile(test_tile,new TileCoords(0,0),shader, new Matrix4f().scale(30),camera);

                world.render(shader,camera, window);

<<<<<<< HEAD

                player.render(shader,camera);
=======
//                for (int i=0; i < entities.length; i++){
//                    entities[i].render(camera);
//                }

                player.render(camera);
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2

                window.swapBuffers();

                frames += 1;

            }

        }

//        clears everything we have used from memory
        glfwTerminate();
    }
}
