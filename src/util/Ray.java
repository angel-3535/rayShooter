package util;

import java.awt.*;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Ray {
    public static final float RAD = 0.0174533F;
    public float   originX,
            originY,
            rayAngle,
            hitX,
            hitY,
            Length,
            rayX = originX,
            rayY = originY,
            disT = 0;
             boolean darker = false;
            Color wallColor = Color.green;


    public int
            rayNumber=0,
            mapX=0,
            mapY=0,
            depth=0,
            maxDepth ,
            mapTileSize;

    public Ray(float startingX, float startingY, float Angle){
        this.originX = startingX;
        this.originY = startingY;
        this.rayAngle = Angle;
    }

    public float getRayLength(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    public void trace(Map map){
        mapTileSize = map.getTileSize();
        maxDepth = map.getMapSize();
        float
                xOffset = 0,
                yOffset = 0;
        wallColor = Color.green;


        if (rayAngle <    0){ rayAngle+=2*PI; }
        if (rayAngle > 2*PI){ rayAngle-=2*PI; }


            //Horizontal line check;
            depth = 0;
            float distanceH= 100000,horX = 0, horY = 0;
            float angleTangent = (float) (-1/Math.tan(rayAngle));


            //if ray is angle down
            if (rayAngle > PI){
                rayY = (float) (((int) originY / map.getMapSize()) * (mapTileSize) -0.0001) ;
                rayX = ((originY - rayY) * angleTangent) + originX;
                yOffset =-mapTileSize; xOffset = -yOffset * angleTangent;
            }
            //if ray is angle up
            if (rayAngle < PI){
                //rounding the float to an aprox (dividing by 64 and then multiplying by 64)
                rayY = (float) (((int) originY / mapTileSize) * (mapTileSize) + mapTileSize);
                rayX = ((originY - rayY) * angleTangent) + originX;
                yOffset = mapTileSize;  xOffset = -yOffset * angleTangent;
            }

            if (rayAngle == 0 || rayAngle == PI){
                rayX = originX;
                rayY = originY;
                depth = maxDepth;
            }

            while (depth < maxDepth){
                mapX = map.getMapX(rayX);
                mapY = map.getMapX(rayY);


//                mapX = clamp(mapX, 0, mapXSize-1);  mapY = clamp(mapY, 0, mapYSize-1);
                if (map.getTileContent(mapX, mapY)!= 0){

                    horX = rayX;horY = rayY;
                    distanceH = getRayLength(originX,originY,horX,horY);
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
                rayX = (float)(((int)originX/mapTileSize) * (mapTileSize) - 0.0001) ;
                rayY = ((originX - rayX) * negAngleTangent) + originY;
                xOffset =-mapTileSize; yOffset =-xOffset * negAngleTangent;
            }
            //if ray is angle right
            if (rayAngle < PI/2 || rayAngle > 3*PI/2){
                rayX = (float)(((int)originX/mapTileSize) * (mapTileSize) + mapTileSize) ;
                rayY = ((originX - rayX) * negAngleTangent) + originY;
                xOffset = mapTileSize;  yOffset = -xOffset * negAngleTangent;
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
            if ( distanceH>distanceV){rayX = verX; rayY = verY; disT=distanceV; wallColor = Color.green;}
            else                     {rayX = horX; rayY = horY; disT=distanceH; darker = true;}

            mapX = map.getMapX(rayX);
            mapY = map.getMapX(rayY);

            if (map.getTileContent(mapX,mapY) == 1) {wallColor = Color.green;}
            if (map.getTileContent(mapX,mapY) == 2) {wallColor = Color.red;}
            if (map.getTileContent(mapX,mapY) == 3) {wallColor = Color.blue;}
            if (map.getTileContent(mapX,mapY) == 4) {wallColor = Color.orange;}
            if (darker) {wallColor = wallColor.darker();}


    }
    public int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public void draw(Graphics g){

        g.setColor(this.wallColor);

        g.drawLine((int) originX, (int)originY, (int)rayX, (int)rayY);



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
