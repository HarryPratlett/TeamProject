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
        for(int i=0; i<1400; i++) {
            

    int x1 = (int) Math.floor(Math.random() * 100);
	int y1 = (int) Math.floor(Math.random() * 100);
	int m1 = (int) Math.floor(Math.random() * 20);
	
	int TF1 = (int) Math.floor(Math.random()*2);
	boolean extendedmap1;
	if(TF1==0) {
		extendedmap1=false;
	}
	else extendedmap1 = true;
	
	
	
	int TF2 = (int) Math.floor(Math.random()*2);
	boolean extendedmap2 = false;
	if(TF2==0) {
		extendedmap2=false;
	}
	else
		extendedmap2=true;
	
	
	
	
	
	
	
	
	
	
	
	// prints coordinates of tile
	System.out.println(x1);
	System.out.println(y1);
	
	
	
	
	
	
	//check the coordinate is valid or not
	if(x1<width|0<x1) {
		if(y1<height|0<y1) {
			map[x1][y1]=new Tile(m1-1,textures[m1]);
			
			
			if(x1+1<height) {
				map[x1+1][y1]=new Tile(m1-1,textures[m1]);
				if(y1+1<height) {
					map[x1+1][y1+1]=new Tile(m1-1, textures[m1]);
					map[x1+1][y1+1]=new Tile(m1-1, textures[m1]);
					map[x1][y1+1]=new Tile(m1-1, textures[m1]);
					
					
					
					
					//randomly choose size of tile map
					if(extendedmap1) { System.out.println("extend tile 3X3_by _horizontally");}
					if(x1+2<width) {
						map[x1+2][y1]=new Tile(m1-1, textures[m1]);
						map[x1+2][y1+1]=new Tile(m1-1, textures[m1]);
						
						
						//randomly choose size of tile map
						if(extendedmap2){System.out.println("extend tile by 3x3 _vertically");}
						if(y1+2<height) {
							map[x1+1][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1+1][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1][y1+2]=new Tile(m1-1, textures[m1]);	
	    					map[x1+2][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1+2][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1][y1+2]=new Tile(m1-1, textures[m1]);
	    				
						}
					}
				}
			
			}
			
			
			
			
			;
			
			
			
		}
	}
	map[2][2]=new Tile(20,textures[20]);
	map[2][2].setSolid();
    
    }
        
        
        for(int i=0; i<width; i++) {
        	map[0][i] = new Tile(20,textures[20]);
        	map[0][i].setSolid();
        	System.out.println(map[0][i].isSolid());
        }
        
        
        for(int i=0; i<height; i++) {
        	map[i][0] = new Tile(20,textures[20]);
        	map[i][0].setSolid();
        	
        }
        for(int i=0; i<height; i++) {
        	map[i][height-1] =new Tile(20,textures[20]);
        	map[i][height-1].setSolid();
        }
   
        for(int i=0; i<width; i++) {
        	map[width-1][i] = new Tile(20,textures[20]);
        	map[width-1][i].setSolid();
        }
       


        return map;
    }


}
