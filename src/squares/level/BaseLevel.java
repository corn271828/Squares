/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.level;

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
import squares.api.level.Level;

/**
 *
 * @author lai_889937
 */

// Holds the layout of a single level
public class BaseLevel extends Level {
    public final String levelLabel; // Name of Level
    protected String[][] design; // level layout
    private String levelCode;

    public final Block[][] blocks;

    public final Coordinate start, end; // Starting position in indices

    public BaseLevel(String[][] in, String[] args, BlockFactory bf) {
        this(in, args[0], args.length >= 2 ? args[1] : null, bf);
    }

    public BaseLevel(String[][] in, String label, String code, BlockFactory bf) {
        super(label, code == null ? "\"\\\"" : eviscerator(code));
        design = in;

        int[] temp = new int[4];
        BlockFactory.BlockListener startpos = ($, x, y) -> {
            temp[0] = x;
            temp[1] = y;
        };
        BlockFactory.BlockListener endpos = ($, x, y) -> {
            temp[2] = x;
            temp[3] = y;
        };
        bf.addBlockListener('X', startpos);
        bf.addBlockListener('O', endpos);

        blocks = new Block[in.length][in[0].length];
        for (int rowNumber = 0; rowNumber < in.length; rowNumber++)
            for (int columnNumber = 0; columnNumber < in[0].length; columnNumber++)
                blocks[rowNumber][columnNumber] = bf.makeBlock(in[rowNumber][columnNumber], rowNumber, columnNumber);

        bf.removeBlockListener('X', startpos);
        bf.removeBlockListener('O', endpos);
        start = new Coordinate(temp[0], temp[1]);
        startPosCol = temp[1];
        endPosRow = temp[2];
        endPosCol = temp[3];
    }

    private static String eviscerator(String input) {
        String ret = "";
        for (int i = 0; i < input.length(); i+=3)
            ret = ret.concat(String.valueOf(input.charAt(i)));
        for (int i = 2; i < input.length(); i+=3)
            ret = ret.concat(String.valueOf(input.charAt(i)));
        for (int i = 1; i < input.length(); i+=3)
            ret = ret.concat(String.valueOf(input.charAt(i)));
        return ret;
    }

    @Override
    public Block blockAt(int x, int y) {
        return blocks[y][x];
    }
    @Override
    public int xSize() {
        return blocks[0].length;
    }
    @Override
    public int ySize() {
        return blocks.length;
    }
    
    public static class LineExploder {
        public int starttime;
        public int timelength;
        public double angle; //Angle be in radians
        public Coordinate startPos;
        int xregister;
        int yregister;
        AffineTransform tx;

        public LineExploder(int stt, int tl, double ang, int sxp, int syp) {
            starttime = stt;
            timelength = tl;
            angle = ang;
            startPos = new Coordinate(sxp, syp);
            xregister = (int) (startPos.x * Math.cos(angle) + startPos.y * Math.sin(angle));
            yregister = (int) (-startPos.x * Math.sin(angle) + startPos.y * Math.cos(angle));
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
            xregister = (int) (startPos.x * Math.cos(angle) + startPos.y * Math.sin(angle));
            yregister = (int) (-startPos.x * Math.sin(angle) + startPos.y * Math.cos(angle));
        }

        @Override
        public LineExploder clone() {
            return new LineExploder(starttime, timelength, angle, startPos.x, startPos.y);
        }

    }

}
