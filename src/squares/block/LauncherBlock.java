/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import javax.swing.ImageIcon;

import squares.Player;
import squares.api.CharacterState;
import squares.api.ResourceLoader;
import squares.api.Direction;
import squares.api.block.DirectedBlock;

/**
 *
 * @author piercelai
 */
public class LauncherBlock extends BaseBlock implements DirectedBlock {

    private static final ImageIcon[] launcherBlocks = new ImageIcon[Direction.values().length];
    protected Direction direction;

    public LauncherBlock(Direction direction) {
        super(getIconByDirection(direction), "Launcher Block");
        this.direction = direction;
    }

    private static ImageIcon getIconByDirection(Direction d) {
        int i = d.ordinal();
        if(launcherBlocks[i] == null)
            launcherBlocks[i] = new ResourceLoader("sprites/block", "launcher_" + d.name.toLowerCase()).asImageIcon();
        return launcherBlocks[i];
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
    @Override
    public boolean canStep() {
        return true;
    }

    @Override
    public void onLand(Player player) {
        switch(getDirection()) {
            case UP :
                while(player.position.y > 0 && (player.level.blockAt(player.position.x, player.position.y - 1) != null && !player.level.blockAt(player.position.x, player.position.y - 1).canStep()
                        || player.level.blockAt(player.position.x, player.position.y - 1) == null))
                    player.position.y--;
                player.position.y--;
                player.charState = CharacterState.FASTMOVING;
                break;
            case RIGHT :
                while(player.position.x < player.level.xSize() - 1 && (player.level.blockAt(player.position.x + 1, player.position.y) != null &&
                        !player.level.blockAt(player.position.x + 1, player.position.y).canStep() || player.level.blockAt(player.position.x + 1, player.position.y) == null )) {
                     player.position.x++;
                }
                player.position.x++;
                player.charState = CharacterState.FASTMOVING;
                break;
            case DOWN :
                while(player.position.y < player.level.ySize() - 1 && (player.level.blockAt(player.position.x, player.position.y + 1) != null && 
                        !player.level.blockAt(player.position.x, player.position.y + 1).canStep()  || player.level.blockAt(player.position.x, player.position.y + 1) == null))
                    player.position.y++;
                player.position.y++;
                player.charState = CharacterState.FASTMOVING;
                break;
            case LEFT :
                while(player.position.x > 0 && (player.level.blockAt(player.position.x - 1, player.position.y) != null 
                        && !player.level.blockAt(player.position.x - 1, player.position.y).canStep()  || player.level.blockAt(player.position.x - 1, player.position.y) == null ))
                    player.position.x--;
                player.position.x--;
                player.charState = CharacterState.FASTMOVING;
                break;
        }
    }
}
