import gfx.Window;
import util.Map;
import util.Rect;
import util.io.KL;
import util.io.ML;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class EditorState implements State {

    Map[] maps;
    int currentMapIndex;
    int[][] currentMap;
    int mapState = 0;

    int currentSelection = 0;
    int texSelMaxRow = 29;
    int texSelMaxCol = 25;


    Color c_layout = new Color(0xB04F4F);
    Color c_floor = new Color(0x76B04F);
    Color c_ceiling = new Color(0x4F6AB0);
    Color c_save = new Color(0x48BB4A);
    Color c_load = new Color(0xBBA848);
    Rect r_layout = new Rect(40, 40, 80, 30);
    Rect r_floor = new Rect(130, 40, 80, 30);
    Rect r_ceiling = new Rect(220, 40, 80, 30);
    Rect r_save;
    Rect r_load;
    ML ml;
    double m1Check = 0;
    KL kl;
    Rect[] r_mapTiles;
    Rect[][] r_texSelect;
    Color c_levels = new Color(0xB49E9E);
    Rect[] r_levels;
    Color c_shade = new Color(0x45000000, true);
    int mapTileSize = 27;
    int texTileSize = 32;
    int editorOffSet = (int) (Window.getWindow().getWidth()* 0.555);



    public EditorState(Map[] maps, int index) {
        currentMapIndex  = index;
        this.maps = maps;
        this.currentMap = maps[currentMapIndex].layout;
        this.ml = ML.getMouseListener();
        this.kl = KL.getKeyListener();

        r_mapTiles = new Rect[maps[currentMapIndex].getMapSize() * maps[currentMapIndex].getMapSize()];

        int x, y, xo, yo;
        for (y = 0; y < currentMap.length; y++) {
            for (x = 0; x < currentMap[y].length; x++) {


                xo = x * mapTileSize + 100;
                yo = y * mapTileSize + 100;

                r_mapTiles[y * maps[currentMapIndex].getMapSize() + x] = new Rect(xo, yo, mapTileSize, mapTileSize);

            }
        }

        r_texSelect = new Rect[texSelMaxRow][texSelMaxCol];

        for (y = 0; y < texSelMaxRow; y++) {
            for (x = 0; x < texSelMaxCol; x++) {

                xo = x * texTileSize + 2 + editorOffSet;
                yo = y * texTileSize + 2 +100;

                r_texSelect[y][x] = new Rect(xo, yo, texTileSize, texTileSize);


            }
        }

        r_save = new Rect(editorOffSet, texSelMaxRow * texTileSize + 2 + 100, 80, 30);
        r_load = new Rect(editorOffSet + r_save.w + 10, texSelMaxRow * texTileSize + 2 + 100, 80, 30);

        r_levels = new Rect[maps.length];
        for (int i = 0; i < maps.length; i++) {
            r_levels[i] = new Rect(40, 50 * i + 100, 40, 40);
        }
    }
    private void drawMap(Graphics g) {
        int x, y, xo, yo;
        for (y = 0; y < currentMap.length; y++) {
            for (x = 0; x < currentMap[y].length; x++) {

                xo = x * mapTileSize + 100;
                yo = y * mapTileSize + 100;

                if (currentMap[y][x] != 0) {
                    g.drawImage(
                            maps[currentMapIndex].getTexture(currentMap[y][x]).img.getImage(),
                            xo,
                            yo,
                            mapTileSize,
                            mapTileSize,
                            null
                    );
                    g.setColor(c_shade);

                    if (mapState!=0){
                        if (maps[currentMapIndex].layout[y][x] !=0) {
                            g.fillRect(xo, yo, mapTileSize, mapTileSize);
                        }

                    }
                }
                g.setColor(Color.BLACK);
                g.drawRect(xo, yo, mapTileSize, mapTileSize);
            }
        }
    }
    private void drawButtons(Graphics g) {
        g.setColor(c_layout);
        g.fillRect(r_layout.x, r_layout.y, r_layout.w, r_layout.h);


        g.setColor(c_floor);
        g.fillRect(r_floor.x, r_floor.y, r_floor.w, r_floor.h);


        g.setColor(c_ceiling);
        g.fillRect(r_ceiling.x, r_ceiling.y, r_ceiling.w, r_ceiling.h);

        g.setColor(c_save);
        g.fillRect(r_save.x, r_save.y, r_save.w, r_save.h);
        g.setColor(c_load);
        g.fillRect(r_load.x, r_load.y, r_load.w, r_load.h);


        for (int i = 0; i < r_levels.length; i++) {
            g.setColor(c_levels);
            g.fillRect(r_levels[i].x, r_levels[i].y, r_levels[i].w, r_levels[i].h);
            g.setColor(Color.BLACK);
            g.drawString(String.format("Level %d", i), r_levels[i].x, r_levels[i].y + r_levels[i].h / 2);
        }

        g.setColor(Color.black);
        g.drawString("Layout", r_layout.x + 20, r_layout.y + 20);

        g.drawString("Floor", r_floor.x + 20, r_floor.y + 20);

        g.drawString("Ceiling", r_ceiling.x + 20, r_ceiling.y + 20);

        g.drawString("Save", r_save.x + 20, r_save.y + 20);
        g.drawString("Load", r_load.x + 20, r_load.y + 20);


        drawTextureSelection(g);

    }
    private void drawTextureSelection(Graphics g) {
        for (int y = 0; y < texSelMaxRow; y++) {
            for (int x = 0; x < texSelMaxCol; x++) {



                g.drawImage(
                        maps[currentMapIndex].getTexture(y * texSelMaxCol + x).img.getImage(),
                        r_texSelect[y][x].x,
                        r_texSelect[y][x].y,
                        r_texSelect[y][x].w,
                        r_texSelect[y][x].h,
                        null
                );
                if (y * texSelMaxCol + x == 0){
                    g.setColor(Color.white);
                    g.fillRect( r_texSelect[y][x].x, r_texSelect[y][x].y, r_texSelect[y][x].w, r_texSelect[y][x].h);
                }
                g.setColor(Color.BLACK);
                g.drawRect( r_texSelect[y][x].x, r_texSelect[y][x].y, r_texSelect[y][x].w, r_texSelect[y][x].h);
            }
        }
        g.setColor(Color.black);
    }
    @Override
    public void update(double dt) {
        m1Check -=dt;

        if (m1Check<=0 && ml.isM1Down ) {
            m1Check = 0.05;
            if (ml.isMouseInsideRect(r_layout)) {
                mapState = 0;
                currentMap = maps[currentMapIndex].mapA[mapState];
            }

            if (ml.isMouseInsideRect(r_floor)) {
                mapState = 1;
                currentMap = maps[currentMapIndex].mapA[mapState];
            }

            if (ml.isMouseInsideRect(r_ceiling)) {
                mapState = 2;
                currentMap = maps[currentMapIndex].mapA[mapState];
            }

            for (int y = 0; y < currentMap.length; y++) {
                for (int x = 0; x < currentMap[y].length; x++) {
                    if (ml.isMouseInsideRect(r_mapTiles[y * currentMap.length + x])) {
                        currentMap[y][x] = currentSelection;
                        break;
                    }
                }
            }

            for (int y = 0; y < texSelMaxRow; y++) {
                for (int x = 0; x < texSelMaxCol; x++) {
                    if (ml.isMouseInsideRect(r_texSelect[y][x])) {

                        currentSelection = y * texSelMaxCol + x;
                        System.out.println("currentSelect: " + currentSelection);
                        break;
                    }
                }
            }

            for (int i = 0; i < r_levels.length; i++) {
                if (ml.isMouseInsideRect(r_levels[i])) {
                    currentMapIndex = i;
                    Map.debugIndex = i;
                    currentMap = maps[currentMapIndex].mapA[mapState];
                }
            }

            if (ml.isMouseInsideRect(r_save)) {
                try {
                    String save = String.format("src/assets/levels/Elevel%d.dat", currentMapIndex);

                    FileOutputStream fos = new FileOutputStream(save);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(maps[currentMapIndex].mapA);

                } catch (Exception e) {

                }

            }
//FIXME loading from editor
            if (ml.isMouseInsideRect(r_load)) {
                try {
//
//                    String path = String.format("src/assets/levels/Elevel%d.dat",currentMapIndex);
//                    maps[currentMapIndex] = new Map(path);
                    String load = String.format("src/assets/levels/Elevel%d.dat", currentMapIndex);

                    FileInputStream fis = new FileInputStream(load);
                    ObjectInputStream iis = new ObjectInputStream(fis);
                    maps[currentMapIndex].mapA = (int[][][]) iis.readObject();

                    maps[currentMapIndex].layout = maps[currentMapIndex].mapA[0];
                    maps[currentMapIndex].floor = maps[currentMapIndex].mapA[1];
                    maps[currentMapIndex].ceiling = maps[currentMapIndex].mapA[2];

                } catch (Exception e) {
                    System.out.println("fail load");
                }

            }

        }

        if (ml.isPressed(MouseEvent.BUTTON2)){
            for (int y = 0; y < currentMap.length; y++) {
                for (int x = 0; x < currentMap[y].length; x++) {
                    if (ml.isMouseInsideRect(r_mapTiles[y * currentMap.length + x])) {
                        currentSelection =  currentMap[y][x];
                        break;
                    }
                }
            }
        }

        if (ml.isPressed(MouseEvent.BUTTON3)){
            for (int y = 0; y < currentMap.length; y++) {
                for (int x = 0; x < currentMap[y].length; x++) {
                    if (ml.isMouseInsideRect(r_mapTiles[y * currentMap.length + x])) {
                        currentMap[y][x] = 0;
                        break;
                    }
                }
            }
        }


        if (kl.isKeyDown(KeyEvent.VK_C)){
            for (int y = 0; y < currentMap.length; y++) {
                for (int x = 0; x < currentMap[y].length; x++) {
                    currentMap[y][x] = currentSelection;
                }
            }
        }

        if (kl.isKeyDown(KeyEvent.VK_V)){
            for (int y = 1; y < currentMap.length-1; y++) {
                for (int x = 1; x < currentMap[y].length-1; x++) {
                    currentMap[y][x] = currentSelection;
                }
            }
        }
        if (kl.isKeyDown(KeyEvent.VK_W)){
            for (int y = 0; y < currentMap.length; y++) {
                for (int x = 0; x < currentMap[y].length; x++) {
                    if (y==0||x==0 ||y==currentMap.length -1 || x ==currentMap[y].length-1){
                        currentMap[y][x] = currentSelection;
                    }
                }
            }
        }

    }
    @Override
    public void draw(Graphics g) {

        drawButtons(g);
        drawMap(g);

    }
}
