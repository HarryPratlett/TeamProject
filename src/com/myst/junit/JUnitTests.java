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
import com.myst.rendering.Shader;
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
	    Shader shader = new Shader("assets/Shader");
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

        world = new World(tiles, map);
        
	  
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
