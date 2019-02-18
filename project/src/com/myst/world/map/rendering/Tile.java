package com.myst.world.map.rendering;

<<<<<<< HEAD
public class Tile {
//    tiles is the different tiles that exist
    public static int nOfTiles = 0;
    private boolean solid;

=======

public class Tile {
//    tiles is the different tiles that exist
    public static int nOfTiles = 0;
	//*
    public boolean solid;
    
    
    
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
//    public static final Tile test_tile = new Tile(  /*0,*/  "assets/tile_18");
//    public static final Tile test_tile2 = new Tile(/*1,*/"assets/tile_186");
//    need error checking to make sure that the texture has loaded

<<<<<<< HEAD
    private int id;
    private String texture;
=======
    protected final int id;//*never change an id
    
    private String texture;
	
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2

    public Tile(int id, String texture){
        nOfTiles++;
        this.id = id;
        this.texture = texture;

//        if(tiles[id] != null){
//            throw new IllegalStateException("Tiles at: " + id + " is already being used");
//        }
//        tiles[id] = this;

<<<<<<< HEAD
        this.solid = false;
    }

    public Tile setSolid(){this.solid = true; return this;}

    public boolean isSolid(){return this.solid;}

=======
        //*
         this.solid = true;//if change this to true, the player would standing outside the wall;
    }
    
    
    public Tile setSolid(){this.solid =true ; return this;}
    public Tile unsetSolid(){this.solid =false ; return this;}
    public boolean isSolid(){return this.solid;}//**

    
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
    public int getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
<<<<<<< HEAD
=======
    
 
	public Object getX() {
		// TODO Auto-generated method stub
		return null;
	}
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
}
