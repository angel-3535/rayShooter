import util.Map;
import util.Rect;
import util.io.KL;
import util.io.ML;

import java.awt.*;
import java.awt.event.MouseEvent;

public class EditorState implements State{
    Map map;
    int[][] currentMap;
    
    Rect layout = new Rect(40,40,80,30);
    Rect floor = new Rect(130,40,80,30);
    Rect ceiling = new Rect(220,40,80,30);
    ML ml;
    KL kl;


    public EditorState(Map map){
        this.map = map;
        currentMap = map.layout;
        this.ml = ML.getMouseListener();
        this.kl = KL.getKeyListener();
    }
    @Override
    public void update(double dt) {

        if (ml.isPressed(MouseEvent.BUTTON1) && ml.isMouseInsideRect(layout)){
            currentMap = map.layout;
        }
        if (ml.isPressed(MouseEvent.BUTTON1) && ml.isMouseInsideRect(floor)){
            currentMap = map.floor;
        }
        if (ml.isPressed(MouseEvent.BUTTON1) && ml.isMouseInsideRect(ceiling)){
            currentMap = map.ceiling;
        }

    }

    @Override
    public void draw(Graphics g) {

        drawButtons(g);
        drawMap(g);

    }

    private void drawMap(Graphics g) {
        int x,y,xo,yo;
        for (y = 0; y < currentMap.length; y++){
            for (x = 0; x < currentMap[y].length; x++){

                if (currentMap[y][x]>0){
                    g.setColor(Color.cyan);
                }else{
                    g.setColor(Color.black);
                }

                xo = x*16 + 100; yo = y*16 + 100;

                g.fillRect(xo,yo,16,16);

                g.setColor(Color.WHITE);
                g.drawRect(xo,yo,16,16);


            }
        }
    }

    private void drawButtons(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(layout.x,layout.y,layout.w,layout.h);
        g.setColor(Color.blue);
        g.fillRect(floor.x,floor.y,floor.w,floor.h);
        g.setColor(Color.red);
        g.fillRect(ceiling.x,ceiling.y,ceiling.w,ceiling.h);
    }
}
