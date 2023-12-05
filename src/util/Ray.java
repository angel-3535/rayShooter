package util;

import gfx.Renderable;

import java.awt.*;
import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Ray implements Renderable {
    public static final float RAD = 0.0174533F;
    public Vector2D origin = new Vector2D();
    public Vector2D hit = new Vector2D();
    public Vector2D rayPos = new Vector2D();
    public Vector2D mapPos = new Vector2D();
    public float rayAngle;
    public float disT = Float.MAX_VALUE;
    public int maxDepth;
    public int depth=0;
    public int hitValue = 0;
    boolean darker = false;
    public Color wallColor = Color.green;

    public Ray(float startingX, float startingY, float Angle){
        this.origin.setX(startingX);
        this.origin.setY(startingY);

        rayPos.setX(startingX);
        rayPos.setY(startingY);

        this.rayAngle = Angle;
    }

    public float getRayLength(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }


    public void trace(Map map){
        maxDepth = (int) (map.getMapSize() * 1.5);
        horizontalTrace(map);
        verticalTrace(map);
        mapPos.setX( map.getMapX(hit.getX()) );
        mapPos.setY( map.getMapY(hit.getY()) );
        hitValue = map.getTileContent((int) mapPos.getX(), (int) mapPos.getY());
        determineWallColor();
    }

    private void horizontalTrace(Map map){
        float xOffset = 0; float yOffset = 0;
        depth = 0;
        float distanceH= Float.MAX_VALUE,horX = 0, horY = 0;
        float angleTangent = (float) (-1/Math.tan(rayAngle));


        //if ray is angle down
        if (rayAngle > PI){
            rayPos.setY((float) (((int) origin.getY() / map.getTileSize()) * (map.getTileSize()) -0.0001));
            rayPos.setX(((origin.getY() - rayPos.getY()) * angleTangent) + origin.getX()) ;
            yOffset =-map.getTileSize(); xOffset = -yOffset * angleTangent;
        }
        //if ray is angle up
        if (rayAngle < PI){
            rayPos.setY((float) (((int) origin.getY() / map.getTileSize()) * (map.getTileSize()) + map.getTileSize()));
            rayPos.setX(((origin.getY() - rayPos.getY()) * angleTangent) + origin.getX());
            yOffset = map.getTileSize();  xOffset = -yOffset * angleTangent;
        }

        if (rayAngle == 0 || rayAngle == PI){
            rayPos.setX(origin.getX());
            rayPos.setY(origin.getY());
            depth = maxDepth;
        }

        while (depth < maxDepth){
            mapPos.setX(map.getMapX(rayPos.getX()));
            mapPos.setY(map.getMapY(rayPos.getY()));

            if (map.getTileContent((int) mapPos.getX(), (int) mapPos.getY())!= 0){

                horX = rayPos.getX();horY = rayPos.getY();
                distanceH = getRayLength(origin.getX(),origin.getY(),horX,horY);
                depth = maxDepth;

            }else {
                rayPos.setX(rayPos.getX() + xOffset); rayPos.setY(rayPos.getY() + yOffset) ;
                depth += 1;
            }
        }

        if (distanceH < disT){
            rayPos.setX(horX);
            rayPos.setY(horY);
            disT=distanceH;
            darker = false;
            hit.setX(rayPos.getX()); hit.setY(rayPos.getY());
        }

    }
    public void verticalTrace(Map map){
        float xOffset = 0; float yOffset = 0;

        depth = 0;

        float distanceV = Float.MAX_VALUE,verX = 0, verY = 0;
        float negAngleTangent = (float) (-Math.tan(rayAngle));

        //if ray is angle left
        if (rayAngle > PI/2 && rayAngle < 3*PI/2){
            rayPos.setX((float)(((int)origin.getX()/map.getTileSize()) * (map.getTileSize()) - 0.0001));
            rayPos.setY( ((origin.getX() - rayPos.getX()) * negAngleTangent) + origin.getY());
            xOffset =-map.getTileSize(); yOffset =-xOffset * negAngleTangent;
        }
        //if ray is angle right
        if (rayAngle < PI/2 || rayAngle > 3*PI/2){
            rayPos.setX((float)(((int)origin.getX()/map.getTileSize()) * (map.getTileSize()) + map.getTileSize()));
            rayPos.setY(((origin.getX() - rayPos.getX()) * negAngleTangent) + origin.getY());
            xOffset = map.getTileSize();  yOffset = -xOffset * negAngleTangent;
        }
        //looking straight up or down
        if (rayAngle == PI/2 || rayAngle == 3 * PI/2){

            rayPos.setX(origin.getX());
            rayPos.setY(origin.getY());
            depth = maxDepth;
        }

        while (depth < maxDepth){
            mapPos.setX(map.getMapX(rayPos.getX()));
            mapPos.setY(map.getMapY(rayPos.getY()));

            if (map.getTileContent((int) mapPos.getX(), (int) mapPos.getY()) != 0){
                verX = rayPos.getX();
                verY = rayPos.getY();
                distanceV = getRayLength(origin.getX(),origin.getY(),verX,verY);
                depth = maxDepth;

            }else {
                rayPos.setX(rayPos.getX() + xOffset);
                rayPos.setY(rayPos.getY() + yOffset);
                depth += 1;
            }
        }

        if (distanceV < disT){
            rayPos.setX(verX);
            rayPos.setY(verY);
            disT=distanceV;
            darker = true;
            hit.setX(rayPos.getX());
            hit.setY(rayPos.getY());
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

        g.drawLine((int) origin.getX(), (int) origin.getY(), (int)hit.getX(), (int)hit.getY());

    }


}
