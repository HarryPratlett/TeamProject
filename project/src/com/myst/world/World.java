package com.myst.world;

import com.myst.datatypes.TileCoords;
import com.myst.datatypes.WorldCoords;
import com.myst.rendering.Shader;
import com.myst.world.view.Camera;
import com.myst.rendering.Window;
import com.myst.world.collisions.AABB;
import com.myst.world.map.rendering.Tile;
import com.myst.world.map.rendering.TileRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
public class World {
    private final int view = 16;
    private AABB[][] bounding_boxes;
    private int[][] tiles;

    private int width;
    private int height;
    private TileRenderer render;
//    scale is the width of and height of the tiles in the world
    public int scale;

//    private int width;
//    private int height;
    public Tile[][] map;
//    scale is the width of and height of the tiles in the world

    public World(TileRenderer render,Tile[][] tileMap){
        this.render = render;
//        this needs refactoring
        this.map = tileMap;
        width = 30;
        height = 30;
        scale = 20; 
        tiles = new int[map.length][map[0].length];

        bounding_boxes = new AABB[map.length][map[0].length];


        for(int i=0; i < map.length; i++){
            for(int j=0; j < map[0].length; j++){
                if (map[i][j].isSolid()){
                    setTile(map[i][j],i,j);
                }
            }
        }

    }

    public void render(Shader shader, Camera camera, Window window){

//        the camera co-ordinate system is flipped to that of the world so the camera position needs to be flipped
        float leftBorder = (camera.position.x + (camera.getWidth() / 2));
        float rightBorder = (camera.position.x - (camera.getWidth() / 2));

        float topBorder = (-camera.position.y + (camera.getHeight() / 2));
        float bottomBorder = (-camera.position.y - (camera.getHeight() / 2));

        WorldCoords topLeft = new WorldCoords(leftBorder, topBorder);
        WorldCoords bottomRight = new WorldCoords(rightBorder, bottomBorder);

        TileCoords topLeftTile = topLeft.asTileCoords(camera.scale);
        TileCoords bottomRightTile = bottomRight.asTileCoords(camera.scale);

        bottomRightTile.x = bottomRightTile.x + 1;
        bottomRightTile.y = bottomRightTile.y + 1;

        for (int x=topLeftTile.x; x <= bottomRightTile.x; x++){
            for (int y= bottomRightTile.y; y >= topLeftTile.y; y--){
                Tile t = getTile(new TileCoords(x,y));
                if (t != null){

                    render.renderTile(t,
                            new TileCoords(x,y),
                            shader, camera);
                }
            }

        }


    }



//    use this in future to set a border to the world for the camera
    public void correctCamera(Camera camera, Window window){
//        Vector3f pos = camera.getPosition();
//
//        int w = -width * scale * 2;
//        int h = height * scale * 2;
//
//        if(pos.x > -(window.getWidth() / 2) + scale){
//            pos.x = -(window.getWidth() / 2) + scale;
//        }

    }


//    look at what is feeding this
    public void setTile(Tile tile, int x, int y){


        map[x][y] = tile;
        if(tile.isSolid()){
            bounding_boxes[x][y] = new AABB(new Vector2f(x,-y),new Vector2f(0.5f,0.5f));

        } else{
            bounding_boxes[x][y] = null;
        }
    }


    public Tile getTile(TileCoords coords){
        try{
//            his code really needs refactoring here

            return map[coords.x][coords.y];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }


    public AABB getBoundingBox(int x, int y){
        try{
            return bounding_boxes[x][y];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
}