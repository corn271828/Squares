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
public class LauncherBlock extends Block {

    protected static final ImageIcon launcherBlockRight = new ImageIcon("Pics/Launcher Block Right.png", "Launcher Block Image");
    protected static final ImageIcon launcherBlockLeft = new ImageIcon("Pics/Launcher Block Left.png", "Launcher Block Image");
    protected static final ImageIcon launcherBlockDown = new ImageIcon("Pics/Launcher Block Down.png", "Launcher Block Image");
    protected static final ImageIcon launcherBlockUp = new ImageIcon("Pics/Launcher Block Up.png", "Launcher Block Image");
    protected Block.Direction direction;

    public LauncherBlock(Block.Direction direction) {
        this.direction = direction;
        stepable = true;
        label = "Launcher Block";
        switch (direction) {
            case UP: icon = launcherBlockUp; break;
            case DOWN: icon = launcherBlockDown; break;
            case LEFT: icon = launcherBlockLeft; break;
            case RIGHT: icon = launcherBlockRight; break;
        }
    }

    @Override
    public void refreshIcon() {

    }

    @Override
    public void reset() {

    }

    public Block.Direction getDirection() {
        return direction;
    }
}
