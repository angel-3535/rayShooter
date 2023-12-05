package util;

import assets.Rooms;

import static util.RMath.clamp;

public class Map {
    public int[][] layout;

    float TileSize;

    public int getTileSize() {
        return (int) TileSize;
    }

    public Map(){
        layout = Rooms.room1;
        TileSize = 16;
    }

    public Vector2D getMapPos(Vector2D v){
        return  this.getMapPos(v.getX(),v.getY());
    }
    public Vector2D getMapPos(float x, float y){
        Vector2D v = new Vector2D();
        v.setX(((int) x/ TileSize));
        v.setY(((int) y/ TileSize));

        return v;
    }

    public int getMapX(float x){
        return (int) ((int) x/ TileSize);
    }
    public int getMapY(float y){
        return (int) ((int) y/ TileSize);
    }

    public int getTileContent(int x, int y){
        x = clamp(x, 0, getMapSize() -1 );
        y = clamp(y, 0, getMapSize() -1 );
        return layout[y][x];
    }

    public int getMapSize(){
        return layout.length;
    }
}
