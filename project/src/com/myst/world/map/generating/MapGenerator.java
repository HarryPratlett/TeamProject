package com.myst.world.map.generating;

import java.util.Scanner;
<<<<<<< HEAD
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
=======
<<<<<<< HEAD

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
import java.io.*;

import com.myst.datatypes.TileCoords;
import com.myst.rendering.Texture;
import com.myst.world.map.rendering.Tile;

<<<<<<< HEAD

=======
<<<<<<< HEAD
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
//this need building on so we can potentially in future procedurally generate maps
public class MapGenerator {
	
	private Scanner m;
<<<<<<< HEAD
=======
=======

//this need building on so we can potentially in future procedurally generate maps
public class MapGenerator {
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59

    //    public static final Tile test_tile = new Tile(  /*0,*/  "assets/tile_18");

    String[] textures;


//    probably needs refactoring for future work however works for now
    public MapGenerator(String[] textures){
        this.textures = textures;
    }

<<<<<<< HEAD
=======
<<<<<<< HEAD


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
=======
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
    public Tile[][] generateMap(int width, int height){
    	
        Tile[][] map = new Tile[width][height];
       // Tileset[][] map1 = new Tileset[width][height];
        
        for(int x = 0; x < width; x++){
            for(int y=0; y < height; y++){
                map[x][y] = new Tile(0, textures[0]);  
                map[x][y].unsetSolid();//*
            }
        }
      

        
  
        
        for(int i=0; i<1400; i++) {
    int x1 = (int) Math.floor(Math.random() * 100);
	int y1 = (int) Math.floor(Math.random() * 100);//this number is the range of cor
	
	//int m1 = (int) Math.floor(Math.random() * 20);
	int m1=19;//for test
<<<<<<< HEAD
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
	
	int TF1 = (int) Math.floor(Math.random()*2);
	boolean extendedmap1;
	if(TF1==0) {
		extendedmap1=false;
	}
	else extendedmap1 = true;
<<<<<<< HEAD
	int TF2 = (int) Math.floor(Math.random()*2);
	
=======
<<<<<<< HEAD
	
	
	
	int TF2 = (int) Math.floor(Math.random()*2);
=======
	int TF2 = (int) Math.floor(Math.random()*2);
	
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
	boolean extendedmap2 = false;
	if(TF2==0) {
		extendedmap2=false;
	}
	else
		extendedmap2=true;
<<<<<<< HEAD
=======
<<<<<<< HEAD
	
	
	
	
	
	
	
	
	
	
	
	// prints coordinates of tile
	System.out.println(x1);
	System.out.println(y1);
	
	
	
=======
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
/*
	//
	int[][] map={
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},		
	}
*/	

<<<<<<< HEAD
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
	
	
	
	//check the coordinate is valid or not
<<<<<<< HEAD
=======
<<<<<<< HEAD
	if(x1<width|0<x1) {
		if(y1<height|0<y1) {
			map[x1][y1]=new Tile(m1-1,textures[m1]);
			
			
			if(x1+1<height) {
=======
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
	if(x1<width|0<x1) {//within the wall
		
		if(y1<height|0<y1) {//within the wall
			map[x1][y1]=new Tile(m1-1,textures[m1]);
			//*map[x1][y1].setSolid();//if added,player would be outside
			
			 if(x1+1<height) {
<<<<<<< HEAD
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
				map[x1+1][y1]=new Tile(m1-1,textures[m1]);
				if(y1+1<height) {
					map[x1+1][y1+1]=new Tile(m1-1, textures[m1]);
					map[x1+1][y1+1]=new Tile(m1-1, textures[m1]);
					map[x1][y1+1]=new Tile(m1-1, textures[m1]);
<<<<<<< HEAD

					//randomly choose size of tile map
=======
<<<<<<< HEAD
					
					
					
					
					//randomly choose size of tile map
					if(extendedmap1) { System.out.println("extend tile 3X3_by _horizontally");}
=======

					//randomly choose size of tile map
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
					if(x1+2<width) {
						map[x1+2][y1]=new Tile(m1-1, textures[m1]);
						map[x1+2][y1+1]=new Tile(m1-1, textures[m1]);
						
						
						//randomly choose size of tile map
<<<<<<< HEAD
=======
<<<<<<< HEAD
						if(extendedmap2){System.out.println("extend tile by 3x3 _vertically");}
						if(y1+2<height) {
							map[x1+1][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1+1][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1][y1+2]=new Tile(m1-1, textures[m1]);	
	    					map[x1+2][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1+2][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1][y1+2]=new Tile(m1-1, textures[m1]);
	    				
=======
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
						if(y1+2<height) {
							map[x1+1][y1+2]=new Tile(m1-1, textures[m1]);
							
	    					//map[x1+1][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1][y1+2]=new Tile(m1-1, textures[m1]);	
	    				
	    					map[x1+2][y1+2]=new Tile(m1-1, textures[m1]);
	    					
	    					//map[x1+2][y1+2]=new Tile(m1-1, textures[m1]);
	    					map[x1][y1+2]=new Tile(m1-1, textures[m1]);
	    					
<<<<<<< HEAD
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
						}
					}
				}
			
<<<<<<< HEAD
			}			
			;
	
		}
	}
=======
<<<<<<< HEAD
			}
			
			
			
			
			;
			
			
			
		}
	}
	
    
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
	 
    try {
		BufferedImage tile_sheet = ImageIO.read(new File("/Users/seongheehan/Desktop/map1.png"));
		// 100 * 100 PNG
		//BufferedImage entity_sheet = ImageIO.read(new File(""));
		
		//width = tile_sheet.getWidth();
		//height = tile_sheet.getHeight();
		
		int [] colourTileSheet = tile_sheet.getRGB(0,0,width,height, null,0,width);
		
		
		  
        for (int y =0; y<height; y++) {
        	for( int x =0; x<width; x++) {
        		int red = (colourTileSheet [x + y * width] >>16) & 0xFF;
        		
        		if(red == 2) {
        			map[x][y]=new Tile(20,textures[20]);
	        		map[x][y].setSolid();
        		}
        		
        		
 
     
        	
        		
        	}
        
        
		
		
        }
    }catch (IOException e) {
		
		e.printStackTrace();
	}
  
	
    }
        
        //
        for(int i=0; i<width; i++) {
        	//map[0][i] = new Tile(20,textures[20]);
        	map[0][i].setSolid();
<<<<<<< HEAD
=======
        	System.out.println(map[0][i].isSolid());
=======
			}			
			;
	
		}
	}
	
	
	
