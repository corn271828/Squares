/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.blocks;

import javax.swing.ImageIcon;

import squares.api.Direction;
import squares.api.DirectedBlock;

/**
 *
 * @author piercelai
 */
public class LauncherBlock extends Block implements DirectedBlock {

    private static final ImageIcon[] launcherBlocks = new ImageIcon[Direction.values().length];
    protected Direction direction;

    public LauncherBlock(Direction direction) {
        super(getIconByDirection(direction), "Launcher Block", true);
    }

    private static ImageIcon getIconByDirection(Direction d) {
	int i = d.ordinal();
	if(launcherBlocks[i] == null)
		launcherBlocks[i] = new ImageIcon(String.format("Pics/Launcher Block %s.png", d.name), "Launcher Block Image");
        return launcherBlocks[i];
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
