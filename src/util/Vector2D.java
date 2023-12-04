package util;

import static java.lang.Math.cos;

public class Vector2D{
    private float x;
    private float y;


    public static Vector2D NorthWest = new Vector2D((float) (-Math.sqrt(2)/2.0), (float) (-Math.sqrt(2)/2.0));
    public static Vector2D north = new Vector2D(0,-1);
    public static Vector2D northEast = new Vector2D((float) (Math.sqrt(2)/2.0), (float) (-Math.sqrt(2)/2.0));
    public static Vector2D East = new Vector2D(1,0);
    public static Vector2D SouthEast = new Vector2D((float) (Math.sqrt(2)/2.0), (float) ( Math.sqrt(2)/2.0));
    public static Vector2D South = new Vector2D(0,1);
    public static Vector2D SouthWest = new Vector2D((float) (-Math.sqrt(2)/2.0), (float) (Math.sqrt(2)/2.0));
    public static Vector2D West = new Vector2D(-1,0);

    public Vector2D(){
        x = 0;
        y = 0;
    }
    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D position) {
        this(position.getX(), position.getY());
    }



    public float getMagnitude(){
        return (float) Math.sqrt(x * x + y * y);
    }

    public void normalize(){
        float movementVectorMagnitude = (float) Math.sqrt(x * x + y * y);

        x = movementVectorMagnitude != 0 ? x / movementVectorMagnitude: x;
        y = movementVectorMagnitude != 0 ? y / movementVectorMagnitude: y;
    }

    public Vector2D getNormalize(){
        Vector2D result = new Vector2D(x,y);
        result.normalize();
        return result;
    }


    public void add(Vector2D v){
        x += v.x;
        y += v.y;
    }

    public void subtract(Vector2D v){
        x -= v.x;
        y -= v.y;
    }

    public Vector2D multiply(float s){
        x *= s;
        y *= s;
        return this;
    }


    public Vector2D getVectorToNorm(Vector2D v){
        Vector2D ret = new Vector2D(v.x - x, v.y - y);
        ret.normalize();


        return ret;
    }

    public Vector2D getVectorTo(Vector2D v){
        Vector2D ret = new Vector2D(v.x - x, v.y - y);

        return ret;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void rotate(float angle, float rotationX, float rotationY){
        float RadiantsAngle = (float) Math.toRadians(angle);

        //place vector at origin
        setX(this.x - rotationX);
        setY(this.y - rotationY);

        //rotate
        //x` = xcos(θ)−ysin(θ)
        //y` = xsin(θ)+ycos(θ)
        float Ox = getX();
        float Oy = getY();

        setX((float) (Ox * Math.cos(RadiantsAngle) - Oy * Math.sin(RadiantsAngle)));
        setY((float) (Ox * Math.sin(RadiantsAngle) + Oy * Math.cos(RadiantsAngle)));

        //setVectorBackToLocation

        setX(this.x + rotationX);
        setY(this.y + rotationY);
    }

    public void rotate(float angle, Vector2D rotationPoint){
        rotate(angle, rotationPoint.x, rotationPoint.y);
    }


}
