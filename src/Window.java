import util.io.KL;
import util.io.ML;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    GraphicsEnvironment ge;
    GraphicsConfiguration gc;

    public Window(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1024,512);
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("rayshooter");
        addKeyListener(KL.getKeyListener());
        addMouseListener(ML.getMouseListener());
        System.setProperty("sun.java2d.opengl", "true");

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
    }




}
