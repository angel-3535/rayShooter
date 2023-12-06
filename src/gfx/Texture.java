package gfx;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

public class Texture {
    public Color[][] texColorArray;
    public int w;
    public int h;
    public static Texture t_bricks;
    public static Texture t_missing;
    public static Texture t_bricks_R;
    public static Texture t_bricks_B;
    public static Texture t_bricks_G;
    public static Texture t_crate_2c ;
    public static Texture t_door_1c ;
    public static Texture t_floor_1a ;
    public static Texture t_pipes_1a ;
    public static Texture t_tech_1c ;


    public Texture(String path) throws IOException {
        System.out.println("Loading..." + path);
        BufferedImage img =  ImageIO.read(new File(path));
        w = img.getWidth();
        h = img.getHeight();
        texColorArray = new Color[h][w];

        for (int y = 0; y < h; y++){
            for (int x = 0; x < w; x++){
                texColorArray[y][x] = new Color(img.getRGB(x,y));
            }
        }
        System.out.println("testing: " + path);
        Color textureColor = texColorArray[0][0];
        System.out.println("test pass");

    }

    public static void loadTextures() throws IOException {

        t_bricks = new Texture("src/assets/bricks.png");
        t_bricks_R = new Texture("src/assets/bricks_R.png");
        t_bricks_B = new Texture("src/assets/bricks_B.png");
        t_bricks_G = new Texture("src/assets/bricks_G.png");
        t_missing = new Texture("src/assets/missing.png");
        t_crate_2c = new Texture("src/assets/CRATE_2C.png");
        t_floor_1a = new Texture("src/assets/FLOOR_1A.png");
        t_pipes_1a = new Texture("src/assets/PIPES_1A.png");
        t_tech_1c = new Texture("src/assets/TECH_1C.PNG");
        t_door_1c = new Texture("src/assets/DOOR_1C.PNG");

    }
}
