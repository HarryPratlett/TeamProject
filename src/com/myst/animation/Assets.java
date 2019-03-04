//Animation part
package com.myst.animation;

//**FINISH

import java.awt.image.BufferedImage;

//load image from png type
//Pre-set 3 player type
//2 types of guns,each direction have 3 kinds images
public class Assets {
	        private static final int width = 32, height = 32;
	//for player1
			public static BufferedImage[]p1_down,p1_up,p1_left,p1_right,
			p1_w1_down,p1_w1_up,p1_w1_right,p1_w1_left,
			p1_w2_down,p1_w2_up,p1_w2_right,p1_w2_left;

			//for player2
			public static BufferedImage[]p2_down,p2_up,p2_left,p2_right,
			p2_w1_down,p2_w1_up,p2_w1_right,p2_w1_left,
			p2_w2_down,p2_w2_up,p2_w2_right,p2_w2_left;

			//for player3
			public static BufferedImage[]p3_down,p3_up,p3_left,p3_right,
			p3_w1_down,p3_w1_up,p3_w1_right,p3_w1_left,
			p3_w2_down,p3_w2_up,p3_w2_right,p3_w2_left;
	public static void init() {
		
		
		
		//player1
		p1_up=new BufferedImage[1];
		p1_down=new BufferedImage[1];
		p1_left=new BufferedImage[1];
		p1_right=new BufferedImage[1];
		p1_w1_up=new BufferedImage[1];
		p1_w1_down=new BufferedImage[1];
		p1_w1_left=new BufferedImage[1];
		p1_w1_right=new BufferedImage[1];
		p1_w2_up=new BufferedImage[1];
		p1_w2_down=new BufferedImage[1];
		p1_w2_left=new BufferedImage[1];
		p1_w2_right=new BufferedImage[1];
		
		//player2
		p2_up=new BufferedImage[1];
		p2_down=new BufferedImage[1];
		p2_left=new BufferedImage[1];
		p2_right=new BufferedImage[1];
		p2_w1_up=new BufferedImage[1];
		p2_w1_down=new BufferedImage[1];
		p2_w1_left=new BufferedImage[1];
		p2_w1_right=new BufferedImage[1];
		p2_w2_up=new BufferedImage[1];
		p2_w2_down=new BufferedImage[1];
		p2_w2_left=new BufferedImage[1];
		p2_w2_right=new BufferedImage[1];
		
		//player3
		p3_up=new BufferedImage[1];
		p3_down=new BufferedImage[1];
		p3_left=new BufferedImage[1];
		p3_right=new BufferedImage[1];
		p3_w1_up=new BufferedImage[1];
		p3_w1_down=new BufferedImage[1];
		p3_w1_left=new BufferedImage[1];
		p3_w1_right=new BufferedImage[1];
		p3_w2_up=new BufferedImage[1];
		p3_w2_down=new BufferedImage[1];
		p3_w2_left=new BufferedImage[1];
		p3_w2_right=new BufferedImage[1];
		
		
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("spritesheet_characters.png"));
		//load player1
		p1_up[0]= sheet.crop(width * 4, 0, width, height);
		p1_down[0]=sheet.crop(width * 5, 0, width, height);
		p1_left[0]=sheet.crop(width * 6, 0, width, height);
		p1_right[0]=sheet.crop(width * 7, 0, width, height);
		p1_w1_up[0]= sheet.crop(width * 8, 0, width, height);
		p1_w1_down[0]=sheet.crop(width * 4, 0, width, height);
		p1_w1_left[0]= sheet.crop(width * 4, 0, width, height);
		p1_w1_right[0]= sheet.crop(width * 4, 0, width, height);
		p1_w2_up[0]= sheet.crop(width * 4, 0, width, height);
		p1_w2_down[0]= sheet.crop(width * 4, 0, width, height);
		p1_w2_left[0]= sheet.crop(width * 4, 0, width, height);
		p1_w2_right[0]=sheet.crop(width * 4, 0, width, height);
		
