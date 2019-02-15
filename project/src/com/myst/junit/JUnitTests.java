package com.myst.junit;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Test;
import org.lwjgl.opengl.GL;

import com.myst.AI.AI;
import com.myst.helper.Timer;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.entities.Bot;
import com.myst.world.entities.Player;
import com.myst.world.map.generating.MapGenerator;
import com.myst.world.map.rendering.Shader;
import com.myst.world.map.rendering.Tile;
import com.myst.world.map.rendering.TileRenderer;
import com.myst.world.view.Camera;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class JUnitTests {
	
	private World world;
	
	private void initialiseWorld() {
		Window.setCallbacks();
		glfwInit();
		Window window = new Window();

        window.setFullscreen(false);
        window.createWindow("Tests");
        

		GL.createCapabilities();
        
	    glEnable(GL_TEXTURE_2D);
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	    glClearColor(0f,0f,0f, 0f);

	    Shader shader = new Shader("project/assets/shader");

	    Tile test_tile = new Tile(  0,  "project/assets/tile_18");
	    Tile test_tile2 = new Tile(1,"project/assets/tile_186");

	    Tile[] tileSet = new Tile[2];
	    tileSet[0] = test_tile;
	    tileSet[1] = test_tile2;

	    Tile[][] map = new MapGenerator(tileSet).generateMap(100,100);


	    TileRenderer tiles = new TileRenderer(map);

	    World world = new World(tiles);
	    
	    Player player = new Player();
	    
	    Bot bot = new Bot(new float[]{
            -0.5f, 0.5f, 0f, /*0*/  0.5f, 0.5f, 0f, /*1*/    0.5f, -0.5f, 0f, /*2*/
                    -0.5f, -0.5f, 0f/*3*/
        },
                new float[] {
                0f, 0f,   1, 0f,  1f, 1f,
                0f, 1f
        },
        new int[] {
                0,1,2,
                2,3,0
        },
        new Vector2f(0.5f,0.5f), shader);
	    
	    Camera camera = new Camera(window.getWidth(), window.getHeight());

	    world.setTile(test_tile2.setSolid(),5,0 );
	    world.setTile(test_tile2.setSolid(),6,0 );
	    
	    Matrix4f target = new Matrix4f();
	    
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


                player.render(shader,camera);

                window.swapBuffers();

                frames += 1;

            }
            
        }

	}
	
	@Test
	public void correctPathShouldBeFound() {
		initialiseWorld();
		Vector2f start = new Vector2f();
		Vector2f end = new Vector2f();
		start.set(0, 0);
		end.set(10,0);
		AI sim = new AI(start, world);
		ArrayList<Vector2f> path = new ArrayList<Vector2f>();
		path.add(start);
		path.add(new Vector2f(1,0));
		path.add(new Vector2f(2,0));
		path.add(new Vector2f(3,0));
		path.add(new Vector2f(4,0));
		path.add(new Vector2f(5,0));
		path.add(new Vector2f(6,0));
		path.add(new Vector2f(7,0));
		path.add(new Vector2f(8,0));
		path.add(new Vector2f(9,0));
		path.add(new Vector2f(10,0));
		
		Assert.assertEquals(path, sim.pathFind(end));
		
	}

}
