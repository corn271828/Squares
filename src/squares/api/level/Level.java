package squares.api.level;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import squares.api.Coordinate;
import squares.api.block.Block;

public abstract class Level {
    private int deaths = 0;
    public final String label, code;
    public Level(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public abstract Block blockAt(int x, int y);
    public abstract int xSize();
    public abstract int ySize();

    public void drawBackground(Graphics g, int timestamp, Component c, Coordinate start) {}
    public void drawForeground(Graphics g, int timestamp, Component c, Coordinate start) {}

    public abstract Coordinate getStartPos();
    public abstract Coordinate getEndPos();

    public int getDeaths() {
        return deaths;
    }
    public void addDeath() {
        deaths++;
    }
    public Color getBackgroundColor(int ts) {
        return Color.WHITE;
    }
}
