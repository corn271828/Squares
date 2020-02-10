/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.level;

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
import java.util.List;
import java.util.Collections;

import squares.api.ResourceLoader;
import squares.api.block.Projectile;
import squares.api.block.Block;
import squares.api.block.BlockFactory;

/**
 *
 * @author lai_889937
 */

// Holds the layout of a single level
public class Level {
    public final String levelLabel; // Name of Level
    protected String[][] design; // level layout
    private String levelCode;

    public final Block[][] blocks;

    public final int startPosRow; // Starting position in indices
    public final int startPosCol;
    public final int endPosRow; // Ending position in indices
    public final int endPosCol;
    private int spr = -1, spc = -1, epr = -1, epc = -1;

    // Static variables for reference
    public static final char START_CHAR = 'X';
    public static final char END_CHAR = 'O';
    public static final char NORMAL_BLOCK_CHAR = 'N';
    public static final char LAUNCHER_BLOCK_CHAR = 'L';
    public static final char BLASTER_BLOCK_CHAR = 'B';
    public static final char CANNON_BLOCK_CHAR = 'C';

    private int numDeaths;

    public Level(String[][] in, String[] args, BlockFactory bf) {
        this(in, args[0], args.length >= 2 ? args[1] : null, bf);
    }

    public Level(String[][] in, String label, String code, BlockFactory bf) {
        design = in;

        BlockFactory.BlockListener startpos = ($, x, y) -> {
            spr = x;
            spc = y;
        };
        BlockFactory.BlockListener endpos = ($, x, y) -> {
            epr = x;
            epc = y;
        };
        bf.addBlockListener('X', startpos);
        bf.addBlockListener('O', endpos);

        blocks = new Block[in.length][in[0].length];
        for (int rowNumber = 0; rowNumber < in.length; rowNumber++)
            for (int columnNumber = 0; columnNumber < in[0].length; columnNumber++)
                blocks[rowNumber][columnNumber] = bf.makeBlock(in[rowNumber][columnNumber], rowNumber, columnNumber);

        bf.removeBlockListener('X', startpos);
        bf.removeBlockListener('O', endpos);
        startPosRow = spr;
        startPosCol = spc;
        endPosRow = epr;
        endPosCol = epc;
        levelLabel = label;
        levelCode = code == null ? "\"\\\"" : eviscerator(code);
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

        public BossLevel(String[][] in, String label, String code, ResourceLoader input, BlockFactory bf) {
            super(in, label, code, bf);
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

        public abstract List<? extends Projectile> generateBlasts(int timestamp, int xCoordinates, int yCoordinates, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS);

        public abstract List<? extends LineExploder> generateLines(int timestamp, int xCoordinates, int yCoordinates, int startx, int starty, int STANDARD_ICON_WIDTH, int SPACING_BETWEEN_BLOCKS);

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
