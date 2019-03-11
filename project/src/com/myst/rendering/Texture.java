package com.myst.rendering;

import static org.lwjgl.opengl.GL20.*;
import com.myst.animation.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;



public class Texture {
	
    private int id;
    private int width;
    private int height;
    private String filepath;
    private ByteBuffer data;

    //Texture class for entity
   
    public Texture(String filepath){

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        this.data = STBImage.stbi_load(filepath,width,height,comp,4);
        this.filepath = filepath;
           
        id = glGenTextures();//assign id for texture
            
        this.width = width.get();
        this.height = height.get();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);


    }

    
    //glBindTexture is used to bind texture to target
    //we may need to bind different different texture to one certain target
    //so we need to change below definition 
    //Note:glactivetexture is used to active texture unit
    //specify which texture is bound to target
    public void bind(int sampler){
    	
        if (sampler >= 0 && sampler <= 31) {
        	
        	//glactivetexture is used before glbindtexture
        	
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    public int getHeight() {
        return this.height;
    }
    public int getWidth()  {
        return this.width;
    }
    public String getPath() {
        String[] path = this.filepath.split("/");
        return path[path.length-1];
    }



}
