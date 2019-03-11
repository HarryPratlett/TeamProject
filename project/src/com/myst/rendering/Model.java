package com.myst.rendering;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.*;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Model {
    private int draw_count;
//    vbox id
    private int v_id;
//    texture id
    private int t_id;
//    indices id
    private int i_id;
 

    
    /*//
    public Model(float[] vertices, Texture[] texture, int[] indices){
//      divided by 2 because we have a 2 dimensional model
      draw_count = indices.length;


//      giving opengl the buffer to draw the model and assigning it an id
      v_id = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, v_id);
      glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices) , GL_STATIC_DRAW);

      t_id = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER,t_id);
      glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(texture), GL_STATIC_DRAW);

      i_id = glGenBuffers();
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indices), GL_STATIC_DRAW);

//      unbinding v_id
      glBindBuffer(GL_ARRAY_BUFFER,0);

      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);

  }
    //***mar 4
    private FloatBuffer createFloatBuffer(Texture[] texture){
        
    	FloatBuffer buffer = BufferUtils.createFloatBuffer(texture.length);
        buffer.(texture);
        
//        wierd thing, opengl just wants things flipped
        buffer.flip();
        return buffer;
        
    }    
    
    */
    
    
    
    
    
    
    
    public Model(float[] vertices, float[] tex_coords, int[] indices){
//        divided by 2 because we have a 2 dimensional model
        draw_count = indices.length;


//        giving opengl the buffer to draw the model and assigning it an id
        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices) , GL_STATIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,t_id);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(tex_coords), GL_STATIC_DRAW);

        i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indices), GL_STATIC_DRAW);

//        unbinding v_id
        glBindBuffer(GL_ARRAY_BUFFER,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);

    }

//    draws the model (vbo)
    public void render(){
//        enable and disable are just so opengl knows what you are doing
//        glEnableClientState(GL_VERTEX_ARRAY);
//        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

//        does the same as the two lines above
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER,v_id);
//        first argument is the dimensions
        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
//
        glBindBuffer(GL_ARRAY_BUFFER,t_id);
        //        the 2 refers to the two dimensions
        glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,i_id);
        glDrawElements(GL_TRIANGLES, draw_count,GL_UNSIGNED_INT,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER,0);
//
//        glDisableClientState(GL_VERTEX_ARRAY);
//        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
//        does the same as the two lines above
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    private FloatBuffer createFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
//        wierd thing, opengl just wants things flipped
        buffer.flip();
        return buffer;
    }

    private IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
//        wierd thing, opengl just wants things flipped
        buffer.flip();
        return buffer;
    }
}
