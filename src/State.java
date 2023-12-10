import java.awt.*;

public interface State {
    void update(double dt);
    void draw(Graphics g);
}