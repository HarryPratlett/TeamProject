/**
 * @author Aled Jackson
 */
package com.myst.datatypes;

/**
 * Tile co-ordinate class for Map Generation
 */
public class TileCoords {
    public int x;
    public int y;

    /**
     * Constructor for the tile co-ordinates
     * @param x X co-ordinate
     * @param y Y co-ordinate
     */
    public TileCoords(int x, int y){
        this.x = x;
        this.y = y;
    }


    public WorldCoords asWorldCoords(float tileSize){
        return new WorldCoords(-x * tileSize, -y * tileSize );
    }

    public static float xAsWorldCoords(float x, float tileSize){
        return -x * tileSize;
    }

    public static float yAsWorldCoords(float y, float tileSize){
        return -y * tileSize;
    }



}

