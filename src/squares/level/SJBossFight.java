/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.level;

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
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import squares.api.ResourceLoader;
import squares.api.block.Projectile;
import squares.api.block.BlockFactory;
import squares.api.level.BossLevel;
import squares.api.Coordinate;
import squares.block.HighExplosion;

import static squares.api.RenderingConstants.SPACING_BETWEEN_BLOCKS;
import static squares.api.RenderingConstants.STANDARD_ICON_WIDTH;

/**
 *
 * @author piercelai
 */
public class SJBossFight extends BaseLevel implements BossLevel {

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

    //Map<Integer, List<FlyingBone>> timeToBlasts = new HashMap<>(); // Note: blast coordinates are not true coordinates; coordinates 
                                                                                 // adjusted on generate(); formula: 0 is center of 1st block, every 2 equals one spacing
    //Map<Integer, List<BaseLevel.LineExploder>> timeToLines = new HashMap<>();
    
    List<FlyingBone> allTheBlasts = new java.util.LinkedList<>();
    int[] timeToBlastIndex;
    List<BaseLevel.LineExploder> allTheLines = new java.util.LinkedList<>();
    int[] timeToLinesIndex;

    public static final int wI_WIDTH = 300;
    public static final int wI_HEIGHT = 300;
    public static ImageIcon warningIcon = new ImageIcon(new ImageIcon("Pics/warning750.png").getImage().getScaledInstance(wI_WIDTH, wI_HEIGHT, java.awt.Image.SCALE_SMOOTH));

    int endtime, levelHP;

    public SJBossFight(String[][] in, String[] args, BlockFactory bf) {
        this(in, args[0], Integer.parseInt(args[1]), new ResourceLoader("bossdata", args[2]), args[3], bf);
    }

    public SJBossFight(String[][] in, String label, int hp, ResourceLoader input, String code, BlockFactory bf) {
        super(in, label, code, bf);
        this.endtime = FIRST_BOSS_TIME + SECOND_BOSS_TIME;
        generateMaps(parseControls(input));
        levelHP = hp;
    }

