/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import javax.swing.ImageIcon;

import squares.api.Direction;
import squares.api.DirectedBlock;

/**
 *
 * @author piercelai
 */
public class LauncherBlock extends Block implements DirectedBlock {

    protected static final ImageIcon launcherBlockRight = new ImageIcon("Pics/Launcher Block Right.png", "Launcher Block Image");
    protected static final ImageIcon launcherBlockLeft = new ImageIcon("Pics/Launcher Block Left.png", "Launcher Block Image");
    protected static final ImageIcon launcherBlockDown = new ImageIcon("Pics/Launcher Block Down.png", "Launcher Block Image");
    protected static final ImageIcon launcherBlockUp = new ImageIcon("Pics/Launcher Block Up.png", "Launcher Block Image");
    protected Direction direction;

    public LauncherBlock(Direction direction) {
        super(getIconByDirection(direction), "Launcher Block", true);
    }

    private static ImageIcon getIconByDirection(Direction d) {
        switch (d) {
            case UP:    return launcherBlockUp;
            case DOWN:  return launcherBlockDown;
            case LEFT:  return launcherBlockLeft;
            case RIGHT: return launcherBlockRight;
        }
        return null;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
