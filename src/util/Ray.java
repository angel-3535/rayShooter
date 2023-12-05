package util;

import gfx.Renderable;

import java.awt.*;
import static java.lang.Math.PI;

public class Ray implements Renderable {
    public static final float RAD = 0.0174533F;
    public Vector2D origin = new Vector2D();
    public float rayAngle;
    public float hitX;
    public float hitY;
    public float rayX;
    public float rayY;
    public float disT = Float.MAX_VALUE;
    boolean darker = false;
    public Color wallColor = Color.green;
    public int mapX=0;
    public int mapY=0;
    public int depth=0;
    public int maxDepth;
    public int hitValue = 0;

    public Ray(float startingX, float startingY, float Angle){
        this.origin.setX(startingX);
        this.origin.setY(startingY);

        this.rayX = startingX;
        this.rayY = startingY;

        this.rayAngle = Angle;
    }

    public float getRayLength(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }


    public void trace(Map map){
        maxDepth = (int) (map.getMapSize() * 1.5);
        horizontalTrace(map);
        verticalTrace(map);
        mapX = map.getMapX(hitX);
        mapY = map.getMapY(hitY);
        hitValue = map.getTileContent(mapX,mapY);
        determineWallColor();
    }

    private void horizontalTrace(Map map){
        float xOffset = 0; float yOffset = 0;
        depth = 0;
        float distanceH= Float.MAX_VALUE,horX = 0, horY = 0;
        float angleTangent = (float) (-1/Math.tan(rayAngle));


        //if ray is angle down
        if (rayAngle > PI){
            rayY = (float) (((int) origin.getY() / map.getTileSize()) * (map.getTileSize()) -0.0001) ;
            rayX = ((origin.getY() - rayY) * angleTangent) + origin.getX();
            yOffset =-map.getTileSize(); xOffset = -yOffset * angleTangent;
        }
        //if ray is angle up
        if (rayAngle < PI){
            rayY = (float) (((int) origin.getY() / map.getTileSize()) * (map.getTileSize()) + map.getTileSize());
            rayX = ((origin.getY() - rayY) * angleTangent) + origin.getX();
            yOffset = map.getTileSize();  xOffset = -yOffset * angleTangent;
        }

        if (rayAngle == 0 || rayAngle == PI){
            rayX = origin.getX();
            rayY = origin.getY();
            depth = maxDepth;
        }

        while (depth < maxDepth){
            mapX = map.getMapX(rayX);
            mapY = map.getMapX(rayY);

            if (map.getTileContent(mapX, mapY)!= 0){

                horX = rayX;horY = rayY;
                distanceH = getRayLength(origin.getX(),origin.getY(),horX,horY);
                depth = maxDepth;

            }else {
                rayX += xOffset; rayY += yOffset;
                depth += 1;
            }
        }

        if (distanceH < disT){
            rayX = horX; rayY = horY; disT=distanceH; darker = false;
            this.hitX = rayX; this.hitY = rayY;
        }

    }
    public void verticalTrace(Map map){
        float xOffset = 0; float yOffset = 0;

        depth = 0;

        float distanceV = Float.MAX_VALUE,verX = 0, verY = 0;
        float negAngleTangent = (float) (-Math.tan(rayAngle));

        //if ray is angle left
        if (rayAngle > PI/2 && rayAngle < 3*PI/2){
            rayX = (float)(((int)origin.getX()/map.getTileSize()) * (map.getTileSize()) - 0.0001) ;
            rayY = ((origin.getX() - rayX) * negAngleTangent) + origin.getY();
            xOffset =-map.getTileSize(); yOffset =-xOffset * negAngleTangent;
        }
        //if ray is angle right
        if (rayAngle < PI/2 || rayAngle > 3*PI/2){
            rayX = (float)(((int)origin.getX()/map.getTileSize()) * (map.getTileSize()) + map.getTileSize()) ;
            rayY = ((origin.getX() - rayX) * negAngleTangent) + origin.getY();
            xOffset = map.getTileSize();  yOffset = -xOffset * negAngleTangent;
        }
        //looking straight up or down
        if (rayAngle == PI/2 || rayAngle == 3 * PI/2){

            rayX = origin.getX();
            rayY = origin.getY();
            depth = maxDepth;
        }

        while (depth < maxDepth){
            mapX = map.getMapX(rayX);
            mapY = map.getMapX(rayY);
            if (map.getTileContent(mapX,mapY) != 0){
                verX = rayX;
                verY = rayY;
                distanceV = getRayLength(origin.getX(),origin.getY(),verX,verY);
                depth = maxDepth;

            }else {
                rayX += xOffset; rayY += yOffset;
                depth += 1;
            }
        }

        if (distanceV < disT){
            rayX = verX; rayY = verY; disT=distanceV; darker = true;
            hitX = rayX; hitY = rayY;
        }

    }
    public void determineWallColor(){
        switch (hitValue){
            case 1:
                wallColor = Color.green;
                break;
            case 2:
                wallColor = Color.red;
                break;
            case 3:
                wallColor = Color.blue;
                break;
            case 4:
                wallColor = Color.orange;
                break;
            case 5:
                wallColor = Color.pink;
                break;
            case 6:
                wallColor = Color.yellow;
                break;
            case 7:
                wallColor = Color.darkGray;
                break;
            case 8:
                wallColor = Color.white;
                break;
            default:

                break;
        }
        if (darker) {wallColor = wallColor.darker();}
    }
    public void draw(Graphics g){

        g.setColor(this.wallColor);

        g.drawLine((int) origin.getX(), (int) origin.getY(), (int)hitX, (int)hitY);

    }
    public void drawIn3D(){

////            Fixes fish eye
//        float ca = player.transform.getAngleRadians() - rayAngle;
//        if (ca <    0){ ca+=2*PI; }
//        if (ca > 2*PI){ ca-=2*PI; }
//        disT = (float)(disT * cos(ca));
////            ends fix for fish eye
//        float lineH = (mapTileSize * 2 * maxRenderLineHeight) / disT;
//        if (lineH> maxRenderLineHeight){
//            lineH = maxRenderLineHeight;
//        }
//        float lineOffset = 160-lineH/2 + lineYOffset;
//        int lines= (int) (512.0/totalRays);
//
//        g.fillRect(rayNumber*lines + 530,(int)lineOffset + 100,lines,(int)lineH);
//
//        rayAngle += RAD/2;
//        if (rayAngle <    0){ rayAngle+=2*PI; }
//        if (rayAngle > 2*PI){ rayAngle-=2*PI; }
    }


}
