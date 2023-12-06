package gfx;

import javax.imageio.ImageIO;
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

    }
}
