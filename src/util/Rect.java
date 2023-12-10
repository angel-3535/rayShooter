package util;

public class Rect {

    public int x,y,w,h;


    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void moveBy(int dx, int dy)
    {
        x += dx;
        y += dy;
    }

    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean isPointInside(int i, int j){
        return (i<this.x+this.w && i>this.x) &&  (j<this.y+this.y && j>this.y);
    }

}