    private void generateMaps(String[] controls) {
        Map<Integer, List<FlyingBone>> timeToBlasts = new HashMap<>();
        Map<Integer, List<BaseLevel.LineExploder>> timeToLines = new HashMap<>();
        for (int time = 0; time <= endtime + 1; time++) {
            timeToBlasts.put(time, new ArrayList<>());
            timeToLines.put(time, new ArrayList<>());
        }
        for (String control: controls) {
            String[] splitted = control.trim().split(" ");
            int time = Integer.parseInt(splitted[0]);
            FlyingBone hold;

            switch(splitted[1].charAt(0)) {
                case FLYING_BONE_CHAR:
                    hold = new FlyingBone(Double.parseDouble(splitted[4]), Integer.parseInt(splitted[5]));
                    hold.moveTo(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 6) {
                        hold.setDimensions(Integer.parseInt(splitted[6]), Integer.parseInt(splitted[7]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case ARCING_BONE_CHAR:
                    hold = new ArcingBone(Double.parseDouble(splitted[4]), Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]));
                    hold.moveTo(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 9) {
                        hold.setDimensions(Integer.parseInt(splitted[9]), Integer.parseInt(splitted[10]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case ROTATING_BONE_CHAR:
                    hold = new RotatingBone(Double.parseDouble(splitted[4]), Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]), Double.parseDouble(splitted[9]));
                    hold.moveTo(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 10) {
                        hold.setDimensions(Integer.parseInt(splitted[10]), Integer.parseInt(splitted[11]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case TARGETING_BONE_CHAR:
                    hold = new ArcingBone(Double.parseDouble(splitted[4]) + 1000, Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]));
                    hold.moveTo(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 9) {
                        hold.setDimensions(Integer.parseInt(splitted[9]), Integer.parseInt(splitted[10]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case ORANGE_BONE_CHAR:
                    hold = new ArcingBone(Double.parseDouble(splitted[4]), Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]), FlyingBone.orangeBony);
                    hold.moveTo(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 9) {
                        hold.setDimensions(Integer.parseInt(splitted[9]), Integer.parseInt(splitted[10]));
                    }
                    timeToBlasts.get(time).add(hold);
                    break;
                case BLUE_BONE_CHAR:
                    hold = new ArcingBone(Double.parseDouble(splitted[4]), Double.parseDouble(splitted[5]), Double.parseDouble(splitted[6]),  Double.parseDouble(splitted[7]),  Double.parseDouble(splitted[8]), FlyingBone.blueBony);
                    hold.moveTo(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
                    if (splitted.length > 9) {
                        hold.setDimensions(Integer.parseInt(splitted[9]), Integer.parseInt(splitted[10]));
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
        
        int currentBlastCount = 0;
        timeToBlastIndex = new int[endtime + 1];
        int currentLineCount = 0;
        timeToLinesIndex = new int[endtime + 1];
        for (int i = 0; i <= endtime; i++) {
            allTheBlasts.addAll(timeToBlasts.get(i));
            currentBlastCount += timeToBlasts.get(i).size();
            timeToBlastIndex[i] = currentBlastCount;
            allTheLines.addAll(timeToLines.get(i));
            currentLineCount += timeToLines.get(i).size();
            timeToLinesIndex[i] = currentLineCount;
        }
    }


    @Override
    public List<? extends Projectile> generateBlasts(int timestamp, Coordinate render, Coordinate start) {
        if (timestamp > endtime || timestamp <= 0)
            return new ArrayList<>();
        List<FlyingBone> temp = allTheBlasts.subList(timeToBlastIndex[timestamp - 1], timeToBlastIndex[timestamp]);
        List<FlyingBone> hold = new ArrayList<>();
        if (temp == null)
            return hold;
        for (FlyingBone bla : temp) {
            FlyingBone kin = bla.clone();
            kin.moveTo(start.x + SPACING_BETWEEN_BLOCKS * bla.getX() / 2 + STANDARD_ICON_WIDTH / 2, start.y + SPACING_BETWEEN_BLOCKS * bla.getY() / 2 + STANDARD_ICON_WIDTH / 2);
            if (kin instanceof ArcingBone && ((ArcingBone) kin).angle > 900) {
                ArcingBone ab = (ArcingBone) kin;
                ab.angle = Math.atan2(render.x - kin.getX(), render.y - kin.getY());
                ab.xvelocity = kin.getSpeed() * Math.sin(ab.angle);
                ab.yvelocity = kin.getSpeed() * Math.cos(ab.angle);
                ab.angle *= -1;
                ab.tx = new AffineTransform();
                ab.tx.rotate(ab.angle);
            }
            hold.add(kin);
        }
        return hold;
    }

    @Override
    public List<? extends LineExploder> generateLines(int timestamp, Coordinate render, Coordinate start) {
        if (timestamp > endtime || timestamp <= 0)
            return new ArrayList<>();
        List<BaseLevel.LineExploder> temp = allTheLines.subList(timeToLinesIndex[timestamp - 1], timeToLinesIndex[timestamp]);
        List<BaseLevel.LineExploder> hold = new ArrayList<>();
        if (temp == null)
            return hold;
        for (BaseLevel.LineExploder lein : temp) {
            BaseLevel.LineExploder kin = lein.clone();
            kin.startPos.x = start.x + SPACING_BETWEEN_BLOCKS * kin.startPos.x / 2 + STANDARD_ICON_WIDTH / 2;
            kin.startPos.y = start.y + SPACING_BETWEEN_BLOCKS * kin.startPos.y / 2 + STANDARD_ICON_WIDTH / 2;
            kin.updateRegister();
            hold.add(kin);
        }
        return hold;
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
    public void drawBackground(Graphics g, int timestamp, Component c, Coordinate start) {

    }

    public Area getForegroundClip(int timestamp, Component c, Coordinate start) {
        Area ret = new Area();
        if (timestamp >= 165 && timestamp < 177)
            ret.add(new Area(new Rectangle(start.x + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    start.y, warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp >= 500 && timestamp < 511)
            ret.add(new Area(new Rectangle(start.x + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    start.y, warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp >= 520 && timestamp < 531)
            ret.add(new Area(new Rectangle(start.x + SPACING_BETWEEN_BLOCKS * 6 + STANDARD_ICON_WIDTH, 
                    start.y + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconHeight() / 2,
                    warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp >= 540 && timestamp < 551)
            ret.add(new Area(new Rectangle(start.x, 
                    start.y + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconHeight() / 2,
                    warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp >= 560 && timestamp < 571)
            ret.add(new Area(new Rectangle(start.x + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    start.y + SPACING_BETWEEN_BLOCKS * 5 + STANDARD_ICON_WIDTH,
                    warningIcon.getIconWidth(), warningIcon.getIconHeight())));

        if (timestamp <= FIRST_BOSS_TIME)
            ret.add(new Area(new Rectangle(c.getWidth() * (timestamp - 1) / FIRST_BOSS_TIME, 0, c.getWidth() * 2 / FIRST_BOSS_TIME, 10)));
        else
            ret.add(new Area(new Rectangle(c.getWidth() * (timestamp - 1 - FIRST_BOSS_TIME) / SECOND_BOSS_TIME, 10, c.getWidth() * 2 / FIRST_BOSS_TIME, 10)));

        return ret;
    }

    @Override
    public void drawForeground(Graphics g, int timestamp, Component c, Coordinate start) {
        Area ret = new Area();

        // Big bone warning signs
        if (timestamp >= 165 && timestamp < 176 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, start.x + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    start.y);
        }

        if (timestamp >= 500 && timestamp < 510 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, start.x + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    start.y);
        }

        if (timestamp >= 520 && timestamp < 530 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, start.x + SPACING_BETWEEN_BLOCKS * 6 + STANDARD_ICON_WIDTH, 
                    start.y + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconHeight() / 2);
        }

        if (timestamp >= 540 && timestamp < 550 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, start.x, 
                    start.y + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconHeight() / 2);
        }

        if (timestamp >= 560 && timestamp < 570 && timestamp % 2 == 1) {
            warningIcon.paintIcon(c, g, start.x + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH / 2 - warningIcon.getIconWidth() / 2, 
                    start.y + SPACING_BETWEEN_BLOCKS * 5 + STANDARD_ICON_WIDTH);
        }

        if (timestamp >= 1100 && timestamp <= 1130){
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 150));
            g.setColor(Color.white);
            g.drawString("Get ready....", start.x + SPACING_BETWEEN_BLOCKS + (int) (Math.random() * 10), start.y + SPACING_BETWEEN_BLOCKS * 3 + (int) (Math.random() * 10));
        }

        if (timestamp >= 1130 && timestamp <= 1170){
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 150));
            g.setColor(Color.red);
            g.drawString("This is", start.x + SPACING_BETWEEN_BLOCKS + (int) (Math.random() * 10), start.y + SPACING_BETWEEN_BLOCKS * 3 + (int) (Math.random() * 10));
            g.drawString(" the end.", start.x + SPACING_BETWEEN_BLOCKS + (int) (Math.random() * 10), start.y + SPACING_BETWEEN_BLOCKS * 4 + (int) (Math.random() * 10));
        }

        if (timestamp >= 1170 && timestamp <= 1200){
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g.setColor(Color.white);
            g.drawString("Note: Bone colors don't mean anything;", start.x + SPACING_BETWEEN_BLOCKS, start.y + SPACING_BETWEEN_BLOCKS * 7);
            g.drawString("They just help you see which direction they're going.", start.x + SPACING_BETWEEN_BLOCKS, start.y + (int) (SPACING_BETWEEN_BLOCKS * 7.3));
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

    @Override
    public int getEndTime() { return endtime; }
    @Override
    public int getPlayerHealth() { return levelHP; }

    public static class FlyingBone extends Projectile {
        public static final Image bonypict = new ImageIcon("Pics/bonepic.png", "bonepic").getImage();
        public static final Image orangeBony = new ImageIcon("Pics/orangebone.png", "orangebone").getImage();
        public static final Image blueBony = new ImageIcon("Pics/bluebone.png", "bluebone").getImage();
        public int picwidth = 100;
        public int picheight = 30;
        public Image image;
        public double angle;
        int xregister;
        int yregister;
        AffineTransform tx;
        Area oldArea;

        public FlyingBone(double d, int bs) {
            this(d, bs, bonypict);
        }

        public FlyingBone(double d, int bs, Image i) {
            this(0, 0, d, bs, i);
        }
        public FlyingBone(int x, int y, double d, int bs, Image i) {
            super(x, y, bs, new ImageIcon(i.getScaledInstance(100, 30, java.awt.Image.SCALE_SMOOTH)));
            image = i;
            angle = d;
            tx = new AffineTransform();
            tx.rotate(angle);
            oldArea = new Area();
        }

        public void setDimensions(int newWidth, int newHeight) {
            picwidth = newWidth;
            picheight = newHeight;
            icon = new ImageIcon(image.getScaledInstance(picwidth, picheight, java.awt.Image.SCALE_SMOOTH));
        }

        @Override
        public void moveTick() {
            moveOffset((int) (getSpeed() * Math.cos(angle)), (int) (getSpeed() * Math.sin(angle)));
            updateRegister();
        }

        public void updateRegister() {
            xregister = (int) (getX() * Math.cos(angle) + getY() * Math.sin(angle));
            yregister = (int) (-getX() * Math.sin(angle) + getY() * Math.cos(angle));
        }

        @Override
        public void moveTo(int x, int y) {
            super.moveTo(x, y);
            updateRegister();
        }

        @Override
        public Area getCollision() {
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
            icon.paintIcon(jp, g2d, xregister - picwidth / 2, yregister - picheight / 2);
            g2d.rotate(-angle);
        }

        @Override
        public FlyingBone clone() {
            FlyingBone fb = new FlyingBone(angle, getSpeed(), image);
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
        public void moveTick() {
            xvelocity += xaccel;
            yvelocity += yaccel;
            moveOffset((int) xvelocity, (int)yvelocity);
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
            ArcingBone ab = new ArcingBone(angle, xvelocity, yvelocity, xaccel, yaccel, image);
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
        public void moveTick() {
            angle += angularVel;
            xvelocity += xaccel;
            yvelocity += yaccel;
            moveOffset((int) xvelocity, (int) yvelocity);
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

        public static class SimpleLineExplosion extends BaseLevel.LineExploder{

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

