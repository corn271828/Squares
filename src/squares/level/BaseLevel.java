/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.level;

import java.awt.Graphics;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;

import squares.api.AABB;
import squares.api.AudioManager;
import squares.api.Clock;
import squares.api.entity.Entity;
import squares.api.block.Block;
import squares.api.block.FiringBlock;
import squares.api.block.TargetingBlock;
import squares.api.block.LinkedBlock;
import squares.api.block.LinkableBlock;
import squares.api.block.BlockFactory;
import squares.api.level.Level;
import squares.api.Coordinate;
import squares.block.HighExplosion;

import static squares.api.RenderingConstants.SPACING_BETWEEN_BLOCKS;
import static squares.api.RenderingConstants.STANDARD_ICON_WIDTH;

/**
 *
 * @author lai_889937
 */

// Holds the layout of a single level
public class BaseLevel extends Level {
    protected String[][] design; // level layout

    public final Block[][] blocks;

    public final Coordinate start, end; // Starting position in indices

    protected Set<Entity> blasts = new HashSet<>();
    
    //protected ButtonBlock[] buttons = new ButtonBlock[3]; // Needs a fast reference place

    public BaseLevel(String[][] in, String[] args, BlockFactory bf) {
        this(in, args[0], args.length >= 2 ? args[1] : null, bf);
    }

    public BaseLevel(String[][] in, String label, String code, BlockFactory bf) {
        super(label, code == null ? "\"\\\"" : eviscerator(code));
        design = in;

        int[] temp = new int[4];
        Map<Integer, List<LinkableBlock>> linkables = new HashMap<>();
        BlockFactory.BlockListener startpos = ($, x, y) -> {
            temp[0] = x;
            temp[1] = y;
        };
        BlockFactory.BlockListener catchLinks = (b, $1, $2) -> {
            assert b instanceof LinkableBlock;
            LinkableBlock lb = (LinkableBlock) b;
            linkables.computeIfAbsent(lb.getLinkIndex(), i -> new LinkedList<>()).add(lb);
        };
        BlockFactory.BlockListener endpos = ($, x, y) -> {
            temp[2] = x;
            temp[3] = y;
        };
        bf.addBlockListener('X', startpos);
        bf.addBlockListener('O', endpos);
        bf.addBlockListener('V', catchLinks);

        blocks = new Block[in.length][in[0].length];

        for (int rowNumber = 0; rowNumber < in.length; rowNumber++)
            for (int columnNumber = 0; columnNumber < in[0].length; columnNumber++) {
                blocks[rowNumber][columnNumber] = bf.makeBlock(in[rowNumber][columnNumber], columnNumber, rowNumber);
            }
        for (Block[] row : blocks)
            for (Block bl : row) {
                if (bl instanceof LinkedBlock) {
                    LinkedBlock lb = ((LinkedBlock) bl);
                    for(LinkableBlock target: linkables.get(lb.getTarget()))
                        lb.linkTo(target);
                }
            }
        bf.removeBlockListener('X', startpos);
        bf.removeBlockListener('O', endpos);
        bf.removeBlockListener('V', catchLinks);
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
    public void tickBlocks(squares.Player player, Coordinate drawingStart) {
        for (int rowNumber = 0; rowNumber < player.level.ySize(); rowNumber++) {
            for (int columnNumber = 0; columnNumber < player.level.xSize(); columnNumber++) {
                Block currBlock = blockAt(columnNumber, rowNumber);
                if (currBlock == null) {
                    continue;
                }

                //Generates blasts at correct time
                if (currBlock instanceof TargetingBlock) {
                    ((TargetingBlock) currBlock).setTarget(player.render.x, player.render.y); // for now.
                }
                if (currBlock instanceof FiringBlock) {
                    FiringBlock fb = (FiringBlock) currBlock;

                    if ((player.clock.time() - fb.getPhase()) % fb.getPeriod() == 0) {
                        blasts.add(fb.createAtCoords(drawingStart.x + columnNumber * SPACING_BETWEEN_BLOCKS, drawingStart.y + rowNumber * SPACING_BETWEEN_BLOCKS));
                    }
                }

            }
        }
    }
    
    @Override
    public void tickEntities(squares.Player player, AABB check) {
        List<Entity> toAdd = new ArrayList<>();
        for (Iterator<Entity> it = blasts.iterator(); it.hasNext();) {
            Entity bla = it.next();

            if (shouldRemoveEntity(bla, check, player.clock)) {

                if (bla instanceof HighExplosion) {
                    double holdangle = 0;
                    if (bla.getX() > check.rx + 10) {
                        holdangle = Math.PI / 2;
                    } else if (bla.getX() < check.lx - 10) {
                        holdangle = 3 * Math.PI / 2;
                    } else if (bla.getY() > check.ry + 10) {
                        holdangle = Math.PI;
                    } else {
                        holdangle = 0;
                    }
                    toAdd.add(((HighExplosion) bla).getLineExplosion(player.clock.time(), holdangle, bla.getX(), bla.getY()));
                }
                onEntityRemove(bla);
                it.remove();
            }
        }
        blasts.addAll(toAdd);
    }

    protected boolean shouldRemoveEntity(Entity e, AABB check, Clock clock) {
        if(e instanceof LineExploder) {
            LineExploder le = (LineExploder) e;
            if (le.starttime + le.timelength <= clock.time()) return true;
        }
        return !check.contains(e.getX(), e.getY());
    }

    public void onEntityRemove(Entity p) {};

    @Override
    public boolean winCond(squares.Player p) {
        return p.position.equals(end);
    }

    @Override
    public Iterable<Entity> getEntities() {
        return blasts;
    }

    @Override
    public void setup(squares.Player p) {
        setup(p, 1);
    }
    protected void setup(squares.Player player, int hp) {
        blasts.clear();
        for (Block[] row : blocks)
            for (Block cell : row)
                if (cell != null) cell.reset();
        player.setMaxHP(hp);
    }

    @Override
    public void setupMusic(AudioManager audio, Clock c) {
        audio.setPlaying("normal");
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
    
    @Override
    public Area refreshAllBlockIcons(Coordinate drawingStart) {
        Area hold = new Area();
        shouldRefreshIcons = false; 
        for (int rowNumber = 0; rowNumber < blocks.length; rowNumber++)
            for (int columnNumber = 0; columnNumber < blocks[0].length; columnNumber++) {
                if (blocks[rowNumber][columnNumber] == null)
                    continue;
                if (blocks[rowNumber][columnNumber].refreshIcon())
                    hold.add(new Area(new Rectangle(drawingStart.x + columnNumber * SPACING_BETWEEN_BLOCKS,
                                                    drawingStart.y + rowNumber * SPACING_BETWEEN_BLOCKS,
                                                    STANDARD_ICON_WIDTH, STANDARD_ICON_WIDTH)));
            }
        return hold;
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
        public void moveTick() {}

        @Override
        public Area getCollision() {
            return new Area();
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
        
    }

}
