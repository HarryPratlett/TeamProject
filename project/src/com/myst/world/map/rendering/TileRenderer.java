/**
 * @author Aled Jackson
 */
package com.myst.world.map.rendering;

import com.myst.datatypes.TileCoords;
import com.myst.datatypes.WorldCoords;
import com.myst.rendering.Shader;
import com.myst.world.view.Camera;
import com.myst.rendering.Model;
import com.myst.rendering.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

import static com.myst.helper.Flatten.flatten;

/**
 * Renders tiles
 */
public class  TileRenderer {
    private HashMap<String, Texture> tileTextures;
    private Model model;
    private int width;
    private int height;


    public TileRenderer(/*Tile[][] tilemap ,*/ String[] textures){
        tileTextures = new HashMap<String,Texture>();

        float[] vertices = new float[] {
                -0.5f, 0.5f, 0f,    /*0*/
                0.5f, 0.5f, 0f,    /*1*/
                0.5f, -0.5f, 0f,  /*2*/
                -0.5f, -0.5f, 0f /*3*/
        };

        float[] texture = new float[] {
                0f, 0f,
                1, 0f,
                1f, 1f,
                0f, 1f
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };

        model = new Model(vertices, texture, indices);

        for(int i=0; i < textures.length; i++){
            tileTextures.put(textures[i], new Texture(textures[i] + ".png"));
        }

    }


    /**
     * Renders a given tile
     * @param tile The tile to be rendered
     * @param coords The co-ordinates of the tiles
     * @param shader Shader which they will be rendered upon
     * @param cam Camera that the user has
     */
    public void renderTile(Tile tile, TileCoords coords, Shader shader, Camera cam){
        shader.bind();
        if(tileTextures.containsKey(tile.getTexture())) {
            tileTextures.get(tile.getTexture()).bind(0);
        }

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(coords.x,-coords.y,0));
        Matrix4f target = new Matrix4f();


        cam.getProjection().mul(new Matrix4f(),target);
        target.mul(tile_pos);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);
        model.render();
    }







}
