package com.myst.rendering;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;

/**
 * Creates a model to render an image
 */
public class Model {
    private int draw_count;
//    vbox id
    private int v_id;
//    texture id
    private int t_id;
//    indices id
    private int i_id;

    /**
     * Constructor to create the model
     * @param vertices Vertices for image/projection
     * @param tex_coords ''
     * @param indices ''
     */
    public Model(float[] vertices, float[] tex_coords, int[] indices){

        draw_count = indices.length;

        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices) , GL_STATIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,t_id);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(tex_coords), GL_STATIC_DRAW);

        i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indices), GL_STATIC_DRAW);


        glBindBuffer(GL_ARRAY_BUFFER,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);

    }

    /**
     * Draws the model
     */
    public void render(){

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER,v_id);

        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);

        glBindBuffer(GL_ARRAY_BUFFER,t_id);

        glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,i_id);
        glDrawElements(GL_TRIANGLES, draw_count,GL_UNSIGNED_INT,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    /**
     * Creates the float buffer
     * @param data Data for the float buffer
     * @return Returns the float buffer
     */
    private FloatBuffer createFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);

        buffer.flip();
        return buffer;
    }

    /**
     * Creates an int buffer
     * @param data Data for the int buffer
     * @return Returns the int buffer
     */
    private IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);

        buffer.flip();
        return buffer;
    }
}
