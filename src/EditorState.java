import util.Map;
import util.Rect;
import util.io.KL;
import util.io.ML;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class EditorState implements State {
    Map[] maps;
    int currentMapIndex = 0;
    int[][] currentMap;
    int currentSelection = 0;
    int texSelMaxRow = 14;
    int texSelMaxCol = 7;

    Color c_layout = new Color(0xB04F4F);
    Color c_floor = new Color(0x76B04F);
    Color c_ceiling = new Color(0x4F6AB0);
    Color c_save = new Color(0x48BB4A);
    Rect r_layout = new Rect(40, 40, 80, 30);
    Rect r_floor = new Rect(130, 40, 80, 30);
    Rect r_ceiling = new Rect(220, 40, 80, 30);
    Rect r_save = new Rect(700, 400, 80, 30);
    ML ml;
    double m1Check = 0;
    KL kl;
    Rect[] r_mapTiles;
    Rect[][] r_texSelect;
    Color c_levels = new Color(0xB49E9E);
    Rect[] r_levels;


    public EditorState(Map[] maps) {
        this.maps = maps;
        this.currentMap = maps[currentMapIndex].layout;
        this.ml = ML.getMouseListener();
        this.kl = KL.getKeyListener();

        r_mapTiles = new Rect[maps[currentMapIndex].getMapSize() * maps[currentMapIndex].getMapSize()];

        int x, y, xo, yo;
        for (y = 0; y < currentMap.length; y++) {
            for (x = 0; x < currentMap[y].length; x++) {

                xo = x * 16 + 100;
                yo = y * 16 + 100;

                r_mapTiles[y * maps[currentMapIndex].getMapSize() + x] = new Rect(xo, yo, 16, 16);

            }
        }

        r_texSelect = new Rect[texSelMaxRow][texSelMaxCol];

        for (y = 0; y < texSelMaxRow; y++) {
            for (x = 0; x < texSelMaxCol; x++) {

                xo = x * 34 + 700;
                yo = y * 34 + 100;

                r_texSelect[y][x] = new Rect(xo, yo, 32, 32);


            }
        }

        r_save = new Rect(700, texSelMaxRow * 34 + 100, 80, 30);

        r_levels = new Rect[maps.length];
        for (int i = 0; i < maps.length; i++) {
            r_levels[i] = new Rect(40, 50 * i + 100, 40, 40);
        }
    }
    private void drawMap(Graphics g) {
        int x, y, xo, yo;
        for (y = 0; y < currentMap.length; y++) {
            for (x = 0; x < currentMap[y].length; x++) {

                xo = x * 16 + 100;
                yo = y * 16 + 100;

                if (currentMap[y][x] != 0) {
                    g.drawImage(
                            maps[currentMapIndex].getTexture(currentMap[y][x]).img.getImage(),
                            xo,
                            yo,
                            16,
                            16,
                            null);
                }
                g.setColor(Color.BLACK);
                g.drawRect(xo, yo, 16, 16);
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

        drawTextureSelection(g);

    }
    private void drawTextureSelection(Graphics g) {
        int x, y, xo, yo;
        for (y = 0; y < texSelMaxRow; y++) {
            for (x = 0; x < texSelMaxCol; x++) {

                xo = x * 34 + 700;
                yo = y * 34 + 100;



                g.drawImage(
                        maps[currentMapIndex].getTexture(y * texSelMaxCol + x).img.getImage(),
                        xo,
                        yo,
                        32,
                        32,
                        null
                );
                if (y * 5 + x == 0){
                    g.setColor(Color.white);
                    g.fillRect(xo, yo, 32, 32);
                }
                g.setColor(Color.BLACK);
                g.drawRect(xo, yo, 32, 32);
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
                currentMap = maps[currentMapIndex].layout;
            }

            if (ml.isMouseInsideRect(r_floor)) {
                currentMap = maps[currentMapIndex].floor;
            }

            if (ml.isMouseInsideRect(r_ceiling)) {
                currentMap = maps[currentMapIndex].ceiling;
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
                    currentMap = maps[currentMapIndex].layout;
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
