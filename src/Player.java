import gfx.Texture;
import gfx.Window;
import util.Map;
import util.Ray;
import util.Transform;
import util.Vector2D;
import util.io.KL;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.lang.Math.*;

public class Player {

    Transform transform;
    KL kl = KL.getKeyListener();
    public float moveSpeed;
    public float rotationSpeed = 270f;
    float reach;

    int maxRenderLineHeight ;
    int totalRays = 150;
    final float FOV = (float) Math.toRadians(65);
    final float rayStep = FOV/totalRays;

    public Map map;

    public Player(float movementSpeed, Map map){
        transform = new Transform(30,300,8,8);
        this.moveSpeed = movementSpeed;
        this.map = map;
        reach = (float) (map.getTileSize() * 1.5);

        maxRenderLineHeight = Window.getWindow().getHeight();

    }
    public void update(double dt){
        inputs(dt);
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


            if (map.getTileContent(map.getMapX(newPos.getX()), map.getMapY(newPos.getY())) == 22){
                map.setTileContent(map.getMapX(newPos.getX()), map.getMapY(newPos.getY()), 0);
            }
        }


        if (kl.isKeyDown(KeyEvent.VK_W) || kl.isKeyDown(KeyEvent.VK_S)){
//            moving = true;
        }
        else{
//            moving = false;
        }
    }

    public void setMap(Map map){
        this.map = map;
    }

    public void draw(Graphics g){
        castRays(g);
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
            float lineH = (map.getTileSize() * maxRenderLineHeight) / r.disT;


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

                    texture = map.getTexture((map.ceiling[(int) (textureY/64)][(int) (textureX/64f)]));
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
    
}
