/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Block;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import squares.Level;

/**
 *
 * @author lai_889937
 */
public abstract class Block {
    protected Icon icon;
    protected String label;
    protected boolean stepable; // Represents whether or not one can step on this type of block
    public abstract void refreshIcon();
    public abstract void reset();
    
    public String toString() {
        return label;
    }
    
    public static enum Direction {
        UP, DOWN, RIGHT, LEFT;
    }
    
    public Icon getIcon() {
        return icon;
    }
    
    public boolean getStepable() {
        return stepable;
    }
    
    public static class NormalBlock extends Block {
        public static final ImageIcon normalBlockIcon = new ImageIcon("Pics/Normal Block.png", "Normal Block Image");
        
        public NormalBlock() {
            stepable = true;
            label = "Normal Block";
            icon = normalBlockIcon;
        }

        @Override
        public void refreshIcon() {
            
        }

        @Override
        public void reset() {
            
        }
        
    }
    
    public static class ButtonBlock extends Block {
        boolean isPressed = false;
        //public static final ImageIcon notPressedIcon = new ImageIcon("Pics/Not Pressed Button Block.png", "Not Pressed Button Block");
        //public static final ImageIcon pressedIcon = new ImageIcon("Pics/Pressed Button Block.png", "Pressed Button Block");
        
        public ButtonBlock() {
            stepable = true;
            label = "Button Block";
        }

        @Override
        public void refreshIcon() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    public static class LauncherBlock extends Block {

        protected static final ImageIcon launcherBlockRight = new ImageIcon("Pics/Launcher Block Right.png", "Launcher Block Image");
        protected static final ImageIcon launcherBlockLeft = new ImageIcon("Pics/Launcher Block Left.png", "Launcher Block Image");
        protected static final ImageIcon launcherBlockDown = new ImageIcon("Pics/Launcher Block Down.png", "Launcher Block Image");
        protected static final ImageIcon launcherBlockUp = new ImageIcon("Pics/Launcher Block Up.png", "Launcher Block Image");
        protected Direction direction;
        
        public LauncherBlock(Direction direction) {
            this.direction = direction;
            stepable = true;
            label = "Launcher Block";
            switch (direction) {
                case UP: icon = launcherBlockUp; break;
                case DOWN: icon = launcherBlockDown; break;
                case LEFT: icon = launcherBlockLeft; break;
                case RIGHT: icon = launcherBlockRight; break;
            }
        }

        @Override
        public void refreshIcon() {
            
        }

        @Override
        public void reset() {
            
        }
        
        public Direction getDirection() {
            return direction;
        }
    }
    
    public static class EndingBlock extends Block {
        public static final ImageIcon endingBlockIcon = new ImageIcon("Pics/Ending Block.png", "End Block Image");;
        
        public EndingBlock() {
            stepable = true;
            label = "End Block";
            icon = endingBlockIcon;
        }

        @Override
        public void refreshIcon() {
            
        }

        @Override
        public void reset() {
            
        }
        
    }
    
    public static class BlasterBlock extends Block {
        public Direction direction;
        public int period; //In units of timestamp
        public int blastSpeed; //pixels per timestamp
        public boolean primed;
        public int delay;
        
        public BlasterBlock(Direction d, int p, int bs) {
            direction = d;
            period = p;
            blastSpeed = bs;
            stepable = false;
            icon = new ImageIcon("Pics/Blaster Block " + direction + ".png", "Blaster Block Image");
            primed = false;
            delay = 1;
        }
        
        public BlasterBlock(Direction d, int p, int bs, int delay) {
            direction = d;
            period = p;
            blastSpeed = bs;
            stepable = false;
            icon = new ImageIcon("Pics/Blaster Block " + direction + ".png", "Blaster Block Image");
            primed = false;
            this.delay = delay;
        }
        
        @Override
        public void reset() {
            
        }

        @Override
        public void refreshIcon() {
            
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
            public Blast clone() throws CloneNotSupportedException {
                return new Blast(this);
            }
            
        }
    }
    
    public static class CannonBlock extends Block {
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
        
        public static class Cannonball extends Block.BlasterBlock.Blast {
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
    
    public static interface HighExplosion {
        public Level.BossLevel.LineExploder getLineExplosion(int startime, double ang, int starx, int stary);
    }
}

