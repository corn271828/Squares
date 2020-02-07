/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Blocks;

import javax.swing.Icon;

/**
 *
 * @author lai_889937
 */
public abstract class Block {
    protected Icon icon;
    protected String label;
    protected boolean stepable; // Represents whether or not one can step on this type of block
    public abstract void refreshIcon();
    public abstract void reset();
    
    @Override
    public String toString() {
        return label;
    }
    
    public static enum Direction {
        UP, DOWN, RIGHT, LEFT;
    }
    
    public Icon getIcon() {
        return icon;
    }
    
    public boolean getStepable() {
        return stepable;
    }
}

