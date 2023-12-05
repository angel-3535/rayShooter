import gfx.Renderable;
import gfx.Window;
import util.*;
import util.io.KL;
import util.io.ML;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Game implements Runnable, Renderable {

    gfx.Window window;
    int frameRate = 0;
    String displayInfo = "";
    private final float PI = (float) Math.PI, RAD = 0.0174533F;
    private float rotationSpeed = 180f;
    private ImageIcon gunSprite;
    private boolean moving = false;
    float weaponX = 0 ,weaponY =  0;
    float lineYOffset = 0;

    Player player = new Player();
    final KL kl = KL.getKeyListener();
    final ML ml = ML.getMouseListener();
    final float moveSpeed = 120f;
    final int maxRenderLineHeight = 520;
    final int totalRays = 120;
    private int newWallVal = 1;
    private Color f_Color = new Color(40, 23, 23,255);
    private Color c_Color = new Color(30, 55, 58,255);

    Map map = new Map();


    public Game(){
        window = Window.getWindow();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1024,512);
        window.setVisible(true);
        window.setTitle("rayShooter");



        player.transform.setPosition(300f,300f);
        player.transform.rotateAngleRadiansBy(10* RAD);

        gunSprite = new ImageIcon("src/assets/handgun.png");

        weaponY =  512 - 256;
        weaponX = 1024 - 256;
    }

    public void drawMap2D(Graphics g){
        int x,y,xo,yo;
        for (y = 0; y < map.getMapSize(); y++){
            for (x = 0; x < map.getMapSize(); x++){

                if (map.getTileContent(x,y)!=0){
                    g.setColor(Color.white);
                }else{
                    g.setColor(Color.black);
                }

                xo = x* map.getTileSize(); yo = y* map.getTileSize();

                g.fillRect(xo,yo, map.getTileSize(), map.getTileSize());

                g.setColor(Color.GRAY);
                g.drawRect(xo,yo, map.getTileSize(), map.getTileSize());


            }
        }
    }

    public float getRayLength(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }


    public void castRays(Graphics g){

        int rayNumber=0;

        float rayAngle = player.transform.getAngleRadians() - (RAD * totalRays/4);

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
            r.draw(g);
            g.setColor(r.wallColor);


//            ----DRAW 3D WALLS----


//            Fixes fish eye
            float ca = player.transform.getAngleRadians() - r.rayAngle;
            if (ca <    0){ ca+=2*PI; }
            if (ca > 2*PI){ ca-=2*PI; }
            r.disT = (float)(r.disT * cos(ca));
//            ends fix for fish eye
            float lineH = (map.getTileSize() * 2 * maxRenderLineHeight) / r.disT;
            if (lineH> maxRenderLineHeight){
                lineH = maxRenderLineHeight;
            }
            float lineOffset = 160-lineH/2 + lineYOffset;
            int lines= (int) (512.0/totalRays) + 1;

            g.fillRect(rayNumber*lines + 512,(int)lineOffset + 100,lines,(int)lineH);

            rayAngle += RAD/2;
            if (rayAngle <    0){ rayAngle+=2*PI; }
            if (rayAngle > 2*PI){ rayAngle-=2*PI; }

        }


    }
    public void drawPlayer2D(Graphics g){

        g.setColor(Color.yellow);
        g.fillRect((int) player.transform.getX(), (int) player.transform.getY(), (int) 8, (int)  8);
        g.setColor(Color.red);
        g.drawLine(
                (int) player.transform.getX(),
                (int)player.transform.getY(),
                (int) (player.transform.getX() + player.transform.getFoward().getX() * 5  ) ,
                (int) (player.transform.getY() + player.transform.getFoward().getY() * 5  )
        );

    }
    public void draw(Graphics g){

        g.setColor(c_Color);
        g.fillRect(0,0,window.getWidth(),window.getHeight()/2);
        g.setColor(f_Color);
        g.fillRect(0,window.getHeight()/2,window.getWidth(),window.getHeight()/2);

        drawMap2D(g);
        castRays(g);
        drawPlayer2D(g);
        drawWeapon(g);

        g.setColor(Color.green);
        g.drawString(displayInfo,30,90);
    }
    public void drawWeapon(Graphics g){
        if (moving){
            weaponX = (float) (1024 - 256 - (cos(Time.getTime()) * 15));
            weaponY = (float) (512 -  256 - (sin(Time.getTime()) * 5));
        }
        g.drawImage(
                gunSprite.getImage(),
                (int) weaponX,
                (int) weaponY,
                256,
                256,
                null
        );

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
            player.transform.rotateAngleDegreesBy((float) -(rotationSpeed * dt));
        }
        if (kl.isKeyDown(KeyEvent.VK_LEFT)){
            player.transform.rotateAngleDegreesBy((float) -(rotationSpeed * dt));
        }
        if (kl.isKeyDown(KeyEvent.VK_D)){
            player.transform.rotateAngleDegreesBy((float) (rotationSpeed * dt));
        }
        if (kl.isKeyDown(KeyEvent.VK_RIGHT)){
            player.transform.rotateAngleDegreesBy((float) (rotationSpeed * dt));
        }
        if (kl.isKeyDown(KeyEvent.VK_1)){
            newWallVal = 1;
        }
        if (kl.isKeyDown(KeyEvent.VK_2)){
            newWallVal = 2;
        }
        if (kl.isKeyDown(KeyEvent.VK_3)){
            newWallVal = 3;
        }
        if (kl.isKeyDown(KeyEvent.VK_4)){
            newWallVal = 4;
        }
        if (kl.isKeyDown(KeyEvent.VK_5)){
            newWallVal = 5;
        }
        if (kl.isKeyDown(KeyEvent.VK_6)){
            newWallVal = 6;
        }
        if (kl.isKeyDown(KeyEvent.VK_7)){
            newWallVal = 7;
        }
        if (kl.isKeyDown(KeyEvent.VK_8)){
            newWallVal = 8;
        }

        if (kl.isKeyDown(KeyEvent.VK_W) || kl.isKeyDown(KeyEvent.VK_S)){
            moving = true;
        }
        else{
            moving = false;
        }

        mouseInputs(dt);
    }
    public void mouseInputs(double dt){
        if (ml.isPressed(MouseEvent.BUTTON1)){
            Vector2D v = map.getMapPos((float)ml.getX(),(float) ml.getY());

            System.out.println("X pos: " + ml.getX() + ", Y pos: " + ml.getY());

            if( v.getX() <  map.getMapSize() && v.getY() <  map.getMapSize()) {
                map.setTileContent((int)v.getX(),(int)v.getY(), newWallVal);
            }
        }

        if (ml.isPressed(MouseEvent.BUTTON3)){
            Vector2D v = map.getMapPos((float)ml.getX(),(float) ml.getY());

            System.out.println("X pos: " + ml.getX() + ", Y pos: " + ml.getY());

            if( v.getX() <  map.getMapSize() && v.getY() <  map.getMapSize()) {
                map.setTileContent((int)v.getX(),(int)v.getY(), 0);
            }
        }
    }

    public void update(double dt){

        inputs(dt);

        frameRate = (int) (1/dt);
        displayInfo = String.format("%d FPS (%.3f)", frameRate,dt);
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
