import util.Time;
import util.io.KL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.VolatileImage;

public class Game implements Runnable{

    Window window;
    int frameRate = 0;
    String displayInfo = "";
    KL kl = KL.getKeyListener();
    private final float moveSpeed = 200f;
    float px, py;
    int mapX = 8, mapY = 8, mapS = 64;
    int[] map = {
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 1, 1,
            1, 0, 0, 0, 0, 0, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1,

    };



    public Game(){
        window = new Window();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1024,512);
        window.setVisible(true);
        window.setTitle("rayShooter");



        px = 300;
        py = 300;
    }

    private void nonVolatileImageRender() {
        Image Img = window.createImage(window.getWidth(),window.getHeight());
        Graphics g = Img.getGraphics();

        this.draw(g);

        window.getGraphics().drawImage(Img, 0, 0, null);
    }

    private void volatileImageRender() {
        VolatileImage vImg =  window.gc.createCompatibleVolatileImage(window.getWidth(),window.getHeight());

        do {
            if (vImg.validate(window.gc) ==
                    VolatileImage.IMAGE_INCOMPATIBLE)
            {
                // old vImg doesn't work with new GraphicsConfig; re-create it
                vImg = window.gc.createCompatibleVolatileImage(window.getWidth(),window.getHeight());
            }
            Graphics2D g = vImg.createGraphics();


            draw(g);

            g.dispose();
        } while (vImg.contentsLost());

        window.getGraphics().drawImage(vImg, 0, 0, null);

    }

    public void drawMap2D(Graphics g){
        int x,y,xo,yo;
        for (y = 0; y < mapY; y++){
            for (x = 0; x < mapX; x++){

                if (map[y*mapX+x]==1){
                    g.setColor(Color.white);
                }else{
                    g.setColor(Color.black);
                }

                xo = x*mapS; yo = y*mapS;

                g.fillRect(xo,yo,mapS,mapS);

                g.setColor(Color.GRAY);
                g.drawRect(xo,yo,mapS,mapS);


            }
        }
    }
    public void drawPlayer(Graphics g){
        g.setColor(Color.yellow);
        g.fillRect((int) px, (int) py, (int) 8, (int)  8);
    }



    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,window.getWidth(),window.getHeight());
        drawMap2D(g);
        drawPlayer(g);

        g.setColor(Color.green);
        g.drawString(displayInfo,30,90);
    }

    public void inputs(double dt){
        if (kl.isKeyDown(KeyEvent.VK_W)){
            py -= moveSpeed * dt;
        }
        if (kl.isKeyDown(KeyEvent.VK_S)){
            py += moveSpeed * dt;
        }
        if (kl.isKeyDown(KeyEvent.VK_A)){
            px -= moveSpeed * dt;
        }
        if (kl.isKeyDown(KeyEvent.VK_D)){
            px += moveSpeed * dt;
        }
    }

    public void update(double dt){

        inputs(dt);


        frameRate = (int) (1/dt);
        displayInfo = String.format("%d FPS (%.3f)", frameRate,dt);

        volatileImageRender();

    }



    @Override
    public void run() {
        window.requestFocus();
        double lastFrameTime = 0.0;
        try{
            while(true){
                double time =  Time.getTime();
                double deltaTime = time - lastFrameTime;
                lastFrameTime = time;

                update(deltaTime);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        window.dispose();
    }
}
