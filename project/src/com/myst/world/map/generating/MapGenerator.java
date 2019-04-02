package com.myst.world.map.generating;

import com.myst.world.map.rendering.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Seonghee Han
 * @version 3.2
 */
public class MapGenerator {
    String[] textures;

    /**
     * @param textures: textures are coming from sever and it contains the tiles in the array.
     */
    public MapGenerator(String[] textures) {
        this.textures = textures;
    }

    /**
     * @param width:  width of the map
     * @param height: height of the map
     * @return 2D array which correspond to world coordinate system.
     * @see <br>For base map
     * <br>100 * 100 PNG
     * <br>consists of 8 sub-grid
     * <br>in the middle there is pond.(not procedurally generating/ only centre)
     * <br>Sub-grid for procedurally generating
     * <br>sub-grid consists of 32 * 32 PNG file
     * <br>________________
     * <br>|1   |2   |3   |
     * <br>|____|____|____|
     * <br>|4   |    |5   |
     * <br>|____|____|____|
     * <br>|6   |7   |8   |
     * <br>|____|____|____|
     * <ol>
     * <li>1st sub-grid x=1 to x=32, y=1 to y=32
     * <li>2nd sub-grid x=34 to x=65, y=1 to y=32
     * <li>3rd sub-grid x=67 to x=98, y=1 to y=32
     * <li>4th sub-grid x=1 to x=32, y=34 to y=65
     * <li>5th sub-grid x=67 to x=98, y=34 to y=65
     * <li>6th sub-grid x=1 to x=32, y=67 to y=98
     * <li>7th sub-grid x=34 to x=65, y=67 to y=98
     * <li>8th sub-grid x=67 to x=98, y=67 to y=98
     * </ol>
     * <br>Colour Reading
     * <br>Base MAP Colour Black
     * <br>R:1 G:1 B:1
     * <p>
     * <br>Maze water object
     * <br>R:8 G:26 B:253
     * <p>
     * <br>boundary pink
     * <br>R:219 G:26 B:253
     * <p>
     * <br>yellow entrance/route
     * <br>R:227 G:255 B: 16
     * <p>
     * <br>Forest green
     * <br>R:227 G:255 B: 16
     * <p>
     * <br>Brick RED
     * <br>R: 251 G: 0 B: 24
     * <p>
     * <br>Maze CYAN
     * <br>R:28 G:201 B:255
     * <p>
     * <br>Centre water object
     * <br>R: 120 G: 0 B: 0
     * <p>
     * <br>Tiles have corner empty for the player entity
     * <br>All of the tiles have empty edges.
     */
    public Tile[][] generateMap(int width, int height) {
        String path = "project/assets/tile_sheet/";
        Tile[][] map = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = new Tile(0, textures[0]);
            }
        }

        try {
            BufferedImage tile_sheet = ImageIO.read(new File(path + "Map.png"));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    int red = (colourTileSheet[x + y * width] >> 16) & 0xFF;

                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:35 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 120 G: 0 B: 0
                    if (red == 120) {
                        map[x][y] = new Tile(11, textures[12]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        // 1st sub_map coordinate x=1 to x=32, y=1 to y=32
        try {
            int preset = (int) Math.floor(Math.random() * 4);
            System.out.println(preset);
            System.out.println(preset);
            String subPath = null;

            switch (preset) {
                case 0:
                    subPath = "brick/";
                    break;
                case 1:
                    subPath = "forest/";
                    break;
                case 2:
                    subPath = "pond/";
                    break;
                case 3:
                    subPath = "maze/";
                    break;
                default:
                    break;
            }

            int random = (int) Math.floor(Math.random() * 3);
            String mapNum = null;

            switch (random) {
                case 0:
                    mapNum = "1.png";
                    break;
                case 1:
                    mapNum = "2.png";
                    break;
                case 2:
                    mapNum = "3.png";
                    break;
                default:
                    break;
            }

            BufferedImage tile_sheet = ImageIO.read(new File(path + subPath + mapNum));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

            for (int y = 1; y < 33; y++) {
                for (int x = 1; x < 33; x++) {

                    int red = (colourTileSheet[(x - 1) + (y - 1) * 32] >> 16) & 0xFF;

                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:36 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 88 G: 0 B: 253
                    if (red == 88) {
                        map[x][y] = new Tile(12, textures[14]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2nd Sub_map
        try {
            int preset = (int) Math.floor(Math.random() * 4);
            String subPath = null;
            switch (preset) {
                case 0:
                    subPath = "brick/";
                    break;
                case 1:
                    subPath = "forest/";
                    break;
                case 2:
                    subPath = "pond/";
                    break;
                case 3:
                    subPath = "maze/";
                    break;
                default:
                    break;
            }

            int random = (int) Math.floor(Math.random() * 3);
            String mapNum = null;

            switch (random) {
                case 0:
                    mapNum = "1.png";
                    break;
                case 1:
                    mapNum = "2.png";
                    break;
                case 2:
                    mapNum = "3.png";
                    break;
                default:
                    break;
            }

            BufferedImage tile_sheet = ImageIO.read(new File(path + subPath + mapNum));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

            for (int y = 1; y < 33; y++) {
                for (int x = 34; x < 66; x++) {

                    int red = (colourTileSheet[(x - 34) + (y - 1) * 32] >> 16) & 0xFF;


                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:35 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 88 G: 0 B: 253
                    if (red == 88) {
                        map[x][y] = new Tile(12, textures[14]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3rd sub_map
        try {
            int preset = (int) Math.floor(Math.random() * 4);
            String subPath = null;
            switch (preset) {
                case 0:
                    subPath = "brick/";
                    break;
                case 1:
                    subPath = "forest/";
                    break;
                case 2:
                    subPath = "pond/";
                    break;
                case 3:
                    subPath = "maze/";
                    break;
                default:
                    break;
            }

            int random = (int) Math.floor(Math.random() * 3);
            String mapNum = null;

            switch (random) {
                case 0:
                    mapNum = "1.png";
                    break;
                case 1:
                    mapNum = "2.png";
                    break;
                case 2:
                    mapNum = "3.png";
                    break;
                default:
                    break;
            }

            BufferedImage tile_sheet = ImageIO.read(new File(path + subPath + mapNum));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

            for (int y = 1; y < 33; y++) {
                for (int x = 67; x < 99; x++) {

                    int red = (colourTileSheet[(x - 67) + (y - 1) * 32] >> 16) & 0xFF;

                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:35 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 88 G: 0 B: 253
                    if (red == 88) {
                        map[x][y] = new Tile(12, textures[14]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4th sub_map
        try {
            int preset = (int) Math.floor(Math.random() * 4);
            String subPath = null;
            switch (preset) {
                case 0:
                    subPath = "brick/";
                    break;
                case 1:
                    subPath = "forest/";
                    break;
                case 2:
                    subPath = "pond/";
                    break;
                case 3:
                    subPath = "maze/";
                    break;
                default:
                    break;
            }

            int random = (int) Math.floor(Math.random() * 3);
            String mapNum = null;

            switch (random) {
                case 0:
                    mapNum = "1.png";
                    break;
                case 1:
                    mapNum = "2.png";
                    break;
                case 2:
                    mapNum = "3.png";
                    break;
                default:
                    break;
            }

            BufferedImage tile_sheet = ImageIO.read(new File(path + subPath + mapNum));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

            for (int y = 34; y < 66; y++) {
                for (int x = 1; x < 33; x++) {

                    int red = (colourTileSheet[(x - 1) + (y - 34) * 32] >> 16) & 0xFF;

                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:35 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 88 G: 0 B: 253
                    if (red == 88) {
                        map[x][y] = new Tile(12, textures[14]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5th map
        try {
            int preset = (int) Math.floor(Math.random() * 4);
            String subPath = null;
            switch (preset) {
                case 0:
                    subPath = "brick/";
                    break;
                case 1:
                    subPath = "forest/";
                    break;
                case 2:
                    subPath = "pond/";
                    break;
                case 3:
                    subPath = "maze/";
                    break;
                default:
                    break;
            }

            int random = (int) Math.floor(Math.random() * 3);
            String mapNum = null;

            switch (random) {
                case 0:
                    mapNum = "1.png";
                    break;
                case 1:
                    mapNum = "2.png";
                    break;
                case 2:
                    mapNum = "3.png";
                    break;
                default:
                    break;
            }

            BufferedImage tile_sheet = ImageIO.read(new File(path + subPath + mapNum));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

            for (int y = 34; y < 66; y++) {
                for (int x = 67; x < 99; x++) {

                    int red = (colourTileSheet[(x - 67) + (y - 34) * 32] >> 16) & 0xFF;

                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:35 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 88 G: 0 B: 253
                    if (red == 88) {
                        map[x][y] = new Tile(12, textures[14]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 6th sub_map
        try {
            int preset = (int) Math.floor(Math.random() * 4);
            String subPath = null;
            switch (preset) {
                case 0:
                    subPath = "brick/";
                    break;
                case 1:
                    subPath = "forest/";
                    break;
                case 2:
                    subPath = "pond/";
                    break;
                case 3:
                    subPath = "maze/";
                    break;
                default:
                    break;
            }

            int random = (int) Math.floor(Math.random() * 3);
            String mapNum = null;

            switch (random) {
                case 0:
                    mapNum = "1.png";
                    break;
                case 1:
                    mapNum = "2.png";
                    break;
                case 2:
                    mapNum = "3.png";
                    break;
                default:
                    break;
            }

            BufferedImage tile_sheet = ImageIO.read(new File(path + subPath + mapNum));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

            for (int y = 67; y < 99; y++) {
                for (int x = 1; x < 33; x++) {

                    int red = (colourTileSheet[(x - 1) + (y - 67) * 32] >> 16) & 0xFF;

                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:35 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 88 G: 0 B: 253
                    if (red == 88) {
                        map[x][y] = new Tile(12, textures[14]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 7th sub_map
        try {
            int preset = (int) Math.floor(Math.random() * 4);
            String subPath = null;
            switch (preset) {
                case 0:
                    subPath = "brick/";
                    break;
                case 1:
                    subPath = "forest/";
                    break;
                case 2:
                    subPath = "pond/";
                    break;
                case 3:
                    subPath = "maze/";
                    break;
                default:
                    break;
            }

            int random = (int) Math.floor(Math.random() * 3);
            String mapNum = null;

            switch (random) {
                case 0:
                    mapNum = "1.png";
                    break;
                case 1:
                    mapNum = "2.png";
                    break;
                case 2:
                    mapNum = "3.png";
                    break;
                default:
                    break;
            }

            BufferedImage tile_sheet = ImageIO.read(new File(path + subPath + mapNum));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

            for (int y = 67; y < 99; y++) {
                for (int x = 34; x < 66; x++) {

                    int red = (colourTileSheet[(x - 34) + (y - 67) * 32] >> 16) & 0xFF;

                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:35 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 88 G: 0 B: 253
                    if (red == 88) {
                        map[x][y] = new Tile(12, textures[14]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 8th Sub_map
        try {
            int preset = (int) Math.floor(Math.random() * 4);
            String subpath = null;
            switch (preset) {
                case 0:
                    subpath = "brick/";
                    break;
                case 1:
                    subpath = "forest/";
                    break;
                case 2:
                    subpath = "pond/";
                    break;
                case 3:
                    subpath = "maze/";
                    break;
                default:
                    break;
            }

            int random = (int) Math.floor(Math.random() * 3);
            String mapnum = null;

            switch (random) {
                case 0:
                    mapnum = "1.png";
                    break;
                case 1:
                    mapnum = "2.png";
                    break;
                case 2:
                    mapnum = "3.png";
                    break;
                default:
                    break;
            }

            BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));
            int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

            for (int y = 67; y < 99; y++) {
                for (int x = 67; x < 99; x++) {

                    int red = (colourTileSheet[(x - 67) + (y - 67) * 32] >> 16) & 0xFF;

                    //Maze water object
                    //R:8 G:26 B:253
                    if (red == 8) {
                        map[x][y] = new Tile(17, textures[18]);
                        map[x][y].setSolid();
                    }
                    // boundary pink
                    // R:219 G: 26 B:253
                    if (red == 219) {
                        map[x][y] = new Tile(7, textures[8]);
                    }
                    // yellow entrance/route
                    // R:227 G:255 B: 16
                    if (red == 227) {
                        map[x][y] = new Tile(9, textures[10]);
                    }
                    // Forest green
                    // R:35 G:255 B: 42
                    if (red == 35) {
                        map[x][y] = new Tile(18, textures[19]);
                        map[x][y].setSolid();
                    }
                    // Brick RED
                    // R: 251 G: 0 B: 24
                    if (red == 251) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Maze CYAN
                    // R:28 G:201 B:255
                    if (red == 28) {
                        map[x][y] = new Tile(10, textures[11]);
                        map[x][y].setSolid();
                    }
                    // Base MAP Colour Black
                    // R:1 G:1 B:1
                    if (red == 1) {
                        map[x][y] = new Tile(8, textures[9]);
                        map[x][y].setSolid();
                    }
                    // Centre water object
                    // R: 88 G: 0 B: 253
                    if (red == 88) {
                        map[x][y] = new Tile(12, textures[14]);
                        map[x][y].setSolid();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}


