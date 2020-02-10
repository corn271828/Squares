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
                while(player.position.y > 0 && (player.level.blockAt(player.position.x, player.position.y - 1) != null && !player.level.blockAt(player.position.x, player.position.y - 1).stepable
                        || player.level.blockAt(player.position.x, player.position.y - 1) == null))
                    player.position.y--;
                player.position.y--;
                player.charState = CharacterState.FASTMOVING;
                break;
            case RIGHT :
                while(player.position.x < player.level.xSize() && (player.level.blockAt(player.position.x + 1, player.position.y) != null &&
                        !player.level.blockAt(player.position.x + 1, player.position.y).stepable || player.level.blockAt(player.position.x + 1, player.position.y) == null )) {
                     player.position.x++;
                }
                player.position.x++;
                player.charState = CharacterState.FASTMOVING;
                break;
            case DOWN :
                while(player.position.y < player.level.ySize() && (player.level.blockAt(player.position.x, player.position.y + 1) != null && 
                        !player.level.blockAt(player.position.x, player.position.y + 1).stepable  || player.level.blockAt(player.position.x, player.position.y + 1) == null))
                    player.position.y++;
                player.position.y++;
                player.charState = CharacterState.FASTMOVING;
                break;
            case LEFT :
                while(player.position.x > 0 && (player.level.blockAt(player.position.x - 1, player.position.y) != null 
                        && !player.level.blockAt(player.position.x - 1, player.position.y).stepable  || player.level.blockAt(player.position.x - 1, player.position.y) == null ))
                    player.position.x--;
                player.position.x--;
                player.charState = CharacterState.FASTMOVING;
                break;
        }
    }
}
