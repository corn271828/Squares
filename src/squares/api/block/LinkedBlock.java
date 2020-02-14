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
 * @param <T>
 */
public abstract class LinkedBlock<T extends LinkableBlock> extends Block {
    public T linked;
    
    public LinkedBlock(ImageIcon i, String label, boolean b) {
        super(i, label, b);
    }
    
    public abstract void setLinked(T block);
    
}
