/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.level;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import squares.Player;
import squares.api.AABB;
import squares.api.AudioManager;
import squares.api.Clock;
import squares.api.Coordinate;
import squares.api.ResourceLoader;
import squares.api.block.Block;
import squares.api.block.BlockFactory;
import squares.api.block.Entity;
import squares.api.block.FiringBlock;
import squares.api.block.Projectile;
import squares.api.level.BossLevel;
import squares.api.level.Level;
import static squares.level.SJBossFight.ARCING_BONE_CHAR;
import squares.level.SJBossFight.ArcingBone;
import static squares.level.SJBossFight.FLYING_BONE_CHAR;
import squares.level.SJBossFight.FlyingBone;
import static squares.level.SJBossFight.LINE_EXPLODER_TESTER_CHAR;
import static squares.level.SJBossFight.ROTATING_BONE_CHAR;
import squares.level.SJBossFight.RotatingBone;
import static squares.api.RenderingConstants.*;
import squares.level.SJBossFight.BoneExploder;

/**
 *
 * @author piercelai
 */
public class TesterLevel extends BaseLevel {

    
    public TesterLevel(String[][] in, String[] args, BlockFactory bf) {// for now
        super(in, args, bf);
    }
    
    @Override
    public void tickBlocks(squares.Player player, Coordinate drawingStart) {
        super.tickBlocks(player, drawingStart);
       
        Coordinate middle = new Coordinate(drawingStart.x + SPACING_BETWEEN_BLOCKS * 3 + STANDARD_ICON_WIDTH / 2,
        drawingStart.y + SPACING_BETWEEN_BLOCKS * 3 + STANDARD_ICON_WIDTH / 2);
        Coordinate end = new Coordinate(drawingStart.x + SPACING_BETWEEN_BLOCKS * 6 + STANDARD_ICON_WIDTH - 2, 
                drawingStart.y + SPACING_BETWEEN_BLOCKS * 6 + STANDARD_ICON_WIDTH - 2);
        
        if (label.equals("0")) {
            if (player.clock.time() % 30 == 10) {
                blasts.add(new LineExploderTester(player.clock.time(), 20, Math.PI,
                        drawingStart.x + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, drawingStart.y - 80 + SPACING_BETWEEN_BLOCKS * 6, player.clock));
            }

            if (player.clock.time() % 30 == 5) {
                blasts.add(new LineExploderTester(player.clock.time(), 20, 0,
                        drawingStart.x + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, drawingStart.y - 80, player.clock));
            }

            if (player.clock.time() % 15 == 10) {
                blasts.add(new LineExploderTester(player.clock.time(), 20, 0,
                        drawingStart.x + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, drawingStart.y - 80, player.clock));
            }

            if (player.clock.time() % 15 == 5) {
                blasts.add(new LineExploderTester(player.clock.time(), 20, Math.PI * .5,
                        drawingStart.x + SPACING_BETWEEN_BLOCKS * 4 + 2 * STANDARD_ICON_WIDTH, drawingStart.y - 80 + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 5) + 1), player.clock));
            }

        }

        if (label.equals("-1")) {
            end = new Coordinate(drawingStart.x + SPACING_BETWEEN_BLOCKS * 5 + STANDARD_ICON_WIDTH - 2, 
                drawingStart.y + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH - 2);

            if (player.clock.time() % 12 == 1) {
                FlyingBone hold = new SJBossFight.FlyingBone(0, 20);
                hold.moveTo(drawingStart.x, drawingStart.y + 100);
                blasts.add(hold);
                hold = new SJBossFight.FlyingBone(0, 20);
                hold.moveTo(drawingStart.x, drawingStart.y + 100 + SPACING_BETWEEN_BLOCKS * 3);
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 8) {
                FlyingBone hold = new SJBossFight.FlyingBone(Math.PI, 40);
                hold.moveTo(end.x, drawingStart.y + 40);
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 5) {
                FlyingBone hold = new SJBossFight.FlyingBone(Math.PI, 40);
                hold.moveTo(end.x, drawingStart.y + 40 + SPACING_BETWEEN_BLOCKS * 3);
                blasts.add(hold);
            }
            if (player.clock.time() % 30 == 17) {
                FlyingBone hold = new SJBossFight.FlyingBone(Math.PI, 40);
                hold.moveTo(end.x, drawingStart.y + 40 + SPACING_BETWEEN_BLOCKS * 4);
                blasts.add(hold);
            }

            if (player.clock.time() % 12 == 5) {
                FlyingBone hold = new SJBossFight.FlyingBone(Math.PI / 2, 20);
                hold.moveTo(drawingStart.x + 100, drawingStart.y);
                blasts.add(hold);
                hold = new SJBossFight.FlyingBone(Math.PI / 2, 20);
                hold.moveTo(drawingStart.x + 100 + SPACING_BETWEEN_BLOCKS * 3, drawingStart.y);
                blasts.add(hold);
            }
        }

        if (label.equals("-2")) {
            end = new Coordinate(drawingStart.x + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH - 2, 
                drawingStart.y + SPACING_BETWEEN_BLOCKS * 4 + STANDARD_ICON_WIDTH - 2);
            if (player.clock.time() % 30 == 4) {
                ArcingBone hold = new SJBossFight.ArcingBone(Math.PI / 2, 40, 0, -4, 0);
                hold.moveTo(drawingStart.x, drawingStart.y + 40);
                blasts.add(hold);
            }

            if (player.clock.time() % 20 == 4) {
                ArcingBone hold = new SJBossFight.ArcingBone(Math.PI / 2, -20, 0, -3, 0);
                hold.moveTo(end.x, drawingStart.y + 40);
                blasts.add(hold);
            }

            if (player.clock.time() % 20 == 14) {
                ArcingBone hold = new SJBossFight.ArcingBone(Math.PI / 2, -20, 0, -3, 0);
                hold.moveTo(end.x, drawingStart.y + 40 + SPACING_BETWEEN_BLOCKS);
                blasts.add(hold);
            }

            if (player.clock.time() % 25 == 2) {
                ArcingBone hold = new SJBossFight.ArcingBone(0, 0, 20, 0, 3);
                hold.moveTo(drawingStart.x + 40 + SPACING_BETWEEN_BLOCKS, drawingStart.y);
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 4) {
                ArcingBone hold = new SJBossFight.ArcingBone(3 * Math.PI / 4, 30, 30, -2, -2);
                hold.moveTo(drawingStart.x + 2, drawingStart.y);
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 20) {
                RotatingBone hold = new SJBossFight.RotatingBone(3 * Math.PI / 4, -28, -28, 0, 0, 0.4);
                hold.moveTo(end.x, end.y);
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 20) {
                RotatingBone hold = new SJBossFight.RotatingBone(3 * Math.PI / 4, 28, 0, 2, 0, 0.4);
                hold.moveTo(drawingStart.x, end.y - STANDARD_ICON_WIDTH / 2);
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 5) {
                RotatingBone hold = new SJBossFight.RotatingBone(3 * Math.PI / 4, 28, 0, 2, 0, 0.4);
                hold.moveTo(drawingStart.x, end.y - STANDARD_ICON_WIDTH / 2 - SPACING_BETWEEN_BLOCKS);
                blasts.add(hold);
            }

        }

        if (label.equals("-3")) {
            if (player.clock.time() % 30 == 5) {
                BoneExploder hold = new SJBossFight.BoneExploder(0, 0, 0, 0, 2, .4, STANDARD_ICON_WIDTH);
                hold.moveTo(drawingStart.x + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * (int) (Math.random() * 5 + 1), drawingStart.y);
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 20) {
                BoneExploder hold = new SJBossFight.BoneExploder(0, 0, 0, 0, -2, .4, STANDARD_ICON_WIDTH);
                hold.moveTo(drawingStart.x + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * (int) (Math.random() * 5 + 1), end.y);
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 13) {
                BoneExploder hold = new SJBossFight.BoneExploder(0, 0, 0, 2, 0, .4, STANDARD_ICON_WIDTH);
                hold.moveTo(drawingStart.x, drawingStart.y + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * (int) (Math.random() * 5 + 1));
                blasts.add(hold);
            }

            if (player.clock.time() % 30 == 28) {
                BoneExploder hold = new SJBossFight.BoneExploder(0, 0, 0, -2, 0, .4, STANDARD_ICON_WIDTH);
                hold.moveTo(end.x, drawingStart.y + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * (int) (Math.random() * 5 + 1));
                blasts.add(hold);
            }

            
            if (player.clock.time() % 20 == 10) {
                double ang = Math.atan2(player.render.y - middle.y, player.render.x - middle.x);
                ArcingBone hold = new SJBossFight.ArcingBone(ang + Math.PI / 2, -20 * Math.cos(ang), -20 * Math.sin(ang),  5 * Math.cos(ang), 5 * Math.sin(ang));
                hold.moveTo(middle.x, middle.y);
                blasts.add(hold);
            }
        }

        if (label.equals("-4")) {
            if (player.clock.time() == 2) {
                RotatingBone hold = new SJBossFight.RotatingBone(0, 0, 0, 0, 0, -.055);
                hold.moveTo(middle.x, middle.y);
                hold.setDimensions(500, 30);
                blasts.add(hold);
            }

            if (player.clock.time() % 20 == 2) {
                RotatingBone hold = new SJBossFight.RotatingBone(0, 0, -3, 0, -2, 0.1);
                hold.moveTo(drawingStart.x + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * 5 / 2, end.y);
                hold.setDimensions(200, 30);
                blasts.add(hold);
            }

            if (player.clock.time() % 20 == 4) {
                RotatingBone hold = new SJBossFight.RotatingBone(0, 0, 3, 0, 2, -0.1);
                hold.moveTo(drawingStart.x + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * 9 / 2, drawingStart.y);
                hold.setDimensions(200, 30);
                blasts.add(hold);
            }
        }
    }
}
