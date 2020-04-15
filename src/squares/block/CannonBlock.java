/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.swing.ImageIcon;

import squares.Player;
import squares.api.ResourceLoader;
import squares.api.block.Block;
import squares.api.entity.Projectile;
import squares.api.block.FiringBlock;
import squares.api.block.TargetingBlock;

import static squares.api.RenderingConstants.STANDARD_ICON_WIDTH;
import static squares.api.RenderingConstants.CHARACTER_WIDTH;

/**
 *
 * @author piercelai
 */
public class CannonBlock extends BaseBlock implements FiringBlock, TargetingBlock {
    public int period; //In units of timestamp
    public int cannonballSpeed; //pixels per timestamp
    public int delay;
    public int targetX, targetY;
    public static final ImageIcon cannonBlockIcon = new ResourceLoader("sprites/block", "cannon").asImageIcon();

    public CannonBlock(String arg) {
        this(
            Integer.parseInt(arg.substring(0, 2)),
            Integer.parseInt(arg.substring(2, 4)),
            Integer.parseInt("0" + arg.substring(4)));
        if (this.delay == 0)
            delay = 1;
    }

    public CannonBlock(int p, int bs, int d) {
        super(cannonBlockIcon, null);
        period = p;
        cannonballSpeed = bs;
        delay = d;
    }

    public void setDelay(int d) {
        delay = d;
    }

    @Override
    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
    }

    @Override
    public boolean canStep() {
        return false;
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
        int xcenter = x + STANDARD_ICON_WIDTH / 2;
        int ycenter = y + STANDARD_ICON_WIDTH / 2;
        Cannonball cb = new Cannonball(xcenter, ycenter, cannonballSpeed, 
            Math.atan2(targetY + CHARACTER_WIDTH / 2 - ycenter, targetX + CHARACTER_WIDTH / 2 - xcenter));
        return cb;
    }

    @Override
    public void onLand(Player player) {
        System.out.println("How did you even land on a CannonBlock?");
    }

    public static class Cannonball extends Projectile {
        protected static final ImageIcon cannonballPic = new ImageIcon(new ResourceLoader("sprites", "cannonball").asImageIcon().getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
        public double angle; // angle in radians; think reflected unit circle
        public static final int CANNONBALL_RADIUS = 9;

        public Cannonball(int x, int y, int s, double ang) {
            super(x, y, s, cannonballPic);
            angle = ang;
        }

        @Override
        public void moveTick() {
            moveOffset((int) (getSpeed() * Math.cos(angle)), (int) (getSpeed() * Math.sin(angle)));
        }

        @Override
        public Area getCollision() {
            return new Area(new Rectangle(getX() + 1, getY() + 1, 14, 14));
        }

    }
}
