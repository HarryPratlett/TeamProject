package com.myst.datatypes;

/**
 * Co-ordinate system for the world the player operates in
 */
public class WorldCoords {
    public float x;
    public float y;


    /**
     * Creates a new world co-ordinate system
     * @param x X co-ordinate
     * @param y Y co-ordinates
     */
    public WorldCoords(float x,float y){
        this.x = x;
        this.y = y;
    }

    /**
     * Gets this as tile co ordinates
     * @param tileSize Tile sizes
     * @return Return new tile coordinates
     */
    public TileCoords asTileCoords(float tileSize){
        return new TileCoords((int) (-x / tileSize) , (int) (-y / tileSize));
    }

    public static int xAsTileCoords(float x, float scale){
        return (int) (-x / scale);
    }

    public static int yAsTileCoords(float y, float scale){
        return (int) (-y / scale);
    }
}
