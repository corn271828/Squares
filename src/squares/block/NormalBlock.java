/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import javax.swing.ImageIcon;

/**
 *
 * @author piercelai
 */
public class NormalBlock extends Block {

    public static final ImageIcon normalBlockIcon = new ImageIcon("Pics/Normal Block.png", "Normal Block Image");

    public NormalBlock() {
        super(normalBlockIcon, "Normal Block", true);
    }

}