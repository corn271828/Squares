/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Block;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import squares.Level;

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

