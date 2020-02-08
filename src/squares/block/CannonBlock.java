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
import squares.api.block.FiringBlock;
import squares.api.block.TargetingBlock;

import static squares.api.RenderingConstants.STANDARD_ICON_WIDTH;
import static squares.api.RenderingConstants.CHARACTER_WIDTH;

/**
 *
 * @author piercelai
 */
public class CannonBlock extends Block implements FiringBlock, TargetingBlock {
    public int period; //In units of timestamp
    public int cannonballSpeed; //pixels per timestamp
    public int delay;
    public int targetX, targetY;
    public static final ImageIcon cannonBlockIcon = new ImageIcon("Pics/Aiming Cannon Base.png", "Cannon Block Image");

    public CannonBlock(int p, int bs, int d) {
        super(cannonBlockIcon, null, false);
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
    public int getPeriod() {
        return period;
    }
   
    @Override
    public int getPhase() {
        return delay;
    }

    @Override
    public BlasterBlock.Blast createAtCoords(int x, int y) {
        int xcenter = x + STANDARD_ICON_WIDTH / 2;
        int ycenter = y + STANDARD_ICON_WIDTH / 2;
        Cannonball cb = new Cannonball(cannonballSpeed, 
            Math.atan2(targetY + CHARACTER_WIDTH / 2 - ycenter, targetX + CHARACTER_WIDTH / 2 - xcenter));
        cb.setCoords(xcenter, ycenter);
        return cb;
    }

    @Override
    public void onLand(Player player) {
        System.out.println("How did you even land on a CannonBlock?");
    }

    public static class Cannonball extends BlasterBlock.Blast {
        protected static final ImageIcon cannonballPic = new ImageIcon(new ImageIcon("Pics/Cannonball.png", "Cannonball pic").getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
        public double angle; // angle in radians; think reflected unit circle

        public Cannonball(int bs, double ang) {
            direction = null;
            blastSpeed = bs;
            icon = cannonballPic;
            angle = ang;
        }

        @Override
        public void move() {
            xcoord += blastSpeed * Math.cos(angle);
            ycoord += blastSpeed * Math.sin(angle);
        }

        @Override
        public Area getOuchArea() {
            return new Area(new Rectangle(xcoord, ycoord, 20, 20));
        }

        @Override
        public Area getClip() {
            Area a = new Area(new Rectangle(xcoord, ycoord, 20, 20));
            a.add(new Area(new Rectangle((int) (xcoord - blastSpeed * Math.cos(angle)), 
                    (int) (ycoord - blastSpeed * Math.sin(angle)), 20, 20)));
            return a;
        }

    }
}
