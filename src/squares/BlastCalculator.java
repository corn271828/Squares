/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares;

import java.awt.Rectangle;
import java.awt.geom.Area;
import squares.api.CharacterState;
import squares.block.BlasterBlock;
import squares.block.Block;
import squares.block.CannonBlock;

import static squares.api.RenderingConstants.CHARACTER_WIDTH;
import static squares.api.RenderingConstants.SPACING_BETWEEN_BLOCKS;
import static squares.api.RenderingConstants.STANDARD_ICON_WIDTH;

/**
 *
 * @author piercelai
 */
public class BlastCalculator extends Thread {
    MainRunningThing mrt;
    
    public BlastCalculator(MainRunningThing marg) {
        mrt = marg;
    }

     @Override
     public void run() {
         for (int rowNumber = 0; rowNumber < mrt.player.level.blocks.length; rowNumber++) {
             for (int columnNumber = 0; columnNumber < mrt.player.level.blocks[0].length; columnNumber++) {
                 Block currBlock = mrt.player.level.blocks[rowNumber][columnNumber];
                 if (currBlock == null) {
                     continue;
                 }

                 //Generates blasts at correct time
                 if (!(mrt.player.charState == CharacterState.DEAD) && !(mrt.player.charState == CharacterState.RESTARTING) && !(mrt.player.charState == CharacterState.WINE)) {

                     if (currBlock instanceof BlasterBlock) {
                         BlasterBlock currBlaBlock = (BlasterBlock) currBlock;

                         if (mrt.timestamp % currBlaBlock.period == currBlaBlock.delay) {
                             mrt.blasts.add(new BlasterBlock.Blast(currBlaBlock.direction, currBlaBlock.blastSpeed));

                             switch (currBlaBlock.direction) {
                                 case UP:
                                     mrt.blasts.get(mrt.blasts.size() - 1).setCoords(mrt.startx + columnNumber * SPACING_BETWEEN_BLOCKS + 32, mrt.starty + rowNumber * SPACING_BETWEEN_BLOCKS);
                                     break;
                                 case LEFT:
                                     mrt.blasts.get(mrt.blasts.size() - 1).setCoords(mrt.startx + columnNumber * SPACING_BETWEEN_BLOCKS, mrt.starty + rowNumber * SPACING_BETWEEN_BLOCKS + 32);
                                     break;
                                 case RIGHT:
                                     mrt.blasts.get(mrt.blasts.size() - 1).setCoords(mrt.startx + columnNumber * SPACING_BETWEEN_BLOCKS + 30, mrt.starty + rowNumber * SPACING_BETWEEN_BLOCKS + 32);
                                     break;
                                 case DOWN:
                                     mrt.blasts.get(mrt.blasts.size() - 1).setCoords(mrt.startx + columnNumber * SPACING_BETWEEN_BLOCKS + 32, mrt.starty + rowNumber * SPACING_BETWEEN_BLOCKS + 30);
                                     break;
                             }

                             currBlaBlock.primed = false;
                         }
                     }

                     if (currBlock instanceof CannonBlock) {
                         CannonBlock currCanBlock = (CannonBlock) currBlock;
                         if (mrt.timestamp % currCanBlock.period == currCanBlock.delay) {
                             int xcenter = mrt.startx + columnNumber * SPACING_BETWEEN_BLOCKS + STANDARD_ICON_WIDTH / 2;
                             int ycenter = mrt.starty + rowNumber * SPACING_BETWEEN_BLOCKS + STANDARD_ICON_WIDTH / 2;
                             mrt.blasts.add(new CannonBlock.Cannonball(currCanBlock.cannonballSpeed, 
                                     mrt.getAngle(mrt.player.xCoordinates + CHARACTER_WIDTH / 2 - xcenter, mrt.player.yCoordinates + CHARACTER_WIDTH / 2 - ycenter)));
                             mrt.blasts.get(mrt.blasts.size() - 1).setCoords(xcenter, ycenter);
                         }
                     }

                 }

             }
         }

         if (mrt.player.level instanceof Level.BossLevel) {
             mrt.blasts.addAll(((Level.BossLevel) mrt.player.level).generateBlasts(mrt.timestamp, mrt.player.xCoordinates, 
                     mrt.player.yCoordinates, mrt.startx, mrt.starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS));
         }

         // Calculates where the blasts would be
         int charYUpper = mrt.player.yCoordinates;
         int charXLeft = mrt.player.xCoordinates;
         int charYLower = mrt.player.yCoordinates + CHARACTER_WIDTH;
         int charXRight = mrt.player.xCoordinates + CHARACTER_WIDTH;
         for (int i = 0; i < mrt.blasts.size(); i++) {
             BlasterBlock.Blast bla = mrt.blasts.get(i);
             bla.move();
             Area blaouch = bla.getOuchArea();
             Rectangle bound = blaouch.getBounds();
             if (bound.getX() > charXRight || bound.getX() + bound.width < charXLeft || bound.getY() > charYLower || bound.getY() + bound.height < charYUpper) {
             } else {
                 mrt.ouchArea.add(blaouch);
             }
         }
     }
}
