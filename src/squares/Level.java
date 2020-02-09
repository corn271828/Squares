/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares;

import squares.block.LauncherBlock;
import squares.block.Block;
import squares.block.EndingBlock;
import squares.block.BlasterBlock;
import squares.block.CannonBlock;
import squares.block.NormalBlock;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import squares.api.ResourceLocator;
import squares.api.block.Projectile;
import squares.api.Direction;

/**
 *
 * @author lai_889937
 */

// Holds the layout of a single level
public class Level {
    protected String levelLabel; // Name of Level
    protected String[][] design; // level layout
    private String levelCode;

    public final Block[][] blocks;

    protected int startPosRow = -78954; // Starting position in indices
    protected int startPosCol = -84739;
    protected int endPosRow = -48395; // Ending position in indices
    protected int endPosCol = -23481;

    // Static variables for reference
    public static final char START_CHAR = 'X';
    public static final char END_CHAR = 'O';
    public static final char NORMAL_BLOCK_CHAR = 'N';
    public static final char LAUNCHER_BLOCK_CHAR = 'L';
    public static final char BLASTER_BLOCK_CHAR = 'B';
    public static final char CANNON_BLOCK_CHAR = 'C';

    private int numDeaths;

    public Level(String[][] in, String[] args) {
        this(in, args[0], args.length >= 2 ? args[1] : null);
    }

    public Level(String[][] in, String label, String code) {
        design = in;
        for (int i = 0; i < in.length; i++)
            for (int j = 0; j < in[0].length; j++)
            {
                if(in[i][j].length() > 0)
                    switch(in[i][j].charAt(0)) {
                        case START_CHAR:
                            startPosRow = i;
                            startPosCol = j;
                            break;
                        case END_CHAR:
                            endPosRow = i;
                            endPosCol = j;
                            break;
                    }
            }
        levelLabel = label;
        levelCode = code == null ? "\"\\\"" : eviscerator(code);
        blocks = new Block[in.length][in[0].length];
        for (int rowNumber = 0; rowNumber < in.length; rowNumber++) {
            for (int columnNumber = 0; columnNumber < in[0].length; columnNumber++) {

                Block hold = null;
                if(in[rowNumber][columnNumber] == null || 
                        "".equals(in[rowNumber][columnNumber]))
                    continue;
                switch(in[rowNumber][columnNumber].charAt(0)) {
                    case START_CHAR:
                    case NORMAL_BLOCK_CHAR:
                        hold = new NormalBlock();
                        break;

                    case END_CHAR:
                        hold = new EndingBlock();
                        break;

                    case LAUNCHER_BLOCK_CHAR:
                        switch(in[rowNumber][columnNumber].charAt(1)) {
                            case '^':
                                hold = new LauncherBlock(Direction.UP);
                                break;
                            case '>':
                                hold = new LauncherBlock(Direction.RIGHT);
                                break;
                            case 'v':
                                hold = new LauncherBlock(Direction.DOWN);
                                break;
                            case '<':
                                hold = new LauncherBlock(Direction.LEFT);
                                break;
                        }
                        break;

                    case BLASTER_BLOCK_CHAR:
                        int dbs = Integer.parseInt(in[rowNumber][columnNumber].substring(2,4));
                        int fbs = Integer.parseInt(in[rowNumber][columnNumber].substring(4,6));
                        int delay = 1;
                        if (in[rowNumber][columnNumber].length() > 6)
                            delay = Integer.parseInt(in[rowNumber][columnNumber].substring(6, 8));

                        switch(in[rowNumber][columnNumber].charAt(1)) {
                            case '^':
                                hold = new BlasterBlock(Direction.UP, dbs, fbs, delay);
                                break;
                            case '>':
                                hold = new BlasterBlock(Direction.RIGHT, dbs, fbs, delay);
                                break;
                            case 'v':
                                hold = new BlasterBlock(Direction.DOWN, dbs, fbs, delay);
                                break;
                            case '<':
                                hold = new BlasterBlock(Direction.LEFT, dbs, fbs, delay);
                                break;
                        }
                        break;

                    case CANNON_BLOCK_CHAR:
                        dbs = Integer.parseInt(in[rowNumber][columnNumber].substring(1,3));
                        fbs = Integer.parseInt(in[rowNumber][columnNumber].substring(3,5));
                        delay = 1;
                        if (in[rowNumber][columnNumber].length() > 5)
                            delay = Integer.parseInt(in[rowNumber][columnNumber].substring(5, 7));

                        hold = new CannonBlock(dbs, fbs, delay);

                        break;
                    case '0': break;
                    default: throw new IllegalArgumentException("Unknown ID-char: " + in[rowNumber][columnNumber].charAt(0));
                }
                blocks[rowNumber][columnNumber] = hold;
            }

        }
    }

