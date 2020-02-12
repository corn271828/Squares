/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import squares.Player;

import squares.api.Direction;
import squares.api.ResourceLoader;
import squares.api.block.Block;
import squares.api.block.DirectedBlock;
import squares.api.block.FiringBlock;
import squares.api.block.Projectile;

/**
 *
 * @author piercelai
 */
public class BlasterBlock extends Block implements DirectedBlock, FiringBlock {
    public Direction direction;
    public int period; //In units of timestamp
    public int blastSpeed; //pixels per timestamp
    public boolean primed;
    public int delay;

    private static final ImageIcon[] blasterBlocks = new ImageIcon[Direction.values().length];

    public BlasterBlock(Direction d, String arg) {
        this(d,
            Integer.parseInt(arg.substring(0, 2)),
            Integer.parseInt(arg.substring(2, 4)),
            Integer.parseInt("0" + arg.substring(4)));
        if (this.delay == 0)
            delay = 1;
    }

    public BlasterBlock(Direction d, int p, int bs) {
        this(d, p, bs, 1);
        if (this.delay == 0)
            delay = 1;
    }

    public BlasterBlock(Direction d, int p, int bs, int delay) {
        super(getIconByDirection(d), "Blaster Block", false);
        period = p;
        blastSpeed = bs;
        primed = false;
        this.delay = delay;
        direction = d;
    }

    private static ImageIcon getIconByDirection(Direction d) {
        int i = d.ordinal();
        if(blasterBlocks[i] == null)
            blasterBlocks[i] = new ResourceLoader("sprites", String.format("Blaster Block %s", d.name)).asImageIcon();
        return blasterBlocks[i];
    }

    public void prime() {
        if (!primed)
            primed = true;
    }

    public void setDelay(int d) {
        delay = d;
    }

    @Override
    public int getPeriod() {
        return period;
    }
   
    @Override
    public int getPhase() {
        return delay;
    }

    @Override
    public Projectile createAtCoords(int x, int y) {
            Projectile b = new Blast(-1, -1, direction, blastSpeed);

            switch (direction) {
                case UP:
                    b.moveTo(x + 32, y);
                    break;
                case LEFT:
                    b.moveTo(x, y + 32);
                    break;
                case RIGHT:
                    b.moveTo(x + 30, y + 32);
                    break;
                case DOWN:
                    b.moveTo(x + 32, y + 30);
                    break;
            }
            primed = false;
            return b;
    }

    @Override
    public void onLand(Player player) {
        System.out.println("Bruh, dunno how you landed on a BlasterBlock, but here you are.");
    }

    public static class Blast extends Projectile {
        private static Icon[] blastIcons = new Icon[Direction.values().length];

        public Direction direction;

        public Blast(int x, int y, Direction d, int bs) {
            this(x, y, d, bs, getIconByDirection(d));
        }

        public Blast(int x, int y, Direction d, int s, Icon imgicn) {
            super(x, y, s, imgicn);
            direction = d;
        }

        private static Icon getIconByDirection(Direction d) {
            int i = d.ordinal();
            if(blastIcons[i] == null)
                blastIcons[i] = new ResourceLoader("sprites", String.format("Slow Blast %s", d.name)).asImageIcon();
            return blastIcons[i];
        }

        @Override
        public void moveTick() {
            moveOffset(direction.x * getSpeed(),  direction.y * getSpeed());
        }

        @Override
        public Area getCollision() {
            switch(direction) {
                case UP:
                    return new Area(new Rectangle(getX(), getY() + 8, 17, 30));
                case LEFT:
                    return new Area(new Rectangle(getX() + 8, getY(), 30, 17));
                case DOWN:
                    return new Area(new Rectangle(getX(), getY(), 17, 30));
                case RIGHT:
                    return new Area(new Rectangle(getX(), getY(), 30, 17));
            }
            throw new IllegalStateException();
        }

        @Override
        public Area getClip() {
            switch (direction) {
            case LEFT:
                return new Area(new Rectangle(getX() - 1, getY() - 1, 43 + getSpeed(), 19));
            case RIGHT:
                return new Area(new Rectangle(getX() - 1 - getSpeed(), getY() - 1, 43 + getSpeed(), 19));
            case DOWN:
                return new Area(new Rectangle(getX() - 1, getY() - 1 - getSpeed(), 19, 43 + getSpeed()));
            case UP:
                return new Area(new Rectangle(getX() - 1, getY() - 1, 19, 43 + getSpeed()));
            }
            throw new IllegalStateException();
        }
    }
    @Override
    public Direction getDirection() {
        return direction;
    }
}
