/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Block;

import javax.swing.ImageIcon;

/**
 *
 * @author piercelai
 */
public class NormalBlock extends Block {

    public static final ImageIcon normalBlockIcon = new ImageIcon("Pics/Normal Block.png", "Normal Block Image");

    public NormalBlock() {
        stepable = true;
        label = "Normal Block";
        icon = normalBlockIcon;
    }

    @Override
    public void refreshIcon() {

    }

    @Override
    public void reset() {

    }

}
