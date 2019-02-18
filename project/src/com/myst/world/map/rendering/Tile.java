package com.myst.world.map.rendering;

<<<<<<< HEAD
=======
<<<<<<< HEAD
public class Tile {
//    tiles is the different tiles that exist
    public static int nOfTiles = 0;
    private boolean solid;

=======
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59

public class Tile {
//    tiles is the different tiles that exist
    public static int nOfTiles = 0;
	//*
    public boolean solid;
    
<<<<<<< HEAD
=======
    
    
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
//    public static final Tile test_tile = new Tile(  /*0,*/  "assets/tile_18");
//    public static final Tile test_tile2 = new Tile(/*1,*/"assets/tile_186");
//    need error checking to make sure that the texture has loaded

<<<<<<< HEAD
=======
<<<<<<< HEAD
    private int id;
    private String texture;
=======
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
    protected final int id;//*never change an id
    
    private String texture;
	
<<<<<<< HEAD
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2

>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
    public Tile(int id, String texture){
        nOfTiles++;
        this.id = id;
        this.texture = texture;

//        if(tiles[id] != null){
//            throw new IllegalStateException("Tiles at: " + id + " is already being used");
//        }
//        tiles[id] = this;

<<<<<<< HEAD

=======
<<<<<<< HEAD
        this.solid = false;
    }

    public Tile setSolid(){this.solid = true; return this;}

    public boolean isSolid(){return this.solid;}

=======
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
        //*
         this.solid = true;//if change this to true, the player would standing outside the wall;
    }
    
    
    public Tile setSolid(){this.solid =true ; return this;}
    public Tile unsetSolid(){this.solid =false ; return this;}
    public boolean isSolid(){return this.solid;}//**

    
<<<<<<< HEAD
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
    public int getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
    
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
 
	public Object getX() {
		// TODO Auto-generated method stub
		return null;
	}
<<<<<<< HEAD
=======
>>>>>>> 2d7693e05ae7a0355ce8576eb2deac316cc812e2
>>>>>>> d16f8b6c5166ce944094769a6ee85d7743d22d59
}
