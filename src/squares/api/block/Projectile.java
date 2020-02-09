package squares.api.block;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.geom.Area;
import javax.swing.Icon;

import squares.api.Direction;

public abstract class Projectile {
    protected int xpos, ypos, speed;
    protected Icon icon;

    public Projectile(int x, int y, int s, Icon i) {
        xpos = x;
        ypos = y;
        speed = s;
        icon = i;
    }

    public abstract void moveTick();
    public abstract Area getClip();
    public abstract Area getCollision();

    public int getX()     { return xpos; }
    public int getY()     { return ypos; }
    public int getSpeed() { return speed; }

    public void draw(Graphics g, JPanel jp) {
        icon.paintIcon(jp, g, xpos, ypos);
    }

    public void moveTo(int x, int y) {
        xpos = x; ypos = y;
    }
}
