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

import squares.api.Direction;
import squares.api.DirectedBlock;

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

    protected static final ImageIcon blasterBlockRight = new ImageIcon("Pics/Blaster Block Right.png", "Blaster Block Image");
    protected static final ImageIcon blasterBlockLeft = new ImageIcon("Pics/Blaster Block Left.png", "Blaster Block Image");
    protected static final ImageIcon blasterBlockDown = new ImageIcon("Pics/Blaster Block Down.png", "Blaster Block Image");
    protected static final ImageIcon blasterBlockUp = new ImageIcon("Pics/Blaster Block Up.png", "Blaster Block Image");

    public BlasterBlock(Direction d, int p, int bs) {
        this(d, p, bs, 1);
    }

    public BlasterBlock(Direction d, int p, int bs, int delay) {
        super(getIconByDirection(d), null, false);
        period = p;
        blastSpeed = bs;
        primed = false;
        this.delay = delay;
    }

    private static ImageIcon getIconByDirection(Direction d) {
        switch (d) {
            case UP:    return blasterBlockUp;
            case DOWN:  return blasterBlockDown;
            case LEFT:  return blasterBlockLeft;
            case RIGHT: return blasterBlockRight;
        }
        return null;
    }

    public void prime() {
        if (!primed)
            primed = true;
    }

    public void setDelay(int d) {
        delay = d;
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