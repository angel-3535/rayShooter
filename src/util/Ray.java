package util;

import gfx.Renderable;

import java.awt.*;
import static java.lang.Math.PI;

public class Ray implements Renderable {
    public static final float RAD = 0.0174533F;
    public float originX;
    public float originY;
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
        this.originX = startingX;
        this.originY = startingY;

        this.rayX = startingX;
        this.rayY = startingY;

        this.rayAngle = Angle;
    }

    public float getRayLength(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }


    public void trace(Map map){
        maxDepth = 32;
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
            rayY = (float) (((int) originY / map.getTileSize()) * (map.getTileSize()) -0.0001) ;
            rayX = ((originY - rayY) * angleTangent) + originX;
            yOffset =-map.getTileSize(); xOffset = -yOffset * angleTangent;
        }
        //if ray is angle up
        if (rayAngle < PI){
            rayY = (float) (((int) originY / map.getTileSize()) * (map.getTileSize()) + map.getTileSize());
            rayX = ((originY - rayY) * angleTangent) + originX;
            yOffset = map.getTileSize();  xOffset = -yOffset * angleTangent;
        }

        if (rayAngle == 0 || rayAngle == PI){
            rayX = originX;
            rayY = originY;
            depth = maxDepth;
        }

        while (depth < maxDepth){
            mapX = map.getMapX(rayX);
            mapY = map.getMapX(rayY);

            if (map.getTileContent(mapX, mapY)!= 0){

                horX = rayX;horY = rayY;
                distanceH = getRayLength(originX,originY,horX,horY);
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
            rayX = (float)(((int)originX/map.getTileSize()) * (map.getTileSize()) - 0.0001) ;
            rayY = ((originX - rayX) * negAngleTangent) + originY;
            xOffset =-map.getTileSize(); yOffset =-xOffset * negAngleTangent;
        }
        //if ray is angle right
        if (rayAngle < PI/2 || rayAngle > 3*PI/2){
            rayX = (float)(((int)originX/map.getTileSize()) * (map.getTileSize()) + map.getTileSize()) ;
            rayY = ((originX - rayX) * negAngleTangent) + originY;
            xOffset = map.getTileSize();  yOffset = -xOffset * negAngleTangent;
        }
        //looking straight up or down
        if (rayAngle == PI/2 || rayAngle == 3 * PI/2){

            rayX = originX;
            rayY = originY;
            depth = maxDepth;
        }

        while (depth < maxDepth){
            mapX = map.getMapX(rayX);
            mapY = map.getMapX(rayY);
            if (map.getTileContent(mapX,mapY) != 0){
                verX = rayX;
                verY = rayY;
                distanceV = getRayLength(originX,originY,verX,verY);
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
                wallColor = Color.blue;
                break;
            default:

                break;
        }
        if (darker) {wallColor = wallColor.darker();}
    }
    public void draw(Graphics g){

        g.setColor(this.wallColor);

        g.drawLine((int) originX, (int)originY, (int)hitX, (int)hitY);

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
