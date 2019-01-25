package com.myst;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class World {
    private final int view = 16;
    private AABB[] bounding_boxes;
    private int[] tiles;
    private int width;
    private int height;
//    scale is the width of and height of the tiles in the world
    public int scale;

    private Matrix4f world;

    public World(){
        width = 30;
        height = 30;
        scale = 30;

        tiles = new int[width * height];

        bounding_boxes = new AABB[width * height];

//        top left corner of the world is in the centre of the screen
        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);

    }

    public World(String world){
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

//        ByteBuffer tile_sheet = STBImage.stbi_load(filepath,width,height,comp,4);
    }

    public void render(TileRenderer render, Shader shader, Camera camera, Window window){

//        left border load all tiles who lie in this area
        int leftx = (int)((camera.position.x + (camera.getWidth() / 2)) / scale) +1; //gets starting x tile
        int rightx = (int)((camera.position.x - (camera.getWidth() / 2)) / scale) -2; // gets ending x tile

        int bottomy = (int) ((camera.position.y - (camera.getHeight() / 2)) / scale) - 1;
        int topy = (int) ((camera.position.y + (camera.getHeight() / 2)) / scale) + 2;
//        tile co-ordinates are (0,0) -> (0,0) (1,0) -> (scale,0) (0,1) -> (0, -scale)


        for (int x=leftx; x >= rightx; x--){
            for (int y= bottomy; y < topy; y++){
                Tile t = getTile(-x, y);
                if (t != null){
                    render.renderTile(t, -x, -y,shader,world, camera);
                }
            }

        }


//        render.renderTile(t,0,-5,shader,world,camera);






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

    public void setTile(Tile tile, int x, int y){

        tiles[ x + y*width] = tile.getId();
        if(tile.isSolid()){
            bounding_boxes[ x + (y*width)] = new AABB(new Vector2f(x,-y),new Vector2f(0.5f,0.5f));

        } else{
            bounding_boxes[ x + (y*width)] = null;
        }
    }


    public Tile getTile(int x, int y){
        try{
//            his code really needs refactoring here
            if( x < width && y < height && x >= 0 && y >= 0){
                return Tile.tiles[tiles[x + (y*width)]];
            } else{
                return null;
            }

        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }


    public AABB getBoundingBox(int x, int y){
        int tileX = x /*/ scale*/;
        int tileY = y /*/ scale*/;
        try{
            //            his code really needs refactoring here
            if( tileX < width && tileY < height && tileX >= 0 && tileY >= 0){
                return bounding_boxes[ tileX + (tileY*width)];
            } else{
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
}