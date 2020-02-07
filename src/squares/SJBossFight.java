/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform; 
import java.awt.geom.Area;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import squares.api.ResourceLocator;
import squares.block.HighExplosion;
import squares.block.BlasterBlock;

/**
 *
 * @author piercelai
 */
public class SJBossFight extends Level.BossLevel {

    public static int FIRST_BOSS_TIME = 840;
    public static int SECOND_BOSS_TIME = 720;

    public static final char FLYING_BONE_CHAR = 'F';
    public static final char ARCING_BONE_CHAR = 'A';
    public static final char ROTATING_BONE_CHAR = 'R';
    public static final char TARGETING_BONE_CHAR = 'T'; // I swear this is an accident
    public static final char ORANGE_BONE_CHAR = 'O';
    public static final char BLUE_BONE_CHAR = 'B';

    public static final char LINE_EXPLODER_TESTER_CHAR = 'L';
    public static final char SERIOUS_EXPLODER_CHAR = 'S';

    HashMap<Integer, ArrayList<BlasterBlock.Blast>> timeToBlasts = new HashMap<>(); // Note: blast coordinates are not true coordinates; coordinates 
                                                                                          // adjusted on generate(); formula: 0 is center of 1st block, every 2 equals one spacing
    HashMap<Integer, ArrayList<Level.BossLevel.LineExploder>> timeToLines = new HashMap<>();

    public static final int wI_WIDTH = 300;
    public static final int wI_HEIGHT = 300;
    public static ImageIcon warningIcon = new ImageIcon(new ImageIcon("Pics/warning750.png").getImage().getScaledInstance(wI_WIDTH, wI_HEIGHT, java.awt.Image.SCALE_SMOOTH));

    public SJBossFight(String[][] in, String label, String... controls) {
        super(in, label, controls);
        this.endtime = FIRST_BOSS_TIME + SECOND_BOSS_TIME;
        generateHashMaps();
        levelHP = 10;
    }

    public SJBossFight(String[][] in, String label, int hp, String... controls) {
        super(in, label, hp, controls);
        this.endtime = FIRST_BOSS_TIME + SECOND_BOSS_TIME;
        generateHashMaps();
        levelHP = hp;
    }

    public SJBossFight(String[][] in, String label, int hp, ResourceLocator input){
        super(in, label, input);
        this.endtime = FIRST_BOSS_TIME + SECOND_BOSS_TIME;
        generateHashMaps();
        levelHP = hp;
    }     

    public SJBossFight(String[][] in, String label, int hp, ResourceLocator input, String code){
        super(in, label, code, input);
        this.endtime = FIRST_BOSS_TIME + SECOND_BOSS_TIME;
        generateHashMaps();
        levelHP = hp;
    } 