	//the starting point
	map[2][2]=new Tile(20,textures[20]);
	//map[2][2].setSolid();
    
    }
        
        //
        for(int i=0; i<width; i++) {
        	map[0][i] = new Tile(20,textures[20]);
        	map[0][i].setSolid();
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
        }
        
        
        for(int i=0; i<height; i++) {
        	map[i][0] = new Tile(20,textures[20]);
<<<<<<< HEAD
        	//map[i][0].setSolid();
=======
<<<<<<< HEAD
        	//map[i][0].setSolid();
=======
        	map[i][0].setSolid();
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
        	
        }
        for(int i=0; i<height; i++) {
        	map[i][height-1] =new Tile(20,textures[20]);
<<<<<<< HEAD
        	//map[i][height-1].setSolid();
=======
<<<<<<< HEAD
        	//map[i][height-1].setSolid();
=======
        	map[i][height-1].setSolid();
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
        }
   
        for(int i=0; i<width; i++) {
        	map[width-1][i] = new Tile(20,textures[20]);
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
        	//map[width-1][i].setSolid();
        }
       
        
        
       // openMaze();
        
       // while(m.hasNext()) {
    	//	for(int i=0; i<100; i++) {
    	//		for(int j=0; j<100; j++) {
    	//			if(m.nextInt()==1) {
    	//			map[j][i]=new Tile(20,textures[20]);
    	//			map[j][i].setSolid();
    	//			}
    	//			else {
    	//				
    	//			}
    	//		}
    	//	}
    //	}
        
        
      //  closeMaze();
        
       
        
        
        
        
=======
        	map[width-1][i].setSolid();
        }
        
        
         openMaze();
        
        while(m.hasNext()) {
    		for(int i=0; i<100; i++) {
    			for(int j=0; j<100; j++) {
    				if(m.nextInt()==1) {
    				map[j][i]=new Tile(20,textures[20]);
    				map[j][i].setSolid();
    				}
    				else {
    					
    				}
    			}
    		}
    	}
        
        
        closeMaze();
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
        
        
        
        
<<<<<<< HEAD
       
// Cool feature to add is delete solid tiles as time goes by

<<<<<<< HEAD
=======
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
        return map;
    }
    
    public void openMaze() {
    	try {
<<<<<<< HEAD
    		m= new Scanner(new File("project/assets/maze.m"));
=======
<<<<<<< HEAD
    		m= new Scanner(new File("/Users/seongheehan/Documents/myst/project/assets/maze.m"));
=======
    		m= new Scanner(new File("/Maze.m"));
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
   
    	}catch(Exception e) {
    		System.out.println("Fatal Error: missing maze data");
    	}
    }

    
    public void closeMaze() {
    	m.close();
    }

<<<<<<< HEAD
=======
<<<<<<< HEAD

=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
}
