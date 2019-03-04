package com.myst.world.map.rendering;

public class Tile {
//    tiles is the different tiles that exist
    public static int nOfTiles = 0;
    private boolean solid;

//    public static final Tile test_tile = new Tile(  /*0,*/  "assets/tile_18");
//    public static final Tile test_tile2 = new Tile(/*1,*/"assets/tile_186");
//    need error checking to make sure that the texture has loaded

    private int id;
    private String texture;

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

    public Tile setSolid(){this.solid = true; return this;}

    public boolean isSolid(){return this.solid;}

    public int getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}
