/**
 * @author Aled Jackson
 */
package com.myst.world.map.rendering;

import java.io.Serializable;

public class Tile implements Serializable{
//    tiles is the different tiles that exist
    public static int nOfTiles = 0;
    private boolean solid;

//    public static final Tile test_tile = new Tile(  /*0,*/  "assets/tile_18");
//    public static final Tile test_tile2 = new Tile(/*1,*/"assets/tile_186");
//    need error checking to make sure that the texture has loaded

    private int id;
    private String texture;

    /**
     * Creates a new tile
     * @param id The id of the tile e.g. 1
     * @param texture The texture that this tile will have e.g. brick/dirt
     */
    public Tile(int id, String texture){
        nOfTiles++;
        this.id = id;
        this.texture = texture;

//        if(tiles[id] != null){
//            throw new IllegalStateException("Tiles at: " + id + " is already being used");
//        }
//        tiles[id] = this;

        this.solid = false;
    }

    /**
     * Sets a tile to solid so the player cannot walk through it
     * @return The tile which will have been set to solid
     */
    public Tile setSolid(){this.solid = true; return this;}

    /**
     * Returns whether or not a tile is solid
     * @return The boolean which will be true if it is solid or false if it is not
     */
    public boolean isSolid(){return this.solid;}

    /**
     * Gets the ID of a tile
     * @return Returns the ID as an int
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the texture of a tile
     * @return The path of the texture
     */
    public String getTexture() {
        return texture;
    }
}
