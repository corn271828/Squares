package squares.api.level;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import squares.api.AudioManager;
import squares.api.Coordinate;
import squares.api.Clock;
import squares.api.AABB;
import squares.api.block.Block;
import squares.api.block.Entity;

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

    public abstract void setup(squares.Player pl);
    public abstract void setupMusic(AudioManager am, Clock c);
    public abstract void tickBlocks(squares.Player pl, Clock c, Coordinate drawingStart);
    public abstract void tickEntities(squares.Player pl, AABB offscreen, Clock c, java.awt.geom.Area clipholder);

    public abstract Iterable<Entity> getEntities();

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
