/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import javax.swing.Icon;

/**
 *
 * @author lai_889937
 */
public abstract class Block {
    public final Icon icon;
    public final String label;
    public final boolean stepable; // Represents whether or not one can step on this type of block

    public Block(Icon i, String l, boolean s) {
            icon = i;
            label = l;
            stepable = s;
    }

    public void refreshIcon() {}
    public void reset() {}
    
    @Override
    public String toString() {
        return label;
    }
    
    public Icon getIcon() {
        return icon;
    }
}

