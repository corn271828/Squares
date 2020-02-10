package squares.api.block;

import java.awt.Graphics;
import java.awt.Component;
import java.awt.geom.Area;
import javax.swing.Icon;

import squares.api.Direction;

public abstract class Entity {
    private int xpos, ypos;

    public Entity(int x, int y) {
        xpos = x;
        ypos = y;
    }

    public abstract Area getClip();
    public abstract Area getCollision();

    public int getX()     { return xpos; }
    public int getY()     { return ypos; }

    public abstract void draw(Graphics g, Component c);

    public void moveTo(int x, int y) {
        xpos = x; ypos = y;
    }
    
    public void moveOffset(int xdelta, int ydelta) {
        xpos += xdelta; ypos += ydelta;
    }
}
