package util;

public class Vector2D{
    private double x;
    private double y;


    public static Vector2D NorthWest = new Vector2D(-Math.sqrt(2)/2.0,-Math.sqrt(2)/2.0);
    public static Vector2D north = new Vector2D(0,-1);
    public static Vector2D northEast = new Vector2D(Math.sqrt(2)/2.0,-Math.sqrt(2)/2.0);
    public static Vector2D East = new Vector2D(1,0);
    public static Vector2D SouthEast = new Vector2D(Math.sqrt(2)/2.0,Math.sqrt(2)/2.0);
    public static Vector2D South = new Vector2D(0,1);
    public static Vector2D SouthWest = new Vector2D(-Math.sqrt(2)/2.0,Math.sqrt(2)/2.0);
    public static Vector2D West = new Vector2D(-1,0);

    public Vector2D(){
        x = 0;
        y = 0;
    }
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D position) {
        this(position.getX(), position.getY());
    }

    public double getMagnitude(){
        return Math.sqrt(x * x + y * y);
    }

    public void normalize(){
        double movementVectorMagnitude = Math.sqrt(x * x + y * y);

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

    public Vector2D multiply(double s){
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void rotate(double angle, double rotationX, double rotationY){
        double RadiantsAngle = Math.toRadians(angle);

        //place vector at origin
        setX(this.x - rotationX);
        setY(this.y - rotationY);

        //rotate
        //x` = xcos(θ)−ysin(θ)
        //y` = xsin(θ)+ycos(θ)
        double Ox = getX();
        double Oy = getY();

        setX( Ox * Math.cos(RadiantsAngle) - Oy * Math.sin(RadiantsAngle));
        setY( Ox * Math.sin(RadiantsAngle) + Oy * Math.cos(RadiantsAngle));

        //setVectorBackToLocation

        setX(this.x + rotationX);
        setY(this.y + rotationY);
    }

    public void rotate(double angle, Vector2D rotationPoint){
        rotate(angle, rotationPoint.x, rotationPoint.y);
    }
}
