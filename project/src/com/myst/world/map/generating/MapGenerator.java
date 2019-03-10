package com.myst.world.map.generating;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

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
		Tile[][] map = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = new Tile(0, textures[0]);
			}
		}
		for (int i = 0; i < 1400; i++) {

			int x1 = (int) Math.floor(Math.random() * 100);
			int y1 = (int) Math.floor(Math.random() * 100);
			int m1 = (int) Math.floor(Math.random() * 20);

			int TF1 = (int) Math.floor(Math.random() * 2);
			boolean extendedmap1;
			if (TF1 == 0) {
				extendedmap1 = false;
			} else
				extendedmap1 = true;

			int TF2 = (int) Math.floor(Math.random() * 2);
			boolean extendedmap2 = false;
			if (TF2 == 0) {
				extendedmap2 = false;
			} else
				extendedmap2 = true;

			// check the coordinate is valid or not
			if (x1 < width | 0 < x1) {
				if (y1 < height | 0 < y1) {
					map[x1][y1] = new Tile(m1 - 1, textures[m1]);

					if (x1 + 1 < height) {
						map[x1 + 1][y1] = new Tile(m1 - 1, textures[m1]);
						if (y1 + 1 < height) {
							map[x1 + 1][y1 + 1] = new Tile(m1 - 1, textures[m1]);
							map[x1 + 1][y1 + 1] = new Tile(m1 - 1, textures[m1]);
							map[x1][y1 + 1] = new Tile(m1 - 1, textures[m1]);

							// randomly choose size of tile map
							if (extendedmap1 = true) {
								if (x1 + 2 < width) {
									map[x1 + 2][y1] = new Tile(m1 - 1, textures[m1]);
									map[x1 + 2][y1 + 1] = new Tile(m1 - 1, textures[m1]);

									// randomly choose size of tile map
									if (extendedmap2 = true) {
										if (y1 + 2 < height) {
											map[x1 + 1][y1 + 2] = new Tile(m1 - 1, textures[m1]);
											map[x1 + 1][y1 + 2] = new Tile(m1 - 1, textures[m1]);
											map[x1][y1 + 2] = new Tile(m1 - 1, textures[m1]);
											map[x1 + 2][y1 + 2] = new Tile(m1 - 1, textures[m1]);
											map[x1 + 2][y1 + 2] = new Tile(m1 - 1, textures[m1]);
											map[x1][y1 + 2] = new Tile(m1 - 1, textures[m1]);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		try {
			File tileSheet = new File("assets/tile_sheet/map.png");
			BufferedImage tile_sheet = ImageIO.read(tileSheet);
			// 100 * 100 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int red = (colourTileSheet[x + y * width] >> 16) & 0xFF;

					if (red == 2) {

						map[x][y] = new Tile(20, textures[20]);
						map[x][y].setSolid();

					}

				}

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		for (int i = 0; i < width; i++) {
			map[0][i] = new Tile(20, textures[20]);
			map[0][i].setSolid();
		}

		for (int i = 0; i < height; i++) {
			map[i][0] = new Tile(20, textures[20]);
			map[i][0].setSolid();

		}
		for (int i = 0; i < height; i++) {
			map[i][height - 1] = new Tile(20, textures[20]);
			map[i][height - 1].setSolid();
		}

		for (int i = 0; i < width; i++) {
			map[width - 1][i] = new Tile(20, textures[20]);
			map[width - 1][i].setSolid();
		}

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

	public void openMaze() {
		try {

			m = new Scanner(new File("/Maze.m"));

		} catch (Exception e) {
			System.out.println("Fatal Error: missing maze data");
		}
	}

	public void closeMaze() {
		m.close();
	}

}

