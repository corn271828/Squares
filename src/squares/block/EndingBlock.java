/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import javax.swing.ImageIcon;

import squares.Player;
import squares.api.block.Block;
import squares.api.CharacterState;

/**
 *
 * @author piercelai
 */

public class EndingBlock extends Block {
    public static final ImageIcon endingBlockIcon = new ImageIcon("Pics/Ending Block.png", "End Block Image");

    public EndingBlock() {
        super(endingBlockIcon, "End Block", true);
    }

    @Override
    public void onLand(Player player) {
        player.charState = CharacterState.NORMAL;
    }

}
