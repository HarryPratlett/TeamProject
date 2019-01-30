package com.myst.world.map.generating;

import com.myst.datatypes.TileCoords;
import com.myst.rendering.Texture;
import com.myst.world.map.rendering.Tile;

//this need building on so we can potentially in future procedurally generate maps
public class MapGenerator {

    //    public static final Tile test_tile = new Tile(  /*0,*/  "assets/tile_18");
    String[] textures;


//    probably needs refactoring for future work however works for now
    public MapGenerator(String[] textures){
        this.textures = textures;
    }



    public Tile[][] generateMap(int width, int height){
        Tile[][] map = new Tile[width][height];
        for(int x = 0; x < width; x++){
            for(int y=0; y < height; y++){
                map[x][y] = new Tile(0, textures[0]);
            }
        }
        map[5][0] = new Tile(1,textures[1]);
        map[5][0].setSolid();
        map[6][0] = new Tile(1,textures[1]);
        map[6][0].setSolid();


        return map;
    }


}
