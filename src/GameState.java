import Entity.Player;
import util.Map;

import java.awt.*;

public class GameState implements State {

    Player player;
    Map map;

    public GameState(Map map){
        this.map = map;
        player = new Player(0, map);

        player.transform.setPosition(300f,300f);

        player.moveSpeed = map.getTileSize() * 6 ;

    }


    @Override
    public void update(double dt) {
        player.update(dt);
    }

    @Override
    public void draw(Graphics g) {
        player.draw(g);

    }
}
