/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import javax.swing.ImageIcon;
import squares.Player;
import squares.api.ResourceLoader;
import squares.api.CharacterState;
import squares.api.block.Block;

/**
 *
 * @author piercelai
 */
public class NormalBlock extends BaseBlock {

    public static final ImageIcon normalBlockIcon = new ResourceLoader("sprites/block", "normal").asImageIcon();

    public NormalBlock() {
        super(normalBlockIcon, "Normal Block");
    }

    @Override
    public boolean canStep() {
        return true;
    }

    @Override
    public void onLand(Player player) {
        player.charState = CharacterState.NORMAL;
    }

}
