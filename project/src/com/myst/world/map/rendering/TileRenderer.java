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

public class  TileRenderer {
    private HashMap<String, Texture> tileTextures;
    private Model model;
//    public Tile[][] tileMap;
    private int width;
    private int height;

//    tile map should potentially be moved to world
//    instead all the textures in tilemap should be passed through
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

//    this renders a given tile at location x y
//    x and y should be according to the new co-ordinate system
//    where x is positive and y is negative

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

//        translates the tiles according to the camera
        cam.getProjection().mul(new Matrix4f(),target);
        target.mul(tile_pos);

//        target is the location of the tile
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);
        model.render();
    }







}
