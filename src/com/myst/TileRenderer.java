package com.myst;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class TileRenderer {
    private HashMap<String, Texture> tileTextures;
    private Model model;

    public TileRenderer(){
        tileTextures = new HashMap<String,Texture>();

        float[] vertices = new float[] {
                -0.5f, 0.5f, 0f, /*0*/  0.5f, 0.5f, 0f, /*1*/    0.5f, -0.5f, 0f, /*2*/
                -0.5f, -0.5f, 0f/*3*/
        };


        float[] texture = new float[] {
                0f, 0f,   1, 0f,  1f, 1f,
                0f, 1f
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };

        model = new Model(vertices, texture, indices);
        for (int i=0; i < Tile.tiles.length; i++){
            if(Tile.tiles[i] != null) {
                if (!tileTextures.containsKey(Tile.tiles[i].getTexture())) {
                    String tex = Tile.tiles[i].getTexture();
                    tileTextures.put(tex, new Texture(tex + ".png"));
                }
            }
        }
    }
    public void renderTile(byte id, int x, int y, Shader shader, Matrix4f world, Camera cam){
        shader.bind();
        if(tileTextures.containsKey(Tile.tiles[id].getTexture())) {
            tileTextures.get(Tile.tiles[id].getTexture()).bind(0);

        }


        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x*2,y*2,0));
        Matrix4f target = new Matrix4f();

        cam.getProjection().mul(world,target);
        target.mul(tile_pos);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);
        model.render();
    }
}
