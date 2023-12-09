import util.Map;
import util.Transform;
import util.Vector2D;
import util.io.KL;

import java.awt.event.KeyEvent;

public class Player {
    Transform transform;
    KL kl = KL.getKeyListener();
    public float moveSpeed;
    public float rotationSpeed = 270f;
    float reach;

    public Map map;

    public Player(float movementSpeed, Map map){
        transform = new Transform(30,300,8,8);
        this.moveSpeed = movementSpeed;
        this.map = map;
        reach = (float) (map.getTileSize() * 1.5);
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

    public void draw(){

    }
}
