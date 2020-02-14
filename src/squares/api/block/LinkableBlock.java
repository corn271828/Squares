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
public abstract class LinkableBlock extends Block {
    
    public LinkableBlock(ImageIcon i, String label, boolean b) {
        super(i, label, b);
    }
    
}
