import util.Map;
import util.Rect;
import util.io.KL;
import util.io.ML;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class EditorState implements State{
    Map[] maps;
    int CurrentMapIndex = 0;
    int[][] currentMap;
    int currentSelection = 0;
    int texSelMaxCol = 5;
    int texSelMaxRow = 5;

    Color c_layout = new Color(0xB04F4F);
    Color c_floor = new Color(0x76B04F);
    Color c_ceiling = new Color(0x4F6AB0);
    Color c_save = new Color(0x48BB4A);
    Rect r_layout = new Rect(40,40,80,30);
    Rect r_floor = new Rect(130,40,80,30);
    Rect r_ceiling = new Rect(220,40,80,30);
    Rect r_save = new Rect(700,400,80,30);
    ML ml;
    KL kl;

    Rect[] r_mapTiles;
    Rect[] r_texSelect;
//    Rect[]



    public EditorState(Map[] maps){
        this.maps = maps;
        this.currentMap = maps[CurrentMapIndex].layout;
        this.ml = ML.getMouseListener();
        this.kl = KL.getKeyListener();

        r_mapTiles = new Rect[maps[CurrentMapIndex].getMapSize() * maps[CurrentMapIndex].getMapSize()];

        int x,y,xo,yo;
        for (y = 0; y < currentMap.length; y++){
            for (x = 0; x < currentMap[y].length; x++){

                xo = x*16 + 100; yo = y*16 + 100;

                r_mapTiles[y* maps[CurrentMapIndex].getMapSize() + x] = new Rect(xo,yo,16,16);

            }
        }

        r_texSelect = new Rect[texSelMaxCol * texSelMaxRow];

        for (y = 0; y < texSelMaxCol; y++){
            for (x = 0; x < texSelMaxRow; x++){

                xo = x*34 + 700; yo = y*34 + 100;

                r_texSelect[y* texSelMaxRow + x] = new Rect(xo,yo,32,32);


            }
        }

    }
    private void drawMap(Graphics g) {
        int x,y,xo,yo;
        for (y = 0; y < currentMap.length; y++){
            for (x = 0; x < currentMap[y].length; x++){

                xo = x*16 + 100; yo = y*16 + 100;

                if (currentMap[y][x]!=0){
                    g.drawImage(
                            maps[CurrentMapIndex].getTexture(currentMap[y][x]).img.getImage(),
                            xo,
                            yo,
                            16,
                            16,
                            null);
                }
                g.setColor(Color.BLACK);
                g.drawRect(xo,yo,16,16);
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

        g.setColor(Color.black);
        g.drawString("Layout", r_layout.x + 20, r_layout.y + 20);

        g.drawString("Floor", r_floor.x + 20, r_floor.y + 20);

        g.drawString("Ceiling", r_ceiling.x + 20, r_ceiling.y + 20);

        g.drawString("Save", r_save.x + 20, r_save.y + 20);

        drawTextureSelection(g);

    }
    private void drawTextureSelection(Graphics g){
        int x,y,xo,yo;
        for (y = 0; y < texSelMaxCol; y++){
            for (x = 0; x < texSelMaxRow; x++){

                xo = x*34 + 700; yo = y*34 + 100;


                    g.drawImage(
                            maps[CurrentMapIndex].getTexture(y*5+x).img.getImage(),
                            xo,
                            yo,
                            32,
                            32,
                            null
                    );
                g.setColor(Color.BLACK);
                g.drawRect(xo,yo,32,32);
            }
        }
        g.setColor(Color.black);
    }
    @Override
    public void update(double dt) {

        if (ml.isPressed(MouseEvent.BUTTON1) && ml.isMouseInsideRect(r_layout)){
            currentMap = maps[CurrentMapIndex].layout;
        }
        if (ml.isPressed(MouseEvent.BUTTON1) && ml.isMouseInsideRect(r_floor)){
            currentMap = maps[CurrentMapIndex].floor;
        }
        if (ml.isPressed(MouseEvent.BUTTON1) && ml.isMouseInsideRect(r_ceiling)){
            currentMap = maps[CurrentMapIndex].ceiling;
        }

        if (ml.isPressed(MouseEvent.BUTTON1)){

            int x,y;
            for (y = 0; y < currentMap.length; y++){
                for (x = 0; x < currentMap[y].length; x++){
                    if (ml.isMouseInsideRect(r_mapTiles[y*currentMap.length + x])) {
                        currentMap[y][x] = currentSelection;
                        break;
                    }
                }
            }
        }
        if (ml.isPressed(MouseEvent.BUTTON1)){

            int x,y;
            for (y = 0; y < texSelMaxCol; y++){
                for (x = 0; x < texSelMaxRow; x++){
                    if (ml.isMouseInsideRect(r_texSelect[y*texSelMaxCol + x])) {
                        currentSelection = y*texSelMaxCol + x;
                        System.out.println("currentSelect: " + currentSelection);
                        break;
                    }
                }
            }
        }

        if (ml.isPressed(MouseEvent.BUTTON1)){
            int x,y;
            for (y = 0; y < texSelMaxCol; y++){
                for (x = 0; x < texSelMaxRow; x++){
                    if (ml.isMouseInsideRect(r_texSelect[y*texSelMaxCol + x])) {
                        currentSelection = y*texSelMaxCol + x;
                        System.out.println("currentSelect: " + currentSelection);
                        break;
                    }
                }
            }
        }

        if (ml.isPressed(MouseEvent.BUTTON1)){
            if (ml.isMouseInsideRect(r_save)){
                try {
                    String save = String.format("src/assets/levels/Elevel%d.dat",CurrentMapIndex);
                    FileOutputStream fos = new FileOutputStream(save);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(maps[CurrentMapIndex].mapA);

                } catch (Exception e) {

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
