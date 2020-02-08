/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import squares.Player;


/**
 *
 * @author piercelai
 */
public class BlasterBlock extends Block implements DirectedBlock {
    public Direction direction;
    public int period; //In units of timestamp
    public int blastSpeed; //pixels per timestamp
    public boolean primed;
    public int delay;

    private static final ImageIcon[] blasterBlocks = new ImageIcon[Direction.values().length];

    public BlasterBlock(Direction d, int p, int bs) {
        this(d, p, bs, 1);
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
            blasterBlocks[i] = new ImageIcon(String.format("Pics/Blaster Block %s.png", d.name), "Blaster Block Image");
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
    public void onLand(Player player) {
        System.out.println("Bruh, dunno how you landed on a BlasterBlock, but here you are.");
    }

    public static class Blast {
        public Direction direction;
        public int blastSpeed;
        public Icon icon;
        public int xcoord;
        public int ycoord;

        public Blast() {
            direction = null;
            blastSpeed = 0;
            icon = null;
        }

        public Blast(Direction d, int bs) {
            direction = d;
            blastSpeed = bs;
            icon = new ImageIcon("Pics/Slow Blast " + direction + ".png", "Blaster Block Image");
        }

        public Blast(Direction d, int bs, ImageIcon imgicn) {
            direction = d;
            blastSpeed = bs;
            icon = imgicn;
        }

        public Blast(Blast bla) {
            this.direction = bla.direction;
            this.blastSpeed = bla.blastSpeed;
            this.icon = bla.icon;
        }

        public void setCoords(int x, int y) {
            xcoord = x;
            ycoord = y;
        }

        public void move() {
            switch(direction) {
                case UP:
                    ycoord -= blastSpeed;
                    break;
                case LEFT:
                    xcoord -= blastSpeed;
                    break;       
                case DOWN:
                    ycoord += blastSpeed;
                    break;
                case RIGHT:
                    xcoord += blastSpeed;
                    break;
            }
        }

        public Area getOuchArea() {
            Area a = new Area();
            switch(direction) {
                case UP:
                    a = new Area(new Rectangle(xcoord, ycoord + 8, 17, 30));
                    break;
                case LEFT:
                    a = new Area(new Rectangle(xcoord + 8, ycoord, 30, 17));
                    break;       
                case DOWN:
                    a = new Area(new Rectangle(xcoord, ycoord, 17, 30));
                    break;
                case RIGHT:
                    a = new Area(new Rectangle(xcoord, ycoord, 30, 17));
                    break;
            }
            return a;
        }

        public Area getClip() {
            switch (direction) {
            case LEFT:
                return new Area(new Rectangle(xcoord - 1, ycoord - 1, 43 + blastSpeed, 19));
            case RIGHT:
                return new Area(new Rectangle(xcoord - 1 - blastSpeed, ycoord - 1, 43 + blastSpeed, 19));
            case DOWN:
                return new Area(new Rectangle(xcoord - 1, ycoord - 1 - blastSpeed, 19, 43 + blastSpeed));
            case UP:
                return new Area(new Rectangle(xcoord - 1, ycoord - 1, 19, 43 + blastSpeed));
            }

            return new Area();
        }

        public void draw(Graphics g, JPanel jp) {
            icon.paintIcon(jp, g, xcoord, ycoord);
        }

        @Override
        public Blast clone() {
            return new Blast(this);
        }

    }
    @Override
    public Direction getDirection() {
        return direction;
    }
}
