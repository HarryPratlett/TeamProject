/**
 * @author Aled Jackson
 */
package com.myst.world;

import com.myst.datatypes.TileCoords;
import com.myst.datatypes.WorldCoords;
import com.myst.rendering.Shader;
import com.myst.rendering.Window;
import com.myst.world.collisions.AABB;
import com.myst.world.map.rendering.Tile;
import com.myst.world.map.rendering.TileRenderer;
import com.myst.world.view.Camera;
import org.joml.Vector2f;

/**
 * Creates a world
 */
public class World {
    private final int view = 16;
    private AABB[][] bounding_boxes;
    private int[][] tiles;

    private TileRenderer render;
    public Tile[][] map;

    public int scale;

    /**
     * Constructor to create world
     * @param render Renderer of tiles
     * @param tileMap Map created by map generator
     */
    public World(TileRenderer render,Tile[][] tileMap){
        this.render = render;

        this.map = tileMap;


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

    /**
     * Renders the map
     * @param shader Shader rendered on
     * @param camera Users camera
     * @param window Window displayed in
     */
    public void render(Shader shader, Camera camera, Window window){

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

    public Tile getTile(int x,int y){
        try {
            return map[x][y];
        }catch (ArrayIndexOutOfBoundsException e){
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