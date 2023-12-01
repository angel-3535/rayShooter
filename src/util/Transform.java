package util;



public class Transform {
    private Vector2D position = new Vector2D();
    private Vector2D size = new Vector2D();
    private Vector2D centerPoint = new Vector2D();


    public Transform(){
        this(0,0,0,0);
    }

    public Transform(Vector2D p, Vector2D s){
        position = p;
        size = s;
    }

    public Transform(double x, double y, double w, double h){
        position.setX(x);
        position.setY(y);
        size.setX(w);
        size.setY(h);
    }


    public double getX(){
        return position.getX();
    }

    public double getY(){
        return position.getY();
    }

    public double getWidth(){
        return size.getX();
    }

    public double getHeight(){
        return size.getY();
    }

    public double getCenterX(){
        return centerPoint.getX();
    }

    public double getCenterY(){
        return centerPoint.getY();
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
        calculateCenterPoint();
    }

    public void setPosition(double x, double y) {
        this.setPosition(new Vector2D(x,y));
    }

    public void movePositionBy(Vector2D moveBy){
        this.moveXBy(moveBy.getX());
        this.moveYBy(moveBy.getY());
    }

    public void setX(double x){
        this.setPosition(new Vector2D(x,this.getY()));
    }

    public void moveXBy(double x){
        this.setX(getX() + x);
    }

    public void setY(double y){
        this.setPosition(new Vector2D(this.getX(),y));
    }

    public void moveYBy(double y){
        this.setY(getY() + y);
    }

    public Vector2D getSize() {
        return size;
    }

    public void setSize(Vector2D size) {
        this.size = size;
        calculateCenterPoint();
    }

    public void setSize(double w, double h) {
        setSize(new Vector2D(w,h));
    }

    public void setWidth(double w){
        this.setSize(new Vector2D(w,this.getHeight()));
    }

    public void setHeight(double h){
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



}
