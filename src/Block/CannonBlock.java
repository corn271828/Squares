/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Block;

import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.swing.ImageIcon;

/**
 *
 * @author piercelai
 */
public class CannonBlock extends Block {
    public int period; //In units of timestamp
    public int cannonballSpeed; //pixels per timestamp
    public int delay;
    public static final ImageIcon cannonBlockIcon = new ImageIcon("Pics/Aiming Cannon Base.png", "Cannon Block Image");

    public CannonBlock(int p, int bs, int d) {
        period = p;
        cannonballSpeed = bs;
        stepable = false;
        icon = cannonBlockIcon;
        delay = d;
    }

    @Override
    public void refreshIcon() {

    }

    @Override
    public void reset() {

    }

    public void setDelay(int d) {
        delay = d;
    }

    public static class Cannonball extends BlasterBlock.Blast {
        protected static final ImageIcon cannonballPic = new ImageIcon(new ImageIcon("Pics/Cannonball.png", "Cannonball pic").getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
        public double angle; // angle in radians; think reflected unit circle

        public Cannonball(int bs, double ang) {
            super(null, bs, cannonballPic);
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