    @Override
    public void generateHashMaps() {
        for (int time = 0; time <= endtime; time++) {
            timeToBlasts.put(time, new ArrayList<>());
            timeToLines.put(time, new ArrayList<>());
        }
        for (String control : controls) {
            String[] splitted = control.trim().split(" ");
            int time = Integer.parseInt(splitted[0]);
            BlasterBlock.Blast hold;

            switch(splitted[1].charAt(0)) {
                case FLYING_BONE_CHAR:
                    hold = new FlyingBone(Double.parseDouble(splitted[4]), Integer.parseInt(splitted[5]));
                    hold.setCoords(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 6) {
                        ((FlyingBone) hold).setDimensions(Integer.parseInt(splitted[6]), Integer.parseInt(splitted[7]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case ARCING_BONE_CHAR:
                    hold = new ArcingBone(Double.parseDouble(splitted[4]), Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]));
                    hold.setCoords(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 9) {
                        ((ArcingBone) hold).setDimensions(Integer.parseInt(splitted[9]), Integer.parseInt(splitted[10]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case ROTATING_BONE_CHAR:
                    hold = new RotatingBone(Double.parseDouble(splitted[4]), Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]), Double.parseDouble(splitted[9]));
                    hold.setCoords(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 10) {
                        ((RotatingBone) hold).setDimensions(Integer.parseInt(splitted[10]), Integer.parseInt(splitted[11]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case TARGETING_BONE_CHAR:
                    hold = new ArcingBone(Double.parseDouble(splitted[4]) + 1000, Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]));
                    hold.setCoords(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 9) {
                        ((ArcingBone) hold).setDimensions(Integer.parseInt(splitted[9]), Integer.parseInt(splitted[10]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case ORANGE_BONE_CHAR:
                    hold = new ArcingBone(Double.parseDouble(splitted[4]), Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]), FlyingBone.orangeBony);
                    hold.setCoords(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 9) {
                        ((ArcingBone) hold).setDimensions(Integer.parseInt(splitted[9]), Integer.parseInt(splitted[10]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case BLUE_BONE_CHAR:
                    hold = new ArcingBone(Double.parseDouble(splitted[4]), Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]), FlyingBone.blueBony);
                    hold.setCoords(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 9) {
                        ((ArcingBone) hold).setDimensions(Integer.parseInt(splitted[9]), Integer.parseInt(splitted[10]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;

                case LINE_EXPLODER_TESTER_CHAR:
                    LineExploderTester letx = new LineExploderTester(time, 20, Double.parseDouble(splitted[4]), Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    timeToLines.get(time).add(letx);
                    break;
                case SERIOUS_EXPLODER_CHAR:
                    LineExploderTester lety = new LineExploderTester(time, 18, Double.parseDouble(splitted[4]), Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]), LineExploderTester.sansSrous);
                    timeToLines.get(time).add(lety);
            }

        }
    }


    @Override
    public ArrayList<BlasterBlock.Blast> generateBlasts(int timestamp, int xCoordinates, int yCoordinates, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS) {
        ArrayList<BlasterBlock.Blast> temp = timeToBlasts.get(timestamp);
        ArrayList<BlasterBlock.Blast> hold = new ArrayList<>();
        if (temp == null)
            return hold;
        for (BlasterBlock.Blast bla : temp) {
            BlasterBlock.Blast kin = bla.clone();
            kin.setCoords(startx + SPACING_BETWEEN_BLOCKS * bla.xcoord / 2 + STANDARD_ICON_WIDTH / 2, starty + SPACING_BETWEEN_BLOCKS * bla.ycoord / 2 + STANDARD_ICON_WIDTH / 2);
            if (kin instanceof ArcingBone && ((ArcingBone) kin).angle > 900) {
                ((ArcingBone) kin).angle = Math.atan2(xCoordinates - kin.xcoord, yCoordinates - kin.ycoord);
                ((ArcingBone) kin).xvelocity = kin.blastSpeed * Math.sin(((ArcingBone) kin).angle);
                ((ArcingBone) kin).yvelocity = kin.blastSpeed * Math.cos(((ArcingBone) kin).angle);
                ((ArcingBone) kin).angle *= -1;
                ((ArcingBone) kin).tx = new AffineTransform();
                ((ArcingBone) kin).tx.rotate(((ArcingBone) kin).angle);
            }
            hold.add(kin);
        }
        return hold;
    }

    @Override
    public ArrayList<LineExploder> generateLines(int timestamp, int xCoordinates, int yCoordinates, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS) {
        ArrayList<Level.BossLevel.LineExploder> temp = timeToLines.get(timestamp);
        ArrayList<Level.BossLevel.LineExploder> hold = new ArrayList<>();
        if (temp == null)
            return hold;
        for (Level.BossLevel.LineExploder lein : temp) {
            Level.BossLevel.LineExploder kin = lein.clone();
            kin.startxPosition = startx + SPACING_BETWEEN_BLOCKS * kin.startxPosition / 2 + STANDARD_ICON_WIDTH / 2;
            kin.startyPosition = starty + SPACING_BETWEEN_BLOCKS * kin.startyPosition / 2 + STANDARD_ICON_WIDTH / 2;
            kin.updateRegister();
            hold.add(kin);
        }
        return hold;
    }

    @Override
    public Area getBackgroundClip(int timestamp, Component c, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS) {
        if (timestamp > 160 && timestamp <= 170 || timestamp > 1460 && timestamp <= 1470)
            return new Area(new Rectangle(0, 0, c.getWidth(), c.getHeight()));
        return new Area();
    }

    @Override
    public Color getBackgroundColor(int timestamp) {
        if (timestamp > 160 && timestamp <= 170) {
            int holdisteij = 255 - (timestamp - 160) * 24;
            return new Color(holdisteij, holdisteij, holdisteij);
        }

        if (timestamp > 170 && timestamp <= 1500)
            return Color.black;

        if (timestamp > 1500 && timestamp <= 1510) {
            int holdisteij = (timestamp - 1500) * 24;
            return new Color(holdisteij, holdisteij, holdisteij);
        }

        return Color.white;
    }

    @Override
    public void drawBackground(Graphics g, int timestamp, Component c, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS) {

    }

    @Override
    public Area getForegroundClip(int timestamp, Component c, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS) {
        Area ret = new Area();
        if (timestamp >= 165 && timestamp < 177)
            ret.add(new Area(new Rectangle(startx + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    starty, warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp >= 500 && timestamp < 511)
            ret.add(new Area(new Rectangle(startx + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    starty, warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp >= 520 && timestamp < 531)
            ret.add(new Area(new Rectangle(startx + SPACING_BETWEEN_BLOCKS * 6 + STANDARD_ICON_WIDTH, 
                    starty + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconHeight() / 2,
                    warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp >= 540 && timestamp < 551)
            ret.add(new Area(new Rectangle(startx, 
                    starty + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconHeight() / 2,
                    warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp >= 560 && timestamp < 571)
            ret.add(new Area(new Rectangle(startx + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    starty + SPACING_BETWEEN_BLOCKS * 5 + STANDARD_ICON_WIDTH,
                    warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp <= FIRST_BOSS_TIME)
            ret.add(new Area(new Rectangle(c.getWidth() * (timestamp - 1) / FIRST_BOSS_TIME, 0, c.getWidth() * 2 / FIRST_BOSS_TIME, 10)));
        else
            ret.add(new Area(new Rectangle(c.getWidth() * (timestamp - 1 - FIRST_BOSS_TIME) / SECOND_BOSS_TIME, 10, c.getWidth() * 2 / FIRST_BOSS_TIME, 10)));

        return ret;
    }

    @Override
    public void drawForeground(Graphics g, int timestamp, Component c, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS) {
        Area ret = new Area();

        // Big bone warning signs
        if (timestamp >= 165 && timestamp < 176 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, startx + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    starty);
        }

        if (timestamp >= 500 && timestamp < 510 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, startx + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    starty);
        }

        if (timestamp >= 520 && timestamp < 530 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, startx + SPACING_BETWEEN_BLOCKS * 6 + STANDARD_ICON_WIDTH, 
                    starty + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconHeight() / 2);
        }

        if (timestamp >= 540 && timestamp < 550 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, startx, 
                    starty + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconHeight() / 2);
        }

        if (timestamp >= 560 && timestamp < 570 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, startx + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    starty + SPACING_BETWEEN_BLOCKS * 5 + STANDARD_ICON_WIDTH);
        }

        /*/ Words
        if (timestamp >= 155 && timestamp < 165) {
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 120));
            g.setColor(Color.black);
            g.drawString("Are you", startx + SPACING_BETWEEN_BLOCKS * 2 + (int) (Math.random() * 20), starty + SPACING_BETWEEN_BLOCKS * 3 + (int) (Math.random() * 20));
            g.drawString("ready...", startx + SPACING_BETWEEN_BLOCKS * 2 + (int) (Math.random() * 20), starty + SPACING_BETWEEN_BLOCKS * 5 + (int) (Math.random() * 20));
        }

        if (timestamp >= 165 && timestamp < 175) {
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 150));
            g.setColor(Color.white);
            g.drawString("TO HAVE A", startx + SPACING_BETWEEN_BLOCKS + (int) (Math.random() * 10), starty + SPACING_BETWEEN_BLOCKS * 3 + (int) (Math.random() * 10));
            g.drawString("BAD TIME?!", startx + SPACING_BETWEEN_BLOCKS +  (int) (Math.random() * 10), starty + SPACING_BETWEEN_BLOCKS * 5 + (int) (Math.random() * 10));
            g.setColor(new Color(200, 0, 0));
            g.drawString("TO HAVE A", startx + SPACING_BETWEEN_BLOCKS +  (int) (Math.random() * 30), starty + SPACING_BETWEEN_BLOCKS * 3 + (int) (Math.random() * 30));
            g.drawString("BAD TIME?!", startx + SPACING_BETWEEN_BLOCKS +  (int) (Math.random() * 30), starty + SPACING_BETWEEN_BLOCKS * 5 + (int) (Math.random() * 30));
        }*/

        if (timestamp >= 1100 && timestamp <= 1130){
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 150));
            g.setColor(Color.white);
            g.drawString("Get ready....", startx + SPACING_BETWEEN_BLOCKS + (int) (Math.random() * 10), starty + SPACING_BETWEEN_BLOCKS * 3 + (int) (Math.random() * 10));
        }

        if (timestamp >= 1130 && timestamp <= 1170){
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 150));
            g.setColor(Color.red);
            g.drawString("This is", startx + SPACING_BETWEEN_BLOCKS + (int) (Math.random() * 10), starty + SPACING_BETWEEN_BLOCKS * 3 + (int) (Math.random() * 10));
            g.drawString(" the end.", startx + SPACING_BETWEEN_BLOCKS + (int) (Math.random() * 10), starty + SPACING_BETWEEN_BLOCKS * 4 + (int) (Math.random() * 10));
        }

        if (timestamp >= 1170 && timestamp <= 1200){
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g.setColor(Color.white);
            g.drawString("Note: Bone colors don't mean anything;", startx + SPACING_BETWEEN_BLOCKS, starty + SPACING_BETWEEN_BLOCKS * 7);
            g.drawString("They just help you see which direction they're going.", startx + SPACING_BETWEEN_BLOCKS, starty + (int) (SPACING_BETWEEN_BLOCKS * 7.3));
        }

        // Progress bar
        g.setColor(Color.green);
        if (timestamp <= FIRST_BOSS_TIME) {
            g.fillRect(0, 0, c.getWidth() * timestamp / FIRST_BOSS_TIME, 10);
        } else {
            g.fillRect(0, 0, c.getWidth(), 10);
            g.fillRect(0, 10, c.getWidth() * (timestamp - FIRST_BOSS_TIME) / SECOND_BOSS_TIME, 10);

        }
    }


    public static class FlyingBone extends BlasterBlock.Blast {
        public static final Image bonypict = new ImageIcon("Pics/bonepic.png", "bonepic").getImage();
        public static final Image orangeBony = new ImageIcon("Pics/orangebone.png", "orangebone").getImage();
        public static final Image blueBony = new ImageIcon("Pics/bluebone.png", "bluebone").getImage();
        public int picwidth = 100;
        public int picheight = 30;
        public ImageIcon bonepic;
        public double angle;
        int xregister;
        int yregister;
        AffineTransform tx;
        Area oldArea;

        public FlyingBone(double d, int bs) {
            bonepic = new ImageIcon(bonypict.getScaledInstance(picwidth, picheight, java.awt.Image.SCALE_SMOOTH));
            icon = bonepic;
            blastSpeed = bs;
            angle = d;
            tx = new AffineTransform();
            tx.rotate(angle);
            oldArea = new Area();
        }

        public FlyingBone(double d, int bs, Image i) {
            bonepic = new ImageIcon(i.getScaledInstance(picwidth, picheight, java.awt.Image.SCALE_SMOOTH));
            icon = bonepic;
            blastSpeed = bs;
            angle = d;
            tx = new AffineTransform();
            tx.rotate(angle);
            oldArea = new Area();
        }

        public void setDimensions(int newWidth, int newHeight) {
            picwidth = newWidth;
            picheight = newHeight;
            bonepic = new ImageIcon(bonepic.getImage().getScaledInstance(picwidth, picheight, java.awt.Image.SCALE_SMOOTH));
        }

        @Override
        public void move() {
            xcoord += Math.cos(angle) * blastSpeed;
            ycoord += Math.sin(angle) * blastSpeed;
            updateRegister();
        }

        public void updateRegister() {
            xregister = (int) (xcoord * Math.cos(angle) + ycoord * Math.sin(angle));
            yregister = (int) (-xcoord * Math.sin(angle) + ycoord * Math.cos(angle));
        }

        @Override
        public void setCoords(int x, int y) {
            super.setCoords(x,y);
            updateRegister();
        }

        @Override
        public Area getOuchArea() {
            return new Area(tx.createTransformedShape(new Rectangle(xregister - picwidth / 2, yregister - picheight / 2, picwidth, picheight)));
        }

        @Override
        public Area getClip() {
            Area a = new Area(tx.createTransformedShape(new Rectangle(xregister - picwidth / 2 - 1, yregister - picheight / 2 - 1, picwidth + 2, picheight + 2)));
            Area hold = new Area(a);
            a.add(oldArea);
            oldArea = hold;

            return a;
        }

        @Override
        public void draw(Graphics g, JPanel jp) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(angle);
            bonepic.paintIcon(jp, g2d, xregister - picwidth / 2, yregister - picheight / 2);
            g2d.rotate(-angle);
        }

        @Override
        public FlyingBone clone() {
            FlyingBone fb = new FlyingBone(angle, blastSpeed, bonepic.getImage());
            fb.setDimensions(picwidth, picheight);
            return fb;
        }
    }


    public static class ArcingBone extends FlyingBone {
        double xvelocity;
        double yvelocity;
        double xaccel;
        double yaccel;

        public ArcingBone(double ang, double xvel, double yvel, double xa, double ya) {
            super(ang, (int) Math.round(Math.pow(xvel * xvel + yvel * yvel, .5)));
            xvelocity = xvel;
            yvelocity = yvel;
            xaccel = xa;
            yaccel = ya;
        }

        public ArcingBone(double ang, double xvel, double yvel, double xa, double ya, Image i) {
            super(ang, (int) Math.round(Math.pow(xvel * xvel + yvel * yvel, .5)), i);
            xvelocity = xvel;
            yvelocity = yvel;
            xaccel = xa;
            yaccel = ya;
        }

        @Override
        public void move() {
            xvelocity += xaccel;
            yvelocity += yaccel;
            xcoord += xvelocity;
            ycoord += yvelocity;
            updateRegister();
        }

        @Override
        public Area getClip() {
            Area a = new Area(tx.createTransformedShape(new Rectangle(xregister - picwidth / 2 - 1, yregister - picheight / 2 - 1, picwidth + 2, picheight + 2)));
            Area hold = new Area(a);
            a.add(oldArea);
            oldArea = hold;

            return a;
        }

        @Override
        public ArcingBone clone() {
            ArcingBone ab = new ArcingBone(angle, xvelocity, yvelocity, xaccel, yaccel, bonepic.getImage());
            ab.setDimensions(picwidth, picheight);
            return ab;
        }

    }

    public static class RotatingBone extends ArcingBone {
        double angularVel;

        public RotatingBone(double ang, double xvel, double yvel, double xa, double ya, double angvel) {
            super(ang, xvel, yvel, xa, ya);
            angularVel = angvel;
        }

        @Override
        public void move() {
            angle += angularVel;
            xvelocity += xaccel;
            yvelocity += yaccel;
            xcoord += xvelocity;
            ycoord += yvelocity;
            updateRegister();
            tx.rotate(angularVel);
        }

        public RotatingBone clone() {
            RotatingBone rb = new RotatingBone(angle, xvelocity, yvelocity, xaccel, yaccel, angularVel);
            rb.setDimensions(picwidth, picheight);
            return rb;
        }

    }

    public static class BoneExploder extends RotatingBone implements HighExplosion {

        public int explosionWidth;

        public BoneExploder(double ang, double xvel, double yvel, double xa, double ya, double angvel, int wid) {
            super(ang, xvel, yvel, xa, ya, angvel);
            explosionWidth = wid;
        }


        @Override
        public LineExploder getLineExplosion(int startime, double ang, int starx, int stary) {
            return new SimpleLineExplosion(startime, ang, starx, stary, explosionWidth);
        }

        public static class SimpleLineExplosion extends Level.BossLevel.LineExploder{

            public static final int[] opacities = new int[] {100, 200, 255, 255, 200, 0, 0};
            public int width;

            public SimpleLineExplosion(int stt, double ang, int sxp, int syp, int w) {
                super(stt, 5, ang, sxp, syp);
                width = w;
            }

            @Override
            public void drawXPlosion(Component c, Graphics g, int timestamp) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.rotate(angle);

                int opacity = opacities[timestamp - starttime];
                g2d.setColor(new Color(0, 100, 0, opacity));
                g2d.fillRect(xregister - width / 2, yregister, width, 1000);
                g2d.setColor(new Color(0, 200, 0, opacity));
                g2d.fillRect(xregister - 2 * width / 5, yregister, width - width / 5, 1000);
                g2d.setColor(new Color(0, 255, 0, opacity));
                g2d.fillRect(xregister - width / 4, yregister,  width - width / 2, 1000);

                g2d.rotate(-angle);
            }

            @Override
            public Area xplosionOuchArea(int timestamp) {
                return new Area(tx.createTransformedShape(new Rectangle(xregister - width / 2, yregister, width, 1000)));
            }

            @Override
            public Area xplosionClipArea(int timestamp) {
                return new Area(tx.createTransformedShape(new Rectangle(xregister - width / 2, yregister, width, 1000)));
            }
        }

    }


}

