package com.myst.world.map.rendering;


public class Tile {
//    tiles is the different tiles that exist
    public static int nOfTiles = 0;
	//*
    public boolean solid;
   
    protected final int id;//*never change an id
    
    private String texture;
	
    public Tile(int id, String texture){
        nOfTiles++;
        this.id = id;
        this.texture = texture;
        this.solid = false;
    }

    
    
    public Tile setSolid(){this.solid =true ; return this;}
    public Tile unsetSolid(){this.solid =false ; return this;}
    public boolean isSolid(){return this.solid;}//**

    public int getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
 
	public Object getX() {
		// TODO Auto-generated method stub
		return null;
	}
}
