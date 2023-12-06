package gfx;

import util.io.KL;
import util.io.ML;

import javax.swing.*;
import java.awt.*;
import java.awt.image.VolatileImage;

public class Window extends JFrame {

    GraphicsEnvironment ge;
    GraphicsConfiguration gc;
    private boolean volatileR;
    public static Window window = null;


    private Window(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1024,1024 * 3/4);
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("rayshooter");
        addKeyListener(KL.getKeyListener());
        addMouseListener(ML.getMouseListener());
        volatileR = true;
        System.setProperty("sun.java2d.opengl", "true");

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
    }

    public static Window getWindow(){
        if (Window.window == null){
            Window.window = new Window();
        }
        return window;
    }

    public static void render(Renderable r){
        if (window.volatileR){
            volatileImageRender(r);
        }else{
            nonVolatileImageRender(r);
        }
    }

    private static void volatileImageRender(Renderable r) {
        VolatileImage vImg =  window.gc.createCompatibleVolatileImage(window.getWidth(),window.getHeight());

        do {
            if (vImg.validate(window.gc) ==
                    VolatileImage.IMAGE_INCOMPATIBLE)
            {
                // old vImg doesn't work with new GraphicsConfig; re-create it
                vImg = window.gc.createCompatibleVolatileImage(window.getWidth(),window.getHeight());
            }
            Graphics2D g = vImg.createGraphics();


            r.draw(g);

            g.dispose();
        } while (vImg.contentsLost());

        window.getGraphics().drawImage(vImg, 0, 0, null);

    }

    private static void nonVolatileImageRender(Renderable r) {
        Image Img = window.createImage(window.getWidth(),window.getHeight());
        Graphics g = Img.getGraphics();

        r.draw(g);

        window.getGraphics().drawImage(Img, 0, 0, null);
    }


}
