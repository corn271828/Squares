/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.api.block;

import javax.swing.ImageIcon;

/**
 *
 * @author piercelai
 */
public interface LinkableBlock extends Block {
    void onLinkedBlockChange();
    int getLinkIndex(); // TODO do it better >:(
}
