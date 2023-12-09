import gfx.Renderable;
import gfx.Texture;
import gfx.Window;
import util.*;
import util.io.KL;
import util.io.ML;
import util.io.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static java.lang.Math.*;

public class Game implements Runnable, Renderable {

    gfx.Window window;
    int frameRate = 0;
    String displayInfo = "";
    private final float PI = (float) Math.PI, RAD = 0.0174533F;

    Player player;
    final KL kl = KL.getKeyListener();
    final ML ml = ML.getMouseListener();
    int maxRenderLineHeight ;
    int totalRays = 150;
    final float FOV = (float) Math.toRadians(75);
    final float rayStep = FOV/totalRays;
    private Color f_Color = new Color(40, 23, 23,255);
    private Color c_Color = new Color(30, 55, 58,255);

    Map map = new Map();



    public Game() throws IOException {
        window = Window.getWindow();
        maxRenderLineHeight = window.getHeight();

        player = new Player(0, map);

        player.transform.setPosition(300f,300f);
        player.transform.rotateAngleRadiansBy(10* RAD);

        player.moveSpeed = map.getTileSize() * 6 ;

        Texture.loadTextures();

        Sound.playMusic(Sound.M_HELL_ON_EARTH.getClip());

    }



    public void castRays(Graphics g){

        int rayNumber=0;

        float rayAngle = player.transform.getAngleRadians() - FOV/2;

        Ray r;

        if (rayAngle <    0){ rayAngle+=2*PI; }
        if (rayAngle > 2*PI){ rayAngle-=2*PI; }

        for (rayNumber = 0; rayNumber < totalRays; rayNumber++) {

            r = new Ray(
                    player.transform.getX(),
                    player.transform.getY(),
                    rayAngle
            );
            r.trace(map);
            g.setColor(r.wallColor);

//            ----DRAW 3D WALLS----
//            Fixes fish eye
            float ca = player.transform.getAngleRadians() - r.rayAngle;
            if (ca <    0){ ca+=2*PI; }
            if (ca > 2*PI){ ca-=2*PI; }
            r.disT = (float)(r.disT * cos(ca));
//            ends fix for fish eye
            float lineH = (map.getTileSize() * maxRenderLineHeight) / r.disT;


            float textureYStep = (63f/lineH);
            float textureYOffset = 0;

            if (lineH> maxRenderLineHeight){
                textureYOffset = (lineH - maxRenderLineHeight) / 2f;
                lineH = maxRenderLineHeight;
            }

            float lineOffset = window.getHeight()/2 - lineH/2;
            float lineWidth= (window.getWidth()/totalRays)+1;


            float textureY = textureYOffset * textureYStep;
            float textureX;

            if (r.darker){
                textureX = (int) (r.hit.getY()) % map.getTileSize();
            }else{
                textureX = (int) (r.hit.getX()) % map.getTileSize();
            }



            for(int wallPixel=0; wallPixel < lineH; wallPixel ++){
                Color textureColor = r.wallTexture.texColorArray[(int) textureY][(int) textureX];

                if (r.darker){
                    textureColor = textureColor.darker();
                }

                textureColor = new Color(textureColor.getRed(),textureColor.getGreen(),textureColor.getBlue());

                g.setColor(textureColor);

                g.fillRect((int) (lineWidth * rayNumber ),(int)lineOffset + wallPixel, (int) lineWidth,1);
                textureY += textureYStep;
            }

            //---Draw Floors---
            for(int y = (int) (lineOffset + lineH); y < window.getHeight(); y++){
                float dy = (float) y - (window.getHeight()/2f);

                float raFix = player.transform.getAngleRadians() - rayAngle;
                if (raFix <    0){ raFix+=2*PI; }
                if (raFix > 2*PI){ raFix-=2*PI; }

                raFix = (float) cos(raFix);

                textureX = (float) (player.transform.getX()/2f + cos(rayAngle) * 158* 64/ dy /raFix);
                textureY = (float) (player.transform.getY()/2f + sin(rayAngle) * 158* 64/ dy /raFix);
                try {

                    Texture texture = map.getFloorTexture((map.floor[(int) (textureY/64)][(int) (textureX/64f)]));


                    //INSANE BIT FUCKERY (USE BITWISE AND TO ONLY GET THE VALUE OF THE FIRST 32 NUMBERS)
                    Color t_pixelColor = texture.texColorArray[(int) (textureY)&31 ][(int) (textureX )&31];

                    g.setColor(t_pixelColor);
                    g.fillRect((int) (lineWidth * rayNumber ), y, (int) lineWidth,1);

                    //Draw Ceiling

                    texture = map.getCeilingTexture((map.ceiling[(int) (textureY/64)][(int) (textureX/64f)]));
                    t_pixelColor = texture.texColorArray[(int) (textureY)&31 ][(int) (textureX )&31];

                    g.setColor(t_pixelColor);

                    g.fillRect((int) (lineWidth * rayNumber ), 640 - y, (int) lineWidth,1);


                }catch (Exception e){
//
                }



            }

            rayAngle += rayStep;
            if (rayAngle <    0){ rayAngle+=2*PI; }
            if (rayAngle > 2*PI){ rayAngle-=2*PI; }

        }
    }

    public void draw(Graphics g){

        g.setColor(c_Color);
        g.fillRect(0,0,window.getWidth(),window.getHeight()/2);
        g.setColor(f_Color);
        g.fillRect(0,window.getHeight()/2,window.getWidth(),window.getHeight()/2);

        castRays(g);
//        drawMap2D(g);
//        drawPlayer2D(g);
//        drawWeapon(g);

        g.setColor(Color.green);
        g.drawString(displayInfo,30,90);
    }

    public void update(double dt){

        player.inputs(dt);

        frameRate = (int) (1/dt);
        displayInfo = String.format("%d FPS (%.4f)", frameRate,dt);
        window.render(this);
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
