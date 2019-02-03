package com.myst.datatypes;

public class TileCoords {
    public int x;
    public int y;

//    the tile coords are stored so that
//
//
//        |------- x
//        |
//        |
//        y
//




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

