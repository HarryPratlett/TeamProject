package com.myst.world.map.generating;

import com.myst.world.map.rendering.Tile;

//this need building on so we can potentially in future procedurally generate maps
public class MapGenerator {

    //    public static final Tile test_tile = new Tile(  /*0,*/  "assets/tile_18");
    Tile[] tileSet;


//    probably needs refactoring for future work however works for now
    public MapGenerator(Tile[] tileSet){
        this.tileSet = tileSet;
    }



    public Tile[][] generateMap(int width, int height){
        Tile[][] map = new Tile[width][height];
        for(int x = 0; x < width; x++){
            for(int y=0; y < height; y++){
                map[x][y] = tileSet[0];
            }
        }
        map[5][0] = tileSet[1];


        return map;
    }


}
