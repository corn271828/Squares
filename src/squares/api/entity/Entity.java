package squares.api.entity;

import java.awt.Graphics;
import java.awt.Component;
import java.awt.geom.Area;

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
    public abstract void moveTick();

    public void moveTo(int x, int y) {
        xpos = x; ypos = y;
    }
    
    public void moveOffset(int xdelta, int ydelta) {
        xpos += xdelta; ypos += ydelta;
    }
}
