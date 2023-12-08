import gfx.Renderable;
import gfx.Texture;
import gfx.Window;
import util.*;
import util.io.KL;
import util.io.ML;

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
    private float rotationSpeed = 270f;
    private ImageIcon gunSprite;
    private boolean moving = false;
    float weaponX = 0 ,weaponY =  0;

    Player player = new Player();
    final KL kl = KL.getKeyListener();
    final ML ml = ML.getMouseListener();
    float moveSpeed;
    float reach;
    int maxRenderLineHeight ;
    int totalRays = 120;
    final float FOV = (float) Math.toRadians(60);
    final float rayStep = FOV/totalRays;
    private int newWallVal = 1;
    private Color f_Color = new Color(40, 23, 23,255);
    private Color c_Color = new Color(30, 55, 58,255);

    final float mapScale = 1f/10f;


    Map map = new Map();


    public Game() throws IOException {
        window = Window.getWindow();
        maxRenderLineHeight = window.getHeight();


        player.transform.setPosition(300f,300f);
        player.transform.rotateAngleRadiansBy(10* RAD);

        moveSpeed = map.getTileSize() * 6 ;
        reach = (float) (map.getTileSize() * 1.5);

        gunSprite = new ImageIcon("src/assets/handgun.png");
        weaponY =  window.getHeight() - gunSprite.getIconHeight();
        weaponX = window.getWidth() - gunSprite.getIconWidth();


        Texture.loadTextures();
    }


    public void inputs(double dt){
        if (kl.isKeyDown(KeyEvent.VK_W)){
            Vector2D v = new Vector2D(player.transform.getFoward());
            v.multiply((float) (moveSpeed * dt));

            Vector2D newPos = new Vector2D(player.transform.getX() + v.getX(), player.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(player.transform.getY())) == 0){
                player.transform.moveXBy(v.getX());
            }
            if (map.getTileContent(map.getMapX(player.transform.getX()), map.getMapY(newPos.getY())) == 0){
                player.transform.moveYBy(v.getY());
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_S)){
            Vector2D v = new Vector2D(player.transform.getFoward());
            v.multiply((float) - (moveSpeed * dt));

            Vector2D newPos = new Vector2D(player.transform.getX() + v.getX(), player.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(player.transform.getY())) == 0){
                player.transform.moveXBy(v.getX());
            }
            if (map.getTileContent(map.getMapX(player.transform.getX()), map.getMapY(newPos.getY())) == 0){
                player.transform.moveYBy(v.getY());
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_A)){
            Vector2D v = new Vector2D(player.transform.getFowardNormal());
            v.multiply((float) (-moveSpeed * dt));

            Vector2D newPos = new Vector2D(player.transform.getX() + v.getX(), player.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(player.transform.getY())) == 0){
                player.transform.moveXBy(v.getX());
            }
            if (map.getTileContent(map.getMapX(player.transform.getX()), map.getMapY(newPos.getY())) == 0){
                player.transform.moveYBy(v.getY());
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_LEFT)){
            player.transform.rotateAngleDegreesBy((float) -(rotationSpeed * dt));
        }
        if (kl.isKeyDown(KeyEvent.VK_D)){
            Vector2D v = new Vector2D(player.transform.getFowardNormal());
            v.multiply((float) (moveSpeed * dt));

            Vector2D newPos = new Vector2D(player.transform.getX() + v.getX(), player.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(player.transform.getY())) == 0){
                player.transform.moveXBy(v.getX());
            }
            if (map.getTileContent(map.getMapX(player.transform.getX()), map.getMapY(newPos.getY())) == 0){
                player.transform.moveYBy(v.getY());
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_RIGHT)){
            player.transform.rotateAngleDegreesBy((float) (rotationSpeed * dt));
        }
        if (kl.isKeyDown(KeyEvent.VK_E)){
            Vector2D v = new Vector2D(player.transform.getFoward());
            v.multiply(reach);

            Vector2D newPos = new Vector2D(player.transform.getX() + v.getX(), player.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(newPos.getY())) == 22){
                map.setTileContent(map.getMapX(newPos.getX()), map.getMapY(newPos.getY()), 0);
            }
        }


        if (kl.isKeyDown(KeyEvent.VK_W) || kl.isKeyDown(KeyEvent.VK_S)){
            moving = true;
        }
        else{
            moving = false;
        }

//        mouseInputs(dt);
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
//            r.draw(g);
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

                float mn = (float) ((window.getWidth()/2f)/tan(FOV/2));



                textureX = (float) (player.transform.getX()/2f + cos(rayAngle) * 158* 64/dy/raFix);
                textureY = (float) (player.transform.getY()/2f + sin(rayAngle) * 158* 64/dy/raFix);
                try {

                    Texture t_floor = map.getFloorTexture(map.floor[(int) (textureY/64)][(int) (textureX/64f)]);


                    //INSANE BIT FUCKERY (USE BITWISE AND TO ONLY GET THE VALUE OF THE FIRST 32 NUMBERS)
                    Color textureColor = t_floor.texColorArray[(int) (textureY)&31 ][(int) (textureX )&31];

                    g.setColor(textureColor);
                    g.fillRect((int) (lineWidth * rayNumber ), y, (int) lineWidth,1);
                }catch (Exception e){
//                    System.out.println("error: " +textureX +", " + textureY);
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

        inputs(dt);

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
