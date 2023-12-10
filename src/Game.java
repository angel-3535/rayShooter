import gfx.Renderable;
import gfx.Texture;
import gfx.Window;
import util.*;
import util.io.KL;
import util.io.ML;
import util.io.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static java.lang.Math.*;

public class Game implements Runnable, Renderable {

    gfx.Window window;
    int frameRate = 0;
    String displayInfo = "";
    private final float PI = (float) Math.PI, RAD = 0.0174533F;

    final KL kl = KL.getKeyListener();
    final ML ml = ML.getMouseListener();

    private Color f_Color = new Color(40, 23, 23,255);
    private Color c_Color = new Color(30, 55, 58,255);

    boolean editor = false;
    float sceneCD = 0f;
    float buttonCD = 0f;
    Map map = new Map();

    private State currentScene;


    public Game() throws IOException {
        window = Window.getWindow();


        Texture.loadTextures();
//        Sound.playMusic(Sound.M_HELL_ON_EARTH.getClip());
        currentScene = new GameState(map);
    }

    public void changeState(int newState) {
        if (sceneCD <= 0.f) {
            sceneCD = 1.f;
            switch (newState) {
                case 0:
                    currentScene = new GameState(map);
                    break;
                case 1:
                    currentScene = new EditorState(map);
                    break;
                default:
                    System.out.println("Unknown game state");
                    currentScene = null;
                    break;

            }

        }
    }


    public void inputs(){
        if (kl.isKeyDown(KeyEvent.VK_Y)){
            changeState(1);
        }
        if (kl.isKeyDown(KeyEvent.VK_U)){
            changeState(0);
        }

    }
    public void handleCD(double dt){
        sceneCD -= dt;
        buttonCD -=dt;
    }


    public void draw(Graphics g){

        currentScene.draw(g);

        g.setColor(Color.green);
        g.drawString(displayInfo,30,90);
    }

    public void update(double dt){
        handleCD(dt);
        inputs();

        currentScene.update(dt);


        frameRate = (int) (1/dt);
        displayInfo = String.format("%d FPS (%.4f)", frameRate,dt);
        window.render(this);
    }

    @Override
    public void run() {
        window.requestFocus();
        double lastFrameTime = 0.0;
        try{
            while(true){
                double time =  Time.getTime();
                double deltaTime = time - lastFrameTime;
                lastFrameTime = time;

                update(deltaTime);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        window.dispose();
    }
}
