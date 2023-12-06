package util;

import assets.Rooms;
import gfx.Renderable;

import java.awt.*;

import static util.RMath.clamp;

public class Map implements Renderable {
    public int[][] layout;

    float TileSize;

    public int getTileSize() {
        return (int) TileSize;
    }

    public Map(){
        layout = Rooms.room1[0];
        TileSize = 64;
    }

    public Vector2D getMapPos(Vector2D v){
        Vector2D r = this.getMapPos(v.getX(),v.getY());
        return  r;
    }
    public Vector2D getMapPos(float x, float y){
        Vector2D v = new Vector2D();
        v.setX(((int) x/ TileSize));
        v.setY(((int) y/ TileSize));

        return v;
    }

    public int getMapX(float x){
        return (int) (x/ TileSize);
    }
    public int getMapY(float y){
        return (int) (y/ TileSize);
    }

    public int getTileContent(int x, int y){
        x = clamp(x, 0, getMapSize() -1 );
        y = clamp(y, 0, getMapSize() -1 );
        return layout[y][x];
    }
    public void setTileContent(int x, int y, int value){
        x = clamp(x, 0, getMapSize() -1 );
        y = clamp(y, 0, getMapSize() -1 );
        layout[y][x] = value;
    }

    public int getMapSize(){
        return layout.length;
    }

    public void draw(Graphics g){
        int x,y,xo,yo;
        for (y = 0; y < this.getMapSize(); y++){
            for (x = 0; x < this.getMapSize(); x++){

                if (this.getTileContent(x,y)!=0){
                    g.setColor(Color.white);
                }else{
                    g.setColor(Color.black);
                }

                xo = x* this.getTileSize(); yo = y* this.getTileSize();

                g.fillRect(xo,yo, this.getTileSize(), this.getTileSize());

                g.setColor(Color.GRAY);
                g.drawRect(xo,yo, this.getTileSize(), this.getTileSize());

            }
        }
    }

}
