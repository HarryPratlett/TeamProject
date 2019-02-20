package com.myst.datatypes;

public class WorldCoords {
    public float x;
    public float y;

//
//
//        y
//        |
//    x---|---(-x)
//        |
//       -y
//
//

//    now that the co-ordinate system is fixed this may not be needed anymore
    public WorldCoords(float x,float y){
        this.x = x;
        this.y = y;
    }

    public TileCoords asTileCoords(float tileSize){
        return new TileCoords((int) x , (int) -y);
    }
}
