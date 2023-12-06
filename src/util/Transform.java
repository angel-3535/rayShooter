package util;


import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Transform {
    private Vector2D position = new Vector2D();
    private Vector2D size = new Vector2D();
    private Vector2D foward = new Vector2D();
    private Vector2D centerPoint = new Vector2D();

    private float angleDegrees = 0;

    private float angleRadians = 0;

    public Transform(){
        this(0,0,0,0);
    }

    public Transform(Vector2D p, Vector2D s){
        this(p.getX(),p.getY(),s.getX(),s.getY());
    }

    public Transform(float x, float y, float w, float h){
        position.setX(x);
        position.setY(y);
        size.setX(w);
        size.setY(h);
        this.setAngleRadians(0);
    }


    public float getX(){
        return position.getX();
    }

    public float getY(){
        return position.getY();
    }

    public float getWidth(){
        return size.getX();
    }

    public float getHeight(){
        return size.getY();
    }

    public float getCenterX(){
        return centerPoint.getX();
    }

    public float getCenterY(){
        return centerPoint.getY();
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
        calculateCenterPoint();
    }

    public void setPosition(float x, float y) {
        this.setPosition(new Vector2D(x,y));
    }

    public void movePositionBy(Vector2D moveBy){
        this.moveXBy(moveBy.getX());
        this.moveYBy(moveBy.getY());
    }

    public void setX(float x){
        this.setPosition(new Vector2D(x,this.getY()));
    }

    public void moveXBy(float x){
        this.setX(getX() + x);
    }

    public void setY(float y){
        this.setPosition(new Vector2D(this.getX(),y));
    }

    public void moveYBy(float y){
        this.setY(getY() + y);
    }

    public Vector2D getSize() {
        return size;
    }

    public void setSize(Vector2D size) {
        this.size = size;
        calculateCenterPoint();
    }

    public void setSize(float w, float h) {
        setSize(new Vector2D(w,h));
    }

    public void setWidth(float w){
        this.setSize(new Vector2D(w,this.getHeight()));
    }

    public void setHeight(float h){
        this.setSize(new Vector2D(this.getWidth(),h));
    }

    public Vector2D getCenterPoint() {
        return centerPoint;
    }


//    public Collider getAsCollider(){
//        return new Collider(
//                (int) this.getX(),
//                (int) this.getY(),
//                (int) this.getWidth(),
//                (int) this.getHeight()
//        );
//    }
    private void calculateCenterPoint() {
        this.centerPoint.setX(this.getX() + this.getWidth()/2);
        this.centerPoint.setY(this.getY() + this.getHeight()/2);
    }

    public Vector2D getFoward() {
        return foward;
    }

    private void setFoward() {
        this.foward = new Vector2D((float) cos(angleRadians), (float) sin(angleRadians));
        this.foward.normalize();
    }

    public float getAngleDegrees() {
        return angleDegrees;
    }

    public void setAngleDegrees(float angleDegrees) {
        this.angleDegrees = angleDegrees;

        if (this.angleDegrees < 0){
            this.angleDegrees += 360;
        }
        if (this.angleDegrees > 360){
            this.angleDegrees -= 360;
        }

        this.angleRadians = (float) Math.toRadians(angleDegrees);
        calculateFoward();

    }

    public float getAngleRadians() {
        return angleRadians;

    }

    public void setAngleRadians(float angleRadians) {
        this.angleRadians = angleRadians;

        if (this.angleRadians < 0){
            this.angleRadians += 2 * Math.PI;
        }
        if (this.angleRadians > 2 * Math.PI){
            this.angleRadians -= 2 * Math.PI;
        }

        this.angleDegrees = (float) Math.toDegrees(angleRadians);

        calculateFoward();
    }

    public void rotateAngleRadiansBy(float angle){
        this.setAngleRadians(getAngleRadians() + angle);
    }
    public void rotateAngleDegreesBy(float angle){
        this.setAngleDegrees(getAngleDegrees() + angle);
    }
    private void calculateFoward(){
//        playerDX = (float) cos(playerAngle) * 5;
//       playerDY = (float) Math.sin(playerAngle) * 5;
        Vector2D v = new Vector2D();
        v.setX((float) cos(getAngleRadians()));
        v.setY((float) sin(getAngleRadians()));
        v.normalize();
        foward.setX(v.getX()); foward.setY(v.getY());
    }

    public Vector2D getFowardNormal(){
        return new Vector2D(-foward.getY(), foward.getX());
    }



}
