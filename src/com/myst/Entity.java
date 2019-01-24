package com.myst;

public abstract class Entity {
	
	private Texture texture;
	private Model model;
	//private ?PhysicsClass? physics 
	//maybe have a separate physics class that has a constructor that you pass a speed modifier or something so that each entity can have unique physics
	private boolean killable;
	private String name;
	private String entityType;
	
	public Entity(String name, Texture texture, Model model, String entityType) {
		this.name = name;
		this.texture = texture;
		this.model = model;
		//this.physics = physics
		this.entityType = entityType;
	}
	
	public boolean isKillable() {
		return killable;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Model getModel() {
		return model;
	}
	
	public String getType() {
		return entityType;
	}
	
	public String toString(){
		return name + "[" + entityType + "]";
	}
	
}
