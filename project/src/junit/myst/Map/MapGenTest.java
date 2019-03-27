package junit.myst.Map;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.Test;
import com.myst.world.map.generating.MapGenerator;
import com.myst.world.map.rendering.Tile;



public class MapGenTest {

	@Test
	public void test() {
		 String[] textures = new String[20];
	        String path = ("assets/tileset/");
	        textures[0] = path + "tile_01";
	        textures[1] = path + "tile_02";
	        textures[2] = path + "tile_03";
	        textures[3] = path + "tile_04";
	        textures[4] = path + "tile_05";
	        textures[5] = path + "tile_06";
	        textures[6] = path + "tile_07";
	        textures[7] = path + "tile_08";
	        textures[8] = path + "tile_09";
	        textures[9] = path + "tile_10";
	        textures[10] = path + "tile_11";
	        textures[11] = path + "tile_12";
	        textures[12] = path + "tile_13";
	        textures[13] = path + "tile_14";
	        textures[14] = path + "tile_15";
	        textures[15] = path + "tile_16";
	        textures[16] = path + "tile_17";
	        textures[17] = path + "tile_18";
	        textures[18] = path + "tile_19";
	        textures[19] = path + "tile_20";

	        Tile[][] map = new MapGenerator(textures).generateMap(100, 100);
	        Tile[][] test = null;


		assertTrue(!Arrays.equals(map,test));


	}
	@Test
	public void readcolour() {
		String[] textures = new String[20];
        String path = ("assets/tileset/");
        textures[0] = path + "tile_01";
        textures[1] = path + "tile_02";
        textures[2] = path + "tile_03";
        textures[3] = path + "tile_04";
        textures[4] = path + "tile_05";
        textures[5] = path + "tile_06";
        textures[6] = path + "tile_07";
        textures[7] = path + "tile_08";
        textures[8] = path + "tile_09";
        textures[9] = path + "tile_10";
        textures[10] = path + "tile_11";
        textures[11] = path + "tile_12";
        textures[12] = path + "tile_13";
        textures[13] = path + "tile_14";
        textures[14] = path + "tile_15";
        textures[15] = path + "tile_16";
        textures[16] = path + "tile_17";
        textures[17] = path + "tile_18";
        textures[18] = path + "tile_19";
        textures[19] = path + "tile_20";

        Tile[][] map = new MapGenerator(textures).generateMap(100, 100);
        Tile[][] test = null;
        try {
			// File tileSheet = new
			// File("/Users/seongheehan/Documents/myst/project/assets/tile_sheet/map.png");
			// BufferedImage tile_sheet = ImageIO.read(tileSheet);
			int preset = (int) Math.floor(Math.random() * 4);
			System.out.println(preset);
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
			System.out.println(random);

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
			System.out.println(path+subpath+mapnum);
			BufferedImage tile_sheet = ImageIO.read(new File(path + subpath + mapnum));

			// 32 * 32 PNG
			// BufferedImage entity_sheet = ImageIO.read(new File(""));

			// width = tile_sheet.getWidth();
			// height = tile_sheet.getHeight();

			int[] colourTileSheet = tile_sheet.getRGB(0, 0, 32, 32, null, 0, 32);

			for (int y = 1; y < 33; y++) {
				for (int x = 1; x < 33; x++) {

					int red = (colourTileSheet[(x - 1) + (y - 1) * 32] >> 16) & 0xFF;

					assertTrue(red==8);
					assertTrue(red==219);
					assertTrue(red==227);
					assertTrue(red==35);
					assertTrue(red==251);
					assertTrue(red==28);
					assertTrue(red==1);
					assertTrue(red==88);




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

	}

}
