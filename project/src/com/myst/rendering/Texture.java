/**
 * @author Aled Jackson
 */
package com.myst.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;


/**
 ** Creates a format in which images can be read in and rendered via OpenGL
 */
public class Texture {
    private int id;
    private int width;
    private int height;
    private String filepath;
    private ByteBuffer data;

    /**
     * Default constructor for Texture
     * @param filepath Filepath of a image that will be rendered
     */
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

    /**
     * OpenGL function
     * @param sampler Merges images together if there are multiple
     */
    public void bind(int sampler){
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    /**
     * Gets the height of an image
     * @return The height of the image as an int
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Gets the width of the image
     * @return The width of the image as an int
     */
    public int getWidth()  {
        return this.width;
    }

    /**
     * Returns the path of the image
     * @return The path of the image as a string
     */
    public String getPath() {
        String[] path = this.filepath.split("/");
        return path[path.length-1];
    }



}
