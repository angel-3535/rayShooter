import util.io.KL;
import util.io.ML;

import javax.swing.*;
import java.awt.*;
import java.awt.image.VolatileImage;

public class Window extends JFrame {

    GraphicsEnvironment ge;
    GraphicsConfiguration gc;
    private boolean volatileR;

    public Window(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1024,512);
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

    public void render(Game game){
        if (volatileR){
            volatileImageRender(game);
        }else{
            nonVolatileImageRender(game);
        }
    }

    private void volatileImageRender(Game game) {
        VolatileImage vImg =  this.gc.createCompatibleVolatileImage(this.getWidth(),this.getHeight());

        do {
            if (vImg.validate(this.gc) ==
                    VolatileImage.IMAGE_INCOMPATIBLE)
            {
                // old vImg doesn't work with new GraphicsConfig; re-create it
                vImg = this.gc.createCompatibleVolatileImage(this.getWidth(),this.getHeight());
            }
            Graphics2D g = vImg.createGraphics();


            game.draw(g);

            g.dispose();
        } while (vImg.contentsLost());

        this.getGraphics().drawImage(vImg, 0, 0, null);

    }

    private void nonVolatileImageRender(Game game) {
        Image Img = this.createImage(this.getWidth(),this.getHeight());
        Graphics g = Img.getGraphics();

        game.draw(g);

        this.getGraphics().drawImage(Img, 0, 0, null);
    }




}