    public Level(String[][] in, String label) {
        this(in, label, null);
    }

    private String eviscerator(String input) {
        String ret = "";
        for (int i = 0; i < input.length(); i+=3)
            ret = ret.concat(String.valueOf(input.charAt(i)));
        for (int i = 2; i < input.length(); i+=3)
            ret = ret.concat(String.valueOf(input.charAt(i)));
        for (int i = 1; i < input.length(); i+=3)
            ret = ret.concat(String.valueOf(input.charAt(i)));
        return ret;
    }

    public String getCode() {
        return levelCode;
    }
        
    public Color getBackgroundColor(int timestamp) {
        return Color.WHITE;
    }

    public void drawBackground(Graphics g, int timestamp, Component c, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS) {}

    public void drawForeground(Graphics g, int timestamp, Component c, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS) {}

    public int getDeaths() {
        return numDeaths;
    }
    public void addDeath() {
        numDeaths++;
    }

    public static abstract class BossLevel extends Level{
        String[] controls; // Represents autogenerated blasts and attacks and whatnot (without specific blocks)
        public static final char BLAST_CHAR = 'b';
        public static final char ROW_EXPLOSION = 'l';
        public static final char COLUMN_EXPLOSION = 'l';
        public int endtime;

        public int levelHP;

        public BossLevel(String[][] in, String label, String... controls) {
            super(in, label);
            this.controls = controls;
        }

        public BossLevel(String[][] in, String label, int hp, String... controls) {
            this(in, label, controls);
            levelHP = hp;
        }

        public BossLevel(String[][] in, String label, int hp, String code, String... controls) {
            super(in, label, code);
            this.controls = controls;
            levelHP = hp;
        }

        public BossLevel(String[][] in, String label, ResourceLocator input) {
            super(in, label);
            try (BufferedReader br = input.asBufferedReader()) {
                String hold = "";
                while(br.ready()) 
                    hold = hold.concat(br.readLine());
                controls = hold.split(",");
                for (int i = 0; i < controls.length; i++)
                    controls[i] = controls[i].trim();
            } catch (FileNotFoundException fnfe) {
                System.out.println(fnfe);
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }

        public BossLevel(String[][] in, String label, String code, ResourceLocator input) {
            super(in, label, code);
            try (BufferedReader br = input.asBufferedReader()) {
                String hold = "";
                while(br.ready()) 
                    hold = hold.concat(br.readLine());
                controls = hold.split(",");
                for (int i = 0; i < controls.length; i++)
                    controls[i] = controls[i].trim();
            } catch (FileNotFoundException fnfe) {
                System.out.println(fnfe);
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }

        public abstract java.util.ArrayList<? extends Projectile> generateBlasts(int timestamp, int xCoordinates, int yCoordinates, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS);

        public abstract java.util.ArrayList<? extends LineExploder> generateLines(int timestamp, int xCoordinates, int yCoordinates, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS);

        public abstract void generateHashMaps();

        public static class LineExploder {
            public int starttime;
            public int timelength;
            public double angle; //Angle be in radians
            public int startxPosition;
            public int startyPosition;
            int xregister;
            int yregister;
            AffineTransform tx;

            public LineExploder(int stt, int tl, double ang, int sxp, int syp) {
                starttime = stt;
                timelength = tl;
                angle = ang;
                startxPosition = sxp;
                startyPosition = syp;
                xregister = (int) (startxPosition * Math.cos(angle) + startyPosition * Math.sin(angle));
                yregister = (int) (-startxPosition * Math.sin(angle) + startyPosition * Math.cos(angle));
                tx = new AffineTransform();
                tx.rotate(angle);
            }

            public void drawXPlosion(Component c, Graphics g, int timestamp) {

            }

            public Area xplosionOuchArea(int timestamp) {
                return new Area();
            }

            public Area xplosionClipArea(int timestamp) {
                return new Area();
            }

            public void updateRegister() {
                xregister = (int) (startxPosition * Math.cos(angle) + startyPosition * Math.sin(angle));
                yregister = (int) (-startxPosition * Math.sin(angle) + startyPosition * Math.cos(angle));
            }

            @Override
            public LineExploder clone() {
                return new LineExploder(starttime, timelength, angle, startxPosition, startyPosition);
            }

        }

    }

}
