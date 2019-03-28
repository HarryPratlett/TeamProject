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

    private Texture[] numberTextures = new Texture[]{
            new Texture("assets/main_menu/typing/0.png"), new Texture("assets/main_menu/typing/1.png"),
            new Texture("assets/main_menu/typing/2.png"), new Texture("assets/main_menu/typing/3.png"),
            new Texture("assets/main_menu/typing/4.png"), new Texture("assets/main_menu/typing/5.png"),
            new Texture("assets/main_menu/typing/6.png"), new Texture("assets/main_menu/typing/7.png"),
            new Texture("assets/main_menu/typing/8.png"), new Texture("assets/main_menu/typing/9.png"),
    };

    private Model model = new Model(baseVertices,textureDocks,indices);
    private float[] vertices;
    private int health;
    private int ammo;

    public Overlay(int initialHealth, int initialAmmo)    {
        this.health = initialHealth;
        this.ammo = initialAmmo;
    }

    public void render(Shader shader) {
        float xHealth = 0.55f;
        String strHealth = Integer.toString(health);
        String strAmmo = Integer.toString(ammo);
        Texture t;

        for (int i = 0; i < strHealth.length(); i++) {
            t = numberTextures[Integer.parseInt(strHealth.substring(i, i + 1))];
            vertices = Arrays.copyOf(baseVertices, baseVertices.length);
            vertices = this.alterVertices(vertices, t.getHeight(), t.getWidth(), 0.002, 0.005);
            model = new Model(vertices, textureDocks, indices);
            renderImage(shader, t, xHealth, 0.55f, new Matrix4f(), model);
            xHealth += 0.1f;
        }

    }

    public void update(int newHealth)  {
        this.health = newHealth;
    }

    public static void renderImage(Shader shader, Texture texture, float x, float y, Matrix4f scale, Model model){
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