		/*
		//or can get from png
	    String[] player1 = new String[13];
        String path1 = ("assets/tile/topdown-shooter/PNG/");
		player1[0]= path1 +"Hitman 1/hitman1_gun.png";
		player1[1]= path1 +"Hitman 1/hitman1_hold.png";
		player1[2]= path1 +"Hitman 1/hitman1_machine.png";
		player1[3]= path1 +"Hitman 1/hitman1_reload.png";
		player1[4]= path1 +"Hitman 1/hitman1_stand.png";
		player1[5]= path1 +"Hitman 1/hitman1_stand.png";
		player1[6]= path1 +"Hitman 1/hitman1_stand.png";
		player1[7]= path1 +"Hitman 1/hitman1_stand.png";
		player1[8]= path1 +"Hitman 1/hitman1_stand.png";
		player1[9]= path1 +"Hitman 1/hitman1_stand.png";
		player1[10]= path1 +"Hitman 1/hitman1_stand.png";
		player1[11]= path1 +"Hitman 1/hitman1_stand.png";
		
		
		String[] player2 = new String[13];
        //String path1 = ("assets/tile/topdown-shooter/PNG/");
		player2[0]= path1 +"Man Blue/manBlue_gun.png";
		player2[1]= path1 +"Man Blue/manBlue_hold.png";
		player2[2]= path1 +"Man Blue/manBlue_machine.png";
		player2[3]= path1 +"Man Blue/manBlue_reload.png";
		player2[4]= path1 +"Man Blue/manBlue_stand.png";
		player2[5]= path1 +"Man Blue/manBlue_stand.png";
		player2[6]= path1 +"Man Blue/manBlue_stand.png";
		player2[7]= path1 +"Man Blue/manBlue_stand.png";
		player2[8]= path1 +"Man Blue/manBlue_stand.png";
		player2[9]= path1 +"Man Blue/manBlue_stand.png";
		player2[10]= path1 +"Man Blue/manBlue_stand.png";
		player2[11]= path1 +"Man Blue/manBlue_stand.png";
		
		String[] player3 = new String[13];
        //String path1 = ("assets/tile/topdown-shooter/PNG/");
		player3[0]= path1 +"Man Brown/manBrown_gun.png";
		player3[1]= path1 +"Man Brown/manBrown_hold.png";
		player3[2]= path1 +"Man Brown/manBrown_machine.png";
		player3[3]= path1 +"Man Brown/manBrown_reload.png";
		player3[4]= path1 +"Man Brown/manBrown_stand.png";
		player3[5]= path1 +"Man Brown/manBrown_stand.png";
		player3[6]= path1 +"Man Brown/manBrown_stand.png";
		player3[7]= path1 +"Man Brown/manBrown_stand.png";
		player3[8]= path1 +"Man Brown/manBrown_stand.png";
		player3[9]= path1 +"Man Brown/manBrown_stand.png";
		player3[10]= path1 +"Man Brown/manBrown_stand.png";
		player3[11]= path1 +"Man Brown/manBrown_stand.png";
*/
}
}


//private static final int width =32,height=32;	
/*
//for player1
public static BufferedImage[]p1_down,p1_up,p1_left,p1_right,
p1_w1_down,p1_w1_up,p1_w1_right,p1_w1_left,
p1_w2_down,p1_w2_up,p1_w2_right,p1_w2_left;

//for player2
public static BufferedImage[]p2_down,p2_up,p2_left,p2_right,
p2_w1_down,p2_w1_up,p2_w1_right,p2_w1_left,
p2_w2_down,p2_w2_up,p2_w2_right,p2_w2_left;

//for player3
public static BufferedImage[]p3_down,p3_up,p3_left,p3_right,
p3_w1_down,p3_w1_up,p3_w1_right,p3_w1_left,
p3_w2_down,p3_w2_up,p3_w2_right,p3_w2_left;
*/

//public static void init() {
	/*
	//player1
	p1_up=new BufferedImage[1];
	p1_down=new BufferedImage[1];
	p1_left=new BufferedImage[1];
	p1_right=new BufferedImage[1];
	p1_w1_up=new BufferedImage[1];
	p1_w1_down=new BufferedImage[1];
	p1_w1_left=new BufferedImage[1];
	p1_w1_right=new BufferedImage[1];
	p1_w2_up=new BufferedImage[1];
	p1_w2_down=new BufferedImage[1];
	p1_w2_left=new BufferedImage[1];
	p1_w2_right=new BufferedImage[1];
	
	//player2
	p2_up=new BufferedImage[1];
	p2_down=new BufferedImage[1];
	p2_left=new BufferedImage[1];
	p2_right=new BufferedImage[1];
	p2_w1_up=new BufferedImage[1];
	p2_w1_down=new BufferedImage[1];
	p2_w1_left=new BufferedImage[1];
	p2_w1_right=new BufferedImage[1];
	p2_w2_up=new BufferedImage[1];
	p2_w2_down=new BufferedImage[1];
	p2_w2_left=new BufferedImage[1];
	p2_w2_right=new BufferedImage[1];
	
	//player3
	p3_up=new BufferedImage[1];
	p3_down=new BufferedImage[1];
	p3_left=new BufferedImage[1];
	p3_right=new BufferedImage[1];
	p3_w1_up=new BufferedImage[1];
	p3_w1_down=new BufferedImage[1];
	p3_w1_left=new BufferedImage[1];
	p3_w1_right=new BufferedImage[1];
	p3_w2_up=new BufferedImage[1];
	p3_w2_down=new BufferedImage[1];
	p3_w2_left=new BufferedImage[1];
	p3_w2_right=new BufferedImage[1];
	*/
