package squares.api.block;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.geom.Area;
import javax.swing.Icon;

import squares.api.Direction;

public abstract class Projectile {
    protected Direction direction;
    protected int xpos, ypos;
    protected int velocity;
    protected Icon render;

    public Projectile(Direction dir, int x, int y, int v, Icon i) {
        direction = dir;
        xpos = x;
        ypos = y;
        velocity = v;
        render = i;
    }
    public abstract Area getClip();
    public abstract Area getCollision();

    public void draw(Graphics g, JPanel jp) {
        render.paintIcon(jp, g, xpos, ypos);
    }

    public void moveTick() {
        xpos += velocity * direction.x;
        ypos += velocity * direction.y;
    }

    public void moveTo(int x, int y) {
        xpos = x; ypos = y;
    }
}
