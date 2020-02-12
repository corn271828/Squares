package squares.api.entity;

import java.awt.Graphics;
import java.awt.Component;
import javax.swing.Icon;

public abstract class Projectile extends Entity {
    private int speed;
    protected Icon icon;

    public Projectile(int x, int y, int s, Icon i) {
        super(x, y);
        speed = s;
        icon = i;
    }

    public abstract void moveTick();

    public int getSpeed() { return speed; }

    @Override
    public void draw(Graphics g, Component c) {
        icon.paintIcon(c, g, getX(), getY());
    }
}
