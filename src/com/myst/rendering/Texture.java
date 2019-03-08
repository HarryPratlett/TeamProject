package com.myst.rendering;

import static org.lwjgl.opengl.GL20.*;

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

    public Texture(String filepath){
//        don't know why this is used but ImageIO doesn't work
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

//        try{

            this.data = STBImage.stbi_load(filepath,width,height,comp,4);
            this.filepath = filepath;
            id = glGenTextures();
            this.width = width.get();
            this.height = height.get();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

//        }catch(){
//            e.printStackTrace();
//        }
    }

    public void bind(int sampler){
        if (sampler >= 0 && sampler <= 31) {
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
