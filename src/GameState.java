import Entity.Player;
import util.Map;
import util.RMath;

import java.awt.*;

public class GameState implements State {

    Player player;
    Map currentMap;
    int currentMapIndex = 0;
    Map[] maps;

    public GameState(Map[] maps){
        this.maps = maps;
        currentMap = maps[currentMapIndex];
        player = new Player(0, currentMap);

        player.moveSpeed = currentMap.getTileSize() * 6 ;
    }

    @Override
    public void update(double dt) {
        int x = (int) currentMap.getMapPos(player.transform.getPosition()).getX();
        int y = (int) currentMap.getMapPos(player.transform.getPosition()).getY();

        if (player.nextState){
            currentMapIndex ++;

            currentMapIndex = RMath.clamp(currentMapIndex, 0, maps.length-1);
            currentMap = maps[currentMapIndex];

            player.map = currentMap;
            player.nextState = false;

        }

        player.update(dt);
    }

    @Override
    public void draw(Graphics g) {
        player.draw(g);

    }
}
