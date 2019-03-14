package com.myst.world.entities;

import com.myst.networking.EntityData;
import com.myst.rendering.Model;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.AABB;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    protected Model model;
    public Transform transform;
    protected Texture texture;
  //used to change character
    protected int change;
    
    
    protected Texture texture2;
    protected Texture texture3;
    protected Texture texture4;
    protected Texture texture5;
   
    protected Texture texture6;
    protected Texture texture7;
    protected Texture texture8;
    protected Texture texture9;
   
    protected Texture texture10;   
    protected Texture texture11;
    protected Texture texture12;
    protected Texture texture13;
    
    protected Texture texture14;
    protected Texture texture15;
    protected Texture texture16;
    protected Texture texture17;
    public AABB boundingBox;
    private Shader shader;
    protected EntityTypes type;
    public String owner;
    public Integer localID;



    public Entity(float[] vertices, float[] texture, int[] indices, Vector2f boundingBoxCoords, Shader shader){
        model = new Model(vertices, texture, indices);
      //survior1_gun
    	this.texture =new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_gun.png");
    	this.texture2 = new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_gun_up.png");
    	this.texture3 = new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_gun_left.png");
    	this.texture4 = new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_gun.png");
    	this.texture5 = new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_gun_down.png");
    	//survior1_machine
    	this.texture6 = new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_machine_up.png");
    	this.texture7 = new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_machine_left.png");
    	this.texture8 = new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_machine_right.png");
    	this.texture9 = new Texture("assets/topdown-shooter/PNG/Survivor 1/survivor1_machine_down.png");
    	//manBlue_gun
    	this.texture10 = new Texture("assets/topdown-shooter/PNG/Man Blue/manBlue_gun_up.png");
    	this.texture11= new Texture("assets/topdown-shooter/PNG/Man Blue/manBlue_gun_left.png");
    	this.texture12= new Texture("assets/topdown-shooter/PNG/Man Blue/manBlue_gun_right.png");
    	this.texture13= new Texture("assets/topdown-shooter/PNG/Man Blue/manBlue_gun_down.png");
    	//manBlue_machine
    	this.texture14= new Texture("assets/topdown-shooter/PNG/Man Blue/manBlue_machine_up.png");
    	this.texture15= new Texture("assets/topdown-shooter/PNG/Man Blue/manBlue_machine_left.png");
    	this.texture16= new Texture("assets/topdown-shooter/PNG/Man Blue/manBlue_machine_right.png");
    	this.texture17= new Texture("assets/topdown-shooter/PNG/Man Blue/manBlue_machine_down.png");
    	
        this.shader = shader;
        transform = new Transform();
        transform.scale = new Vector3f(1,1,1);
        boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), boundingBoxCoords);
    }

    public abstract void update(float deltaTime, Window window, Camera camera, World world);

    public void render(Camera camera){
        this.shader.bind();
        this.shader.setUniform("sampler", 0);
        this.shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

//    used in networking to get the entity data
    public EntityData getData(){
        EntityData data = new EntityData();
        data.localID = this.localID;
        data.ownerID = this.owner;
        data.boundingBox = this.boundingBox;
        data.transform = this.transform;
        return data;
    }

    public void readInEntityData(EntityData data){
        this.owner = data.ownerID;
        this.localID = data.localID;
        this.transform = data.transform;
        this.boundingBox = data.boundingBox;
    }

}
