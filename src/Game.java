import util.Time;
import util.io.KL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.VolatileImage;

import static java.lang.Math.cos;

public class Game implements Runnable{

    Window window;
    int frameRate = 0;
    String displayInfo = "";
    KL kl = KL.getKeyListener();
    private final float moveSpeed = 50f;
    float playerX, playerY, playerDX, playerDY, playerAngle;
    int mapXSize = 16, mapYSize = 16, mapTileSize = 32, mapArraySize = 16;

//    int[][] map = {
//            {1, 1, 1, 1, 1, 1, 1, 1},
//            {1, 0, 1, 0, 0, 0, 0, 1},
//            {1, 0, 1, 0, 0, 0, 0, 1},
//            {1, 0, 0, 0, 0, 0, 0, 1},
//            {1, 0, 0, 0, 0, 0, 0, 1},
//            {1, 0, 0, 0, 0, 0, 0, 1},
//            {1, 0, 1, 0, 0, 0, 0, 1},
//            {1, 1, 1, 1, 1, 1, 1, 1},
//
//
//    };
int[][] map = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0, 0, 0, 1},
            {1, 0, 3, 3, 3, 3, 3, 3, 0, 3, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1},
            {1, 0, 2, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}

    };

    private final float PI = (float) Math.PI, RAD = 0.0174533F;


    public Game(){
        window = new Window();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1024,512);
        window.setVisible(true);
        window.setTitle("rayShooter");



        playerX = 300;
        playerY = 300;
        playerDX = (float) cos(playerAngle) * 5;
        playerDY = (float) Math.sin(playerAngle) * 5;

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
        for (y = 0; y < mapYSize; y++){
            for (x = 0; x < mapXSize; x++){

                if (map[y][x]!=0){
                    g.setColor(Color.white);
                }else{
                    g.setColor(Color.black);
                }

                xo = x* mapTileSize; yo = y* mapTileSize;

                g.fillRect(xo,yo, mapTileSize, mapTileSize);

                g.setColor(Color.GRAY);
                g.drawRect(xo,yo, mapTileSize, mapTileSize);


            }
        }
    }

    public float getRayLength(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }



    public void drawRays2D(Graphics g){

        int rayNumber=0, totalRays = 120, mapX=0,mapY=0,depth=0,maxDepth = mapArraySize;
        float rayX = playerX, rayY = playerY, rayAngle = 0, xOffset = 0, yOffset = 0, disT;
        rayAngle = playerAngle - (RAD * totalRays/4);
        Color wallColor = Color.green;


        if (rayAngle <    0){ rayAngle+=2*PI; }
        if (rayAngle > 2*PI){ rayAngle-=2*PI; }




        for (rayNumber = 0; rayNumber < totalRays; rayNumber++){
            //Horizontal line check;
            depth = 0;
            float distanceH= 100000,horX = 0, horY = 0;
            float angleTangent = (float) (-1/Math.tan(rayAngle));


            //if ray is angle down
            if (rayAngle > PI){
                rayY = (float) (((int) playerY/ mapTileSize) * (mapTileSize) -0.0001) ;  rayX = ((playerY - rayY) * angleTangent) + playerX;
                yOffset =-mapTileSize; xOffset = -yOffset * angleTangent;
            }
            //if ray is angle up
            if (rayAngle < PI){
                //rounding the float to an aprox (dividing by 64 and then multiplying by 64)
                rayY = (float) (((int) playerY/ mapTileSize) * (mapTileSize) + mapTileSize); rayX = ((playerY - rayY) * angleTangent) + playerX;
                yOffset = mapTileSize;  xOffset = -yOffset * angleTangent;
            }

            if (rayAngle == 0 || rayAngle == PI){
                rayX = playerX; rayY = playerY; depth = maxDepth;
            }

            while (depth < maxDepth){
                mapX = (int) (rayX) / mapTileSize; mapY = (int) (rayY) / mapTileSize;
                mapX = clamp(mapX, 0, mapXSize-1);  mapY = clamp(mapY, 0, mapYSize-1);
                if (map[mapY][mapX] != 0){

                    horX = rayX;horY = rayY;
                    distanceH = getRayLength(playerX,playerY,horX,horY);
                    depth = maxDepth;
                }else {
                    rayX += xOffset; rayY += yOffset;
                    depth += 1;
                }
            }

            //-------------------Vertical RAYCAST-------------------------


            depth = 0;
            float distanceV= Float.MAX_VALUE,verX = 0, verY = 0;
            float negAngleTangent = (float) (-Math.tan(rayAngle));

            //if ray is angle left
            if (rayAngle > PI/2 && rayAngle < 3*PI/2){
                rayX = (float)(((int)playerX/mapTileSize) * (mapTileSize) -0.0001) ;  rayY = ((playerX - rayX) * negAngleTangent) + playerY;
                xOffset =-mapTileSize; yOffset =-xOffset * negAngleTangent;
            }
            //if ray is angle right
            if (rayAngle < PI/2 || rayAngle > 3*PI/2){
                rayX = (float)(((int)playerX/mapTileSize) * (mapTileSize) +mapTileSize) ; rayY = ((playerX - rayX) * negAngleTangent) + playerY;
                xOffset = mapTileSize;  yOffset = -xOffset * negAngleTangent;
            }
            //looking straight up or down
            if (rayAngle == PI/2 || rayAngle == 3 * PI/2){

                rayX = playerX; rayY = playerY; depth = maxDepth;
            }

            while (depth < maxDepth){
                mapX = (int) (rayX)/mapTileSize; mapY = (int) (rayY)/mapTileSize;
                mapX = clamp(mapX, 0, mapXSize-1);  mapY = clamp(mapY, 0, mapYSize-1);
                if (map[mapY][mapX] != 0){
                    verX = rayX;
                    verY = rayY;
                    distanceV = getRayLength(playerX,playerY,verX,verY);
                    depth = maxDepth;


                }else {
                    rayX += xOffset; rayY += yOffset;
                    depth += 1;
                }
            }
            boolean darker = false;
            if ( distanceH>distanceV){rayX = verX; rayY = verY; disT=distanceV; g.setColor(Color.GREEN);}
            else                     {rayX = horX; rayY = horY; disT=distanceH; darker = true;}

            mapX = (int) (rayX)/mapTileSize; mapY = (int) (rayY)/mapTileSize;
            mapX = clamp(mapX, 0, mapXSize-1);  mapY = clamp(mapY, 0, mapYSize-1);

            if (map[mapY][mapX] == 1) {wallColor = Color.green;}
            if (map[mapY][mapX] == 2) {wallColor = Color.red;}
            if (map[mapY][mapX] == 3) {wallColor = Color.blue;}
            if (darker) {wallColor = wallColor.darker();}


            g.setColor(wallColor);

            g.drawLine((int) playerX, (int)playerY, (int)rayX, (int)rayY);

//            ----DRAW 3D WALLS----

//            Fixes fish eye
            float ca = playerAngle - rayAngle;
            if (ca <    0){ ca+=2*PI; }
            if (ca > 2*PI){ ca-=2*PI; }
            disT = (float)(disT * cos(ca));
//            ends fix for fish eye
            float lineH = (mapTileSize * 2 * 320) / disT;
            if (lineH>320){
                lineH = 320;
            }
            float lineOffset = 160-lineH/2;
            int lines= (int) (512.0/totalRays);

            g.fillRect(rayNumber*lines + 530,(int)lineOffset + 100,lines,(int)lineH);

            rayAngle += RAD/2;
            if (rayAngle <    0){ rayAngle+=2*PI; }
            if (rayAngle > 2*PI){ rayAngle-=2*PI; }

        }


    }

    public int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public void drawPlayer(Graphics g){

        g.setColor(Color.yellow);
        g.fillRect((int) playerX, (int) playerY, (int) 8, (int)  8);
        g.setColor(Color.red);
        g.drawLine((int) playerX,(int)playerY,(int) (playerX + playerDX * 5  ) ,(int) (playerY + playerDY * 5));

    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,window.getWidth(),window.getHeight());

        drawMap2D(g);
        drawRays2D(g);
        drawPlayer(g);

        g.setColor(Color.green);
        g.drawString(displayInfo,30,90);
    }

    public void inputs(double dt){
        if (kl.isKeyDown(KeyEvent.VK_W)){
            playerX += (float) (moveSpeed * playerDX * dt);
            playerY += (float) (moveSpeed * playerDY * dt);
        }
        if (kl.isKeyDown(KeyEvent.VK_S)){
            playerX -= (float) (moveSpeed * playerDX * dt);
            playerY -= (float) (moveSpeed * playerDY * dt);
        }
        if (kl.isKeyDown(KeyEvent.VK_A)){
            playerAngle -= (float) (6 * dt);
            if (playerAngle <0){
                playerAngle +=2*PI;
            }
            playerDX = (float) cos(playerAngle) * 5;
            playerDY = (float) Math.sin(playerAngle) * 5;

        }
        if (kl.isKeyDown(KeyEvent.VK_D)){
            playerAngle += (float) (6 * dt);
            if (playerAngle >2*PI){
                playerAngle -=2*PI;
            }
            playerDX = (float) cos(playerAngle) * 5;
            playerDY = (float) Math.sin(playerAngle) * 5;
        }
    }

    public void update(double dt){

        inputs(dt);

        frameRate = (int) (1/dt);
        displayInfo = String.format("%d FPS (%.3f)", frameRate,dt);

        volatileImageRender();
//        nonVolatileImageRender();
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
