package com.myst.rendering;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import com.myst.animation.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texturenew {
	
	  private int id;
	  private int width;
	  private int height;
	  public String filepath;
	  private ByteBuffer data;
	  
	  public Texturenew(String filepath) {
		  
		  IntBuffer width = BufferUtils.createIntBuffer(1);
	      IntBuffer height = BufferUtils.createIntBuffer(1);
	      IntBuffer comp = BufferUtils.createIntBuffer(1);
	      this.width = width.get();
	      this.height = height.get();
	      
	      id= glGenTextures();
	     
	      this.data = STBImage.stbi_load(filepath,width,height,comp,4);
	      this.filepath = filepath;
	      glBindTexture(GL_TEXTURE_2D, id);

	      glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	      glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER, GL_NEAREST);

	      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

		  
	  }

      public void imageload(int i) {
    	  

     }
	    public void bind(int sampler){
	        if (sampler >= 0 && sampler <= 31) {	        		        		        	
	            glActiveTexture(GL_TEXTURE0 + sampler);  //glactivetexture is used before glbindtexture
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
