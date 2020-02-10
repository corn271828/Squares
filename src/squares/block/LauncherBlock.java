/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import javax.swing.ImageIcon;

import squares.Player;
import squares.api.CharacterState;
import squares.api.Direction;
import squares.api.block.Block;
import squares.api.block.DirectedBlock;

import static squares.api.RenderingConstants.BORDER_WIDTH;
import static squares.api.RenderingConstants.SPACING_BETWEEN_BLOCKS;

/**
 *
 * @author piercelai
 */
public class LauncherBlock extends Block implements DirectedBlock {

    private static final ImageIcon[] launcherBlocks = new ImageIcon[Direction.values().length];
    protected Direction direction;

    public LauncherBlock(Direction direction) {
        super(getIconByDirection(direction), "Launcher Block", true);
        this.direction = direction;
    }

    private static ImageIcon getIconByDirection(Direction d) {
        int i = d.ordinal();
        if(launcherBlocks[i] == null)
            launcherBlocks[i] = new ImageIcon(String.format("Pics/Launcher Block %s.png", d.name), "Launcher Block Image");
        return launcherBlocks[i];
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void onLand(Player player) {
        switch(getDirection()) {
            case UP :
                while(player.yPosition > 0 && (player.level.blocks[player.yPosition - 1][player.xPosition] != null && !player.level.blocks[player.yPosition - 1][player.xPosition].stepable
                        || player.level.blocks[player.yPosition - 1][player.xPosition] == null))
                    player.yPosition--;
                player.yPosition--;
                player.charState = CharacterState.FASTMOVING;
                break;
            case RIGHT :
                while(player.xPosition < player.level.blocks[0].length && (player.level.blocks[player.yPosition][player.xPosition + 1] != null &&
                        !player.level.blocks[player.yPosition][player.xPosition + 1].stepable || player.level.blocks[player.yPosition][player.xPosition + 1] == null )) {
                     player.xPosition++;
                }
                player.xPosition++;
                player.charState = CharacterState.FASTMOVING;
                break;
            case DOWN :
                while(player.yPosition < player.level.blocks.length && (player.level.blocks[player.yPosition + 1][player.xPosition] != null && 
                        !player.level.blocks[player.yPosition + 1][player.xPosition].stepable  || player.level.blocks[player.yPosition + 1][player.xPosition] == null))
                    player.yPosition++;
                player.yPosition++;
                player.charState = CharacterState.FASTMOVING;
                break;
            case LEFT :
                while(player.xPosition > 0 && (player.level.blocks[player.yPosition][player.xPosition - 1] != null 
                        && !player.level.blocks[player.yPosition][player.xPosition - 1].stepable  || player.level.blocks[player.yPosition][player.xPosition - 1] == null ))
                    player.xPosition--;
                player.xPosition--;
                player.charState = CharacterState.FASTMOVING;
                break;
        }
    }
}
