/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import javax.swing.ImageIcon;

import squares.Player;
import squares.api.ResourceLoader;
import squares.api.block.Block;
import squares.api.CharacterState;

/**
 *
 * @author piercelai
 */

public class EndingBlock extends BaseBlock {
    public static final ImageIcon endingBlockIcon = new ResourceLoader("sprites/block", "ending").asImageIcon();

    public EndingBlock() {
        super(endingBlockIcon, "End Block");
    }

    @Override
    public void onLand(Player player) {
        player.charState = CharacterState.NORMAL;
    }
    @Override
    public boolean canStep() {
        return true;
    }

}
