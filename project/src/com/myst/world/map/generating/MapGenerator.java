package com.myst.world.map.generating;

import java.awt.image.BufferedImage;
//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

//import javax.imageio.ImageIO;

import com.myst.datatypes.TileCoords;
import com.myst.rendering.Texture;
import com.myst.world.map.rendering.Tile;

//this need building on so we can potentially in future procedurally generate maps
public class MapGenerator {
	private Scanner m;

	// public static final Tile test_tile = new Tile( /*0,*/ "assets/tile_18");
	String[] textures;

	//    probably needs refactoring for future work however works for now
	public MapGenerator(String[] textures) {
		this.textures = textures;
	}

	public Tile[][] generateMap(int width, int height) {
		String path = "assets/tile_sheet/";

		Tile[][] map = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = new Tile(0, textures[0]);
			}
		}
		for (int i = 0; i < 1400; i++) {



		}
		// For Base Map Generating
		//각줄의 마지막 타일셋이 한칸 왼쪽으로 밀려있음 오른쪽으로 다시 밀어아햠
		try {

			BufferedImage tile_sheet = ImageIO.read(new File(path + "Map.png"));
			// 100 * 100 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {

					int red = (colourTileSheet[x + y * width] >> 16) & 0xFF;


					// water blue
					// R:8 G:26 B:253
					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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
			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
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

			// 32 * 32 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

			for (int y = 1; y < 33; y++) {
				for (int x = 1; x < 33; x++) {

					int red = (colourTileSheet[(x - 1) + (y - 1) * 32] >> 16) & 0xFF;



					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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

			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
			BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));

			// 32 * 32 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

			for (int y = 1; y < 33; y++) {
				for (int x = 34; x < 66; x++) {

					int red = (colourTileSheet[(x-34) + (y-1) * 32] >> 16) & 0xFF;


					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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

			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
			BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));

			// 100 * 100 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

			for (int y = 1; y < 33; y++) {
				for (int x = 67; x < 99; x++) {

					int red = (colourTileSheet[(x-67) + (y-1) * 32] >> 16) & 0xFF;



					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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
			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
			BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));

			// 32 * 32 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

			for (int y = 34; y < 66; y++) {
				for (int x = 1; x < 33; x++) {

					int red = (colourTileSheet[(x-1) + (y-34) * 32] >> 16) & 0xFF;

					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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

			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
			BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));

			// 32 * 32 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

			for (int y = 34; y < 66; y++) {
				for (int x = 67; x < 99; x++) {

					int red = (colourTileSheet[(x-67) + (y-34) * 32] >> 16) & 0xFF;


					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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
			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
			BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));

			// 32 * 32 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

			for (int y = 67; y < 99; y++) {
				for (int x = 1; x < 33; x++) {

					int red = (colourTileSheet[(x-1) + (y-67) * 32] >> 16) & 0xFF;

					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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
			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
			BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));

			// 32 * 32 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0,32);

			for (int y = 67; y < 99; y++) {
				for (int x = 34; x < 66; x++) {

					int red = (colourTileSheet[(x-34) + (y-67) * 32] >> 16) & 0xFF;

					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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
			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
			BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));

			// 32 * 32 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

			for (int y = 67; y < 99; y++) {
				for (int x = 67; x < 99; x++) {

					int red = (colourTileSheet[(x-67) + (y-67) * 32] >> 16) & 0xFF;



					if (red == 8) {
						map[x][y] = new Tile(12, textures[13]);
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
					if (red == 36) {
						map[x][y] = new Tile(15, textures[16]);
						map[x][y].setSolid();
					}
					// Brick RED
					// R: 251 G: 0 B: 24
					if (red == 251) {
						map[x][y] = new Tile(11, textures[12]);
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

		// for (int i = 0; i < width; i++) {
		// map[0][i] = new Tile(20, textures[20]);
		// map[0][i].setSolid();
		// }

		// for (int i = 0; i < height; i++) {
		// map[i][0] = new Tile(20, textures[20]);
		// map[i][0].setSolid();
//
//		}
//		for (int i = 0; i < height; i++) {
//			map[i][height - 1] = new Tile(20, textures[20]);
//			map[i][height - 1].setSolid();
//		}
//
//		for (int i = 0; i < width; i++) {
//			map[width - 1][i] = new Tile(20, textures[20]);
//			map[width - 1][i].setSolid();
//		}

		// openMaze();

		// while (m.hasNext()) {
		// for (int i = 0; i < 100; i++) {
		// for (int j = 0; j < 100; j++) {
		// if (m.nextInt() == 1) {
		// map[j][i] = new Tile(20, textures[20]);
		// map[j][i].setSolid();
		// } else {
		//
		// }
		// }
		// }
		// }

		// closeMaze();

		return map;

	}

	// public void openMaze() {
	// try {
//
//			m = new Scanner(new File("/Maze.m"));
//
//		} catch (Exception e) {
//			System.out.println("Fatal Error: missing maze data");
//		}
//	}
//
//	public void closeMaze() {
//		m.close();
//	}

}
