/**
 * @author Harry Pratlett
 */
package com.myst.GUI;

import com.myst.input.Input;
import com.myst.rendering.Model;
import com.myst.rendering.Shader;
import com.myst.rendering.Texture;
import com.myst.rendering.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;

public class Overlay {
    private int health;
    private int ammo;

    private Window window;
    private Input input;

    private final String PATH = "assets/gui/main_menu/typing/";

    private final float[] baseVertices = new float[] {
            -1f, 0.5f, 0f, /*0*/  1f, 0.5f, 0f, /*1*/    1f, -0.5f, 0f, /*2*/
            -1f, -0.5f, 0f/*3*/
    };

    private float[] textureDocks = new float[] {
            0f, 0f,   1, 0f,  1f, 1f,
            0f, 1f
    };

    private int[] indices = new int[] {
            0,1,2,
            2,3,0
    };

    /**
     * Default constructor that takes the window its an overlay for, the input for that window and default values for health and ammo
     * @param window Window that the overlay is on
     * @param input Input listener for that window
     * @param health Default health value
     * @param ammo Default ammo value
     */
    public Overlay(Window window, Input input, int health, int ammo)    {
        this.window = window;
        this.health = health;
        this.ammo = ammo;
        this.input = input;


    }

    private Texture[] numberTextures = new Texture[]{
            new Texture(PATH + "0.png"), new Texture(PATH + "1.png"),
            new Texture(PATH + "2.png"), new Texture(PATH + "3.png"),
            new Texture(PATH + "4.png"), new Texture(PATH + "5.png"),
            new Texture(PATH + "6.png"), new Texture(PATH + "7.png"),
            new Texture(PATH + "8.png"), new Texture(PATH + "9.png"),
    };

    /**
     * Renders the ammo and health overlays
     * @param shader The shader used to render them
     */
    public void render(Shader shader)    {
        String health = Integer.toString(this.health);
        String ammo = Integer.toString(this.ammo);
        float xHealth = 0.5f;
        float xAmmo = 0.5f;

        for(int i = 0; i < health.length(); i++)  {
            float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
            vertices = this.alterVertices(vertices, new Texture(PATH + "0.png").getHeight(), numberTextures[Integer.valueOf(health.substring(i, i+1))].getWidth(), 0.002, 0.005);
            Model model = new Model(vertices, textureDocks, indices);
            this.renderImage(shader, new Texture(PATH + "0.png"), xHealth, 0.9f, new Matrix4f(), model);
//            xHealth += 0.1f;
        }
//        for(int i = 0; i < ammo.length(); i++)   {
//            float[] vertices = Arrays.copyOf(baseVertices, baseVertices.length);
//            vertices = this.alterVertices(vertices, numberTextures[Integer.valueOf(health.substring(i, i+1))].getHeight(), numberTextures[Integer.valueOf(health.substring(i, i+1))].getWidth(), 0.002, 0.005);
//            Model model = new Model(vertices, textureDocks, indices);
//            this.renderImage(shader, numberTextures[Integer.valueOf(ammo.substring(i, i+1))], xAmmo, 0.75f, new Matrix4f(), model);
//            xAmmo += 0.1f;
//        }
    }

    /**
     * Updates the players health and ammo
     * @param playerHealth The updated health
     */
    public void update(int playerHealth)    {
        this.health = playerHealth;
    }

    /**
     * Renders a given image on the screen
     * @param shader Shader used to render the image
     * @param texture Texture that will be rendered
     * @param x X co-ordinate of texture
     * @param y Y co-ordinate of texture
     * @param scale Scale of image
     * @param model Model of the image that is used to render the image
     */
    public void renderImage(Shader shader, Texture texture, float x, float y, Matrix4f scale, Model model){
        shader.bind();
        texture.bind(0);
        Matrix4f target = new Matrix4f();

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x,y,0));
        scale.mul(tile_pos, target);


        shader.setUniform("sampler",0);
        shader.setUniform("projection", target);
        model.render();
    }

    /**
     * Alters a given float array of vertices to fit an image
     * @param vertices The float array to be altered
     * @param height The height of the image it is being altered for
     * @param width The width of the image it is being altered for
     * @param widthScale The scale of the subsequent rendering
     * @param heightScale The scale of the subsequent rendering
     * @return Returns an altered float array from these parameters
     */
    public float[] alterVertices(float[] vertices, int height, int width, double widthScale, double heightScale) {
        vertices[0] *= width * widthScale;
        vertices[3] *= width * widthScale;
        vertices[6] *= width * widthScale;
        vertices[9] *= width * widthScale;

        vertices[1] *= height * heightScale;
        vertices[4] *= height * heightScale;
        vertices[7] *= height * heightScale;
        vertices[10] *= height * heightScale;
        return vertices;
    }
}
