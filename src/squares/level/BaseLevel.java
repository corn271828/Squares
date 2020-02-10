/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.level;

import java.awt.Graphics;
import java.awt.Component;
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
import squares.api.block.Entity;
import squares.api.block.Block;
import squares.api.block.BlockFactory;
import squares.api.level.Level;
import squares.api.Coordinate;

/**
 *
 * @author lai_889937
 */

// Holds the layout of a single level
public class BaseLevel extends Level {
    protected String[][] design; // level layout

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
                blocks[rowNumber][columnNumber] = bf.makeBlock(in[rowNumber][columnNumber], columnNumber, rowNumber);

        bf.removeBlockListener('X', startpos);
        bf.removeBlockListener('O', endpos);
        start = new Coordinate(temp[0], temp[1]);
        end = new Coordinate(temp[2], temp[3]);
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
    public void reset() {
        for (Block[] row : blocks)
            for (Block cell : row)
                if (cell != null) cell.reset();
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
    @Override
    public Coordinate getStartPos() {
        return start;
    }
    @Override
    public Coordinate getEndPos() {
        return end;
    }
    
    public static class LineExploder extends Entity {
        public final int starttime;
        public final int timelength;
        public final double angle; //Angle be in radians
        int xregister;
        int yregister;
        AffineTransform tx;

        public LineExploder(int stt, int tl, double ang, int sxp, int syp) {
            super(sxp, syp);
            starttime = stt;
            timelength = tl;
            angle = ang;
            updateRegister();
            tx = new AffineTransform();
            tx.rotate(angle);
        }

        @Override
        public void draw(Graphics g, Component c) {}

        @Override
        public Area getCollision() {
            return new Area();
        }

        @Override
        public Area getClip() {
            return new Area();
        }

        public void updateRegister() {
            xregister = (int) (getX() * Math.cos(angle) + getY() * Math.sin(angle));
            yregister = (int) (-getX() * Math.sin(angle) + getY() * Math.cos(angle));
        }
        @Override
        public LineExploder clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
        }
    }

}
