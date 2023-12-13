package Entity;

import gfx.Hud;
import gfx.Texture;
import gfx.Window;
import util.*;
import util.io.KL;
import weapon.Weapon;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.WeakHashMap;

import static java.lang.Math.*;

public class Player extends Entity{

    KL kl = KL.getKeyListener();
    public float moveSpeed;
    public float rotationSpeed = 270f;
    float reach;

    int maxRenderLineHeight ;
    int totalRays = 260;
    final float FOV = (float) Math.toRadians(60);
    float rayStep = FOV/totalRays;

    public Map map;
    Hud hud = new Hud();

    Vector2D spritePos = new Vector2D(300,300);
    int spriteZ =  20;
    int spriteX =  300;
    int spriteY =  300;
    Weapon gun = new Weapon(this,40,0.3,1,Integer.MAX_VALUE,3.f);
    public boolean nextState = false;

    public Player(float movementSpeed, Map map){


        transform = new Transform(2*64,2*64, 0,0);
        this.moveSpeed = movementSpeed;
        this.map = map;
        reach = (float) (map.getTileSize() * 1.5);

        maxRenderLineHeight = Window.getWindow().getHeight();


    }

    public void drawSprite(Graphics g){

        float sx = spritePos.getX() - transform.getX();
        float sy = spritePos.getY() - transform.getY();
        float sz = spriteZ;

        float CS = (float) cos(transform.getAngleRadians()), SN = (float) sin(transform.getAngleRadians());

        float a = -sy*CS + sx * SN;
        float b = -sx*CS - sy * SN;

        sx = a;
        sy = b;

        sx = (float) ((sx * 108.0/sy) + 60);
        sy = (float) ((sz * 108.0/sy) + 40);

        g.setColor(Color.red);
        g.drawImage(
                Texture.t_crate_2c.img.getImage(),
                (int) sx*8,
                (int) sy * 8,
                null
        );


    }
    public void inputs(double dt){
        if (kl.isKeyDown(KeyEvent.VK_W)){
            Vector2D v = new Vector2D(this.transform.getFoward());
            v.multiply((float) (moveSpeed * dt));

            Vector2D newPos = new Vector2D(this.transform.getX() + v.getX(), this.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(this.transform.getY())) == 0){
                this.transform.moveXBy(v.getX());
            }
            if (map.getTileContent(map.getMapX(this.transform.getX()), map.getMapY(newPos.getY())) == 0){
                this.transform.moveYBy(v.getY());
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_S)){
            Vector2D v = new Vector2D(this.transform.getFoward());
            v.multiply((float) - (moveSpeed * dt));

            Vector2D newPos = new Vector2D(this.transform.getX() + v.getX(), this.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(this.transform.getY())) == 0){
                this.transform.moveXBy(v.getX());
            }
            if (map.getTileContent(map.getMapX(this.transform.getX()), map.getMapY(newPos.getY())) == 0){
                this.transform.moveYBy(v.getY());
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_A)){
            Vector2D v = new Vector2D(this.transform.getFowardNormal());
            v.multiply((float) (-moveSpeed * dt));

            Vector2D newPos = new Vector2D(this.transform.getX() + v.getX(), this.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(this.transform.getY())) == 0){
                this.transform.moveXBy(v.getX());
            }
            if (map.getTileContent(map.getMapX(this.transform.getX()), map.getMapY(newPos.getY())) == 0){
                this.transform.moveYBy(v.getY());
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_LEFT)){
            this.transform.rotateAngleDegreesBy((float) -(rotationSpeed * dt));
        }
        if (kl.isKeyDown(KeyEvent.VK_D)){
            Vector2D v = new Vector2D(this.transform.getFowardNormal());
            v.multiply((float) (moveSpeed * dt));

            Vector2D newPos = new Vector2D(this.transform.getX() + v.getX(), this.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(this.transform.getY())) == 0){
                this.transform.moveXBy(v.getX());
            }
            if (map.getTileContent(map.getMapX(this.transform.getX()), map.getMapY(newPos.getY())) == 0){
                this.transform.moveYBy(v.getY());
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_RIGHT)){
            this.transform.rotateAngleDegreesBy((float) (rotationSpeed * dt));
        }
        if (kl.isKeyDown(KeyEvent.VK_E)){
            Vector2D v = new Vector2D(this.transform.getFoward());
            v.multiply(reach);

            Vector2D newPos = new Vector2D(this.transform.getX() + v.getX(), this.transform.getY() + v.getY());


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(newPos.getY())) == 4){
                map.setTileContent(map.getMapX(newPos.getX()), map.getMapY(newPos.getY()), 0);
            }
            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(newPos.getY())) == 3){
                nextState = true;
                transform.setPosition(new Vector2D(300,300));
            }
        }

        if (kl.isKeyDown(KeyEvent.VK_1)){
            totalRays = 60;
            rayStep = FOV/totalRays;
        }

        if (kl.isKeyDown(KeyEvent.VK_2)){
            totalRays = 120;
            rayStep = FOV/totalRays;

        }

        if (kl.isKeyDown(KeyEvent.VK_3)){
            totalRays = 240;
            rayStep = FOV/totalRays;

        }
        if (kl.isKeyDown(KeyEvent.VK_0)){
            totalRays = 30;
            rayStep = FOV/totalRays;

        }
        if (kl.isKeyDown(KeyEvent.VK_P)){
            totalRays = (int) (totalRays + 10 * dt)+2;
            totalRays = RMath.clamp(totalRays,10,Window.getWindow().getWidth());
            rayStep = FOV/totalRays;
        }
        if (kl.isKeyDown(KeyEvent.VK_O)){
            totalRays -= 10 * dt;
            totalRays = RMath.clamp(totalRays,10,960);
            rayStep = FOV/totalRays;

        }

    }
    public void setMap(Map map){
        this.map = map;
    }
    public void castRays(Graphics g){

        int rayNumber=0;

        float rayAngle = this.transform.getAngleRadians() - FOV/2;

        Ray r;

        if (rayAngle <    0){ rayAngle+=2*PI; }
        if (rayAngle > 2*PI){ rayAngle-=2*PI; }

        for (rayNumber = 0; rayNumber < totalRays; rayNumber++) {

            r = new Ray(
                    this.transform.getX(),
                    this.transform.getY(),
                    rayAngle
            );
            r.trace(map);
            g.setColor(r.wallColor);

//            ----DRAW 3D WALLS----
//            Fixes fish eye
            float ca = this.transform.getAngleRadians() - r.rayAngle;
            if (ca <    0){ ca+=2*PI; }
            if (ca > 2*PI){ ca-=2*PI; }
            r.disT = (float)(r.disT * cos(ca));
//            ends fix for fish eye
            float lineH = (float) ((map.getTileSize() * maxRenderLineHeight) / r.disT);


            float textureYStep = (63f/lineH);
            float textureYOffset = 0;

            if (lineH> maxRenderLineHeight){
                textureYOffset = (lineH - maxRenderLineHeight) / 2f;
                lineH = maxRenderLineHeight;
            }

            float lineOffset = Window.getWindow().getHeight()/2 - lineH/2;
            float lineWidth= (Window.getWindow().getWidth()/totalRays)+1;


            float textureY = textureYOffset * textureYStep;
            float textureX;

            if (r.darker){
                textureX = (int) (r.hit.getY()) % map.getTileSize();
            }else{
                textureX = (int) (r.hit.getX()) % map.getTileSize();
            }



            for(int wallPixel=0; wallPixel < lineH; wallPixel ++){
                int hitValue = r.hitValue;
                Color textureColor = map.getTexture(hitValue).texColorArray[(int) textureY][(int) textureX];

                if (r.darker){
                    textureColor = textureColor.darker();
                }

                textureColor = new Color(textureColor.getRed(),textureColor.getGreen(),textureColor.getBlue());

                g.setColor(textureColor);
                g.fillRect((int) (lineWidth * rayNumber ),(int)lineOffset + wallPixel, (int) lineWidth,1);
                textureY += textureYStep;
            }

            //---Draw Floors---
            for(int y = (int) (lineOffset + lineH); y < Window.getWindow().getHeight(); y++){
                float dy = (float) y - (Window.getWindow().getHeight()/2f);

                float raFix = this.transform.getAngleRadians() - rayAngle;
                if (raFix <    0){ raFix+=2*PI; }
                if (raFix > 2*PI){ raFix-=2*PI; }

                raFix = (float) cos(raFix);

                textureX = (float) (this.transform.getX() + cos(rayAngle) * 158 * 2* 64/ dy /raFix);
                textureY = (float) (this.transform.getY() + sin(rayAngle) * 158 * 2* 64/ dy /raFix);
                try {

                    Texture texture = map.getTexture((map.floor[(int) (textureY/64)][(int) (textureX/64f)]));


                    //INSANE BIT FUCKERY (USE BITWISE AND TO ONLY GET THE VALUE OF THE FIRST 32 NUMBERS)
                    Color t_pixelColor = texture.texColorArray[(int) (textureY)&63 ][(int) (textureX )&63];

                    g.setColor(t_pixelColor);
                    g.fillRect((int) (lineWidth * rayNumber ), y, (int) lineWidth,1);

                    //Draw Ceiling
                    if ((map.ceiling[(int) (textureY/64)][(int) (textureX/64f)] != 0)){
                        texture = map.getTexture((map.ceiling[(int) (textureY/64)][(int) (textureX/64f)]));
                        t_pixelColor = texture.texColorArray[(int) (textureY)&31 ][(int) (textureX )&31];

                        g.setColor(t_pixelColor);

                        g.fillRect((int) (lineWidth * rayNumber ), 640 - y, (int) lineWidth,1);
                    }

                }catch (Exception e){

                    try {

                        Texture texture = Texture.t_dirt_1a;


                        //INSANE BIT FUCKERY (USE BITWISE AND TO ONLY GET THE VALUE OF THE FIRST 32 NUMBERS)
                        Color t_pixelColor = texture.texColorArray[(int) (textureY)&63 ][(int) (textureX )&63];

                        g.setColor(t_pixelColor);
                        g.fillRect((int) (lineWidth * rayNumber ), y, (int) lineWidth,1);

                        //Draw Ceiling
                        if ((map.ceiling[(int) (textureY/64)][(int) (textureX/64f)] != 0)){
                            texture = map.getTexture((map.ceiling[(int) (textureY/64)][(int) (textureX/64f)]));
                            t_pixelColor = texture.texColorArray[(int) (textureY)&31 ][(int) (textureX )&31];

                            g.setColor(t_pixelColor);

                            g.fillRect((int) (lineWidth * rayNumber ), 640 - y, (int) lineWidth,1);
                        }

                    }catch (Exception ex){

                    }
//
                }



            }

            rayAngle += rayStep;
            if (rayAngle <    0){ rayAngle+=2*PI; }
            if (rayAngle > 2*PI){ rayAngle-=2*PI; }

        }
    }
    public void drawSky(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(0,0,Window.getWindow().getWidth(),Window.getWindow().getHeight());
        float ptw = transform.getAngleDegrees()!= 0? (transform.getAngleDegrees()/360f): 0;

        int xo =  (int) (ptw * Window.getWindow().getWidth()/2);
        g.drawImage(
                Texture.t_sky.img.getImage(),
                xo - Window.getWindow().getWidth(),
                0,
                Window.getWindow().getWidth(),
                Window.getWindow().getHeight()/2,
                null
        );
        g.drawImage(
                Texture.t_sky.img.getImage(),
                xo,
                0,
                Window.getWindow().getWidth(),
                Window.getWindow().getHeight()/2,
                null
        );

    }
    public void draw(Graphics g){

        drawSky(g);
        castRays(g);
//        drawSprite(g);
        gun.draw(g);

//        String str = String.format("Total rays: %d || Ray step: %.4f", totalRays,rayStep);
//        g.setColor(Color.BLACK);
//        Font myFont = new Font ("Courier New", 1, 17);
//        g.setFont(myFont);
//        g.drawString(str,Window.getWindow().getWidth()/2,Window.getWindow().getHeight()/2);
    }
    public void update(double dt){
        inputs(dt);
    }

}
