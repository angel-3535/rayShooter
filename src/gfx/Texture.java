package gfx;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Texture {
    public Color[][] texColorArray;
    public ImageIcon img;
    public int w;
    public int h;
    public static Texture t_bricks;
    public static Texture t_missing;

    static {
        try {
            t_missing = new Texture("src/assets/textures/0missing.png");
        } catch (IOException e) {
            System.out.println("Missing textures missing......funny");
            e.printStackTrace();
        }
    }

    public static Texture t_bricks_R;
    public static Texture t_bricks_B;
    public static Texture t_bricks_G;
    public static Texture t_crate_2c ;
    public static Texture t_door_1c ;
    public static Texture t_exit ;
    public static Texture t_floor_1a ;
    public static Texture t_grass_1a ;
    public static Texture t_dirt_1a ;
    public static Texture t_pipes_1a ;
    public static Texture t_tech_1c ;
    public static Texture t_sky ;

    public static Texture[] textures = new Texture[1000] ;


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
        this.img = new ImageIcon(img);
        System.out.println("testing: " + path);
        Color textureColor = texColorArray[0][0];
        System.out.println("test pass");

    }

    public Texture(File file) throws IOException {
        System.out.println("Loading..." + file);
        BufferedImage img =  ImageIO.read(file);
        w = img.getWidth();
        h = img.getHeight();
        texColorArray = new Color[h][w];

        for (int y = 0; y < h; y++){
            for (int x = 0; x < w; x++){
                texColorArray[y][x] = new Color(img.getRGB(x,y));
            }
        }
        this.img = new ImageIcon(img);
        System.out.println("testing: " + file);
        Color textureColor = texColorArray[0][0];
        System.out.println("test pass");

    }

    //LOAD ALL TEXTURE FILES
    public static void loadAllTextures(){
//        try {
//            textures[1] = new Texture("src/assets/textures/2DOOR_1C.PNG");
//
//            textures[2] = new Texture("src/assets/textures/1EXIT.PNG");
//        }catch (Exception e){
//
//        }

        int current = 1;
        File folder = new File("src/assets/textures");
        File[] listOfFiles = folder.listFiles()  ;

        for (File file : listOfFiles){
            try {
                textures[current] = new Texture(file);
                current++;
            }catch (Exception e){
                textures[current] = t_missing;
                current++;
            }
        }

        try{

            t_sky = new Texture("src/assets/sky/skyBox2.png");
        }catch (Exception e){

        }

    }
    public static void loadTextures() throws IOException {

        t_bricks = new Texture("src/assets/textures/bricks.png");
        textures[1] = t_bricks;

        t_bricks_R = new Texture("src/assets/textures/bricks_R.png");
        textures[2] = t_bricks_R;

        t_bricks_B = new Texture("src/assets/textures/bricks_B.png");
        textures[3] = t_bricks_B;

        t_bricks_G = new Texture("src/assets/textures/bricks_G.png");
        textures[4] = t_bricks_G;

        t_crate_2c = new Texture("src/assets/textures/CRATE_2C.png");
        textures[5] = t_crate_2c;

        t_floor_1a = new Texture("src/assets/textures/FLOOR_1A.png");
        textures[6] = t_floor_1a;

        t_grass_1a = new Texture("src/assets/textures/GRASS_1A.PNG");
        textures[7] = t_grass_1a;

        t_dirt_1a = new Texture("src/assets/textures/DIRT_1A.PNG");
        textures[8] = t_dirt_1a;

        t_pipes_1a = new Texture("src/assets/textures/PIPES_1A.png");
        textures[9] = t_pipes_1a;

        t_tech_1c = new Texture("src/assets/textures/TECH_1C.PNG");
        textures[10] = t_tech_1c;

        t_door_1c = new Texture("src/assets/textures/2DOOR_1C.PNG");
        textures[11] = t_door_1c;

        t_exit = new Texture("src/assets/textures/1EXIT.PNG");
        textures[12] = t_exit;

        textures[99] = t_missing;


        t_sky = new Texture("src/assets/sky/skyBox2.png");

    }
}
