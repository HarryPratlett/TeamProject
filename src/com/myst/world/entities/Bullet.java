package com.myst.world.entities;
import com.myst.rendering.Model;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import com.myst.world.World;
import com.myst.world.collisions.Line;
import com.myst.world.view.Camera;
import com.myst.world.view.Transform;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Bullet extends Entity {
   private static final float MAX_LENGTH = 20;
   private Texture texture;
   private Shader shader;
   private Line line;    public Bullet(Line line, float length, float damage){
       super( new float[]{
               -0.1f, 1f * length, 0f, /*0*/  0.1f, 1f * length, 0f, /*1*/    0.1f, 0.1f, 0f, /*2*/
               -0.1f, 0.1f, 0f/*3*/
       },new float[] {
                       0f, 0f,   1, 0f,  1f, 1f,
                       0f, 1f
       },
       new int[] {
               0,1,2,
               2,3,0
       },
       new Vector2f(0.5f,0.5f));
       this.line = line;
       this.type = EntityTypes.BULLET;
       this.lightDistance = 0f;
       this.lightSource = false;    }    public void update(float deltaTime, Window window, Camera camera, World world){    }
	
	public boolean attack(World world, int entityID) {
		// TODO Auto-generated method stub
		return false;
	}
}
