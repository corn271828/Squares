/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import squares.Player;
import squares.api.CharacterState;
import squares.api.Coordinate;
import squares.api.ResourceLoader;
import squares.api.block.Block;
import squares.api.block.LinkableBlock;
import squares.api.block.LinkedBlock;

import static squares.api.RenderingConstants.PIXELS_PER_INCH;
import static squares.api.RenderingConstants.STANDARD_ICON_WIDTH;

/**
 *
 * @author piercelai
 */
public class ButtonBlock extends BaseBlock implements LinkedBlock {
    
    boolean pressed = false;
    private static ImageIcon[] buttonimages = new ImageIcon[6]; // pu, pp, ou, op, gu, gp
    private static ImageIcon[] buttonLinkedImages = new ImageIcon[6]; // same
    static {
        try {
            BufferedImage all = ImageIO.read(new ResourceLoader("sprites", "buttons").asInputStream("png"));
            for (int i = 0; i < 3; i++) {
                buttonimages[2 * i] = new ImageIcon(all.getSubimage(i * PIXELS_PER_INCH - (i == 0 ? 0 : 1), 0, STANDARD_ICON_WIDTH, STANDARD_ICON_WIDTH));
                buttonimages[2 * i + 1] = new ImageIcon(all.getSubimage(i * PIXELS_PER_INCH - (i == 0 ? 0 : 1), PIXELS_PER_INCH - 1, STANDARD_ICON_WIDTH, STANDARD_ICON_WIDTH));
                buttonLinkedImages[2 * i] = new ImageIcon(all.getSubimage(i * PIXELS_PER_INCH - (i == 0 ? 0 : 1), 2 * PIXELS_PER_INCH - 1, STANDARD_ICON_WIDTH, STANDARD_ICON_WIDTH));
                buttonLinkedImages[2 * i + 1] = new ImageIcon(all.getSubimage(i * PIXELS_PER_INCH - (i == 0 ? 0 : 1), 3 * PIXELS_PER_INCH - 1, STANDARD_ICON_WIDTH, STANDARD_ICON_WIDTH));
            }
        } catch (IOException ex) {
            Logger.getLogger(ButtonBlock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private Coordinate target;
    private LinkableBlock link;

    public ButtonBlock(int x, int y) {
        super(null, "Button Block");
        target = new Coordinate(x, y);
    }

    @Override
    public Coordinate getTarget() {
        return target;
    }
    @Override
    public void linkTo(LinkableBlock lb) {
        link = lb;
    }
    
    @Override
    public void onLand(Player player) {
        player.charState = CharacterState.NORMAL;
        if (!pressed) {
            pressed = true;
            this.icon = buttonimages[link.getLinkIndex() * 2 + 1];
            link.onLinkedBlockChange();
        }
    }
    
    @Override
    public boolean canStep() {
        return true;
    }

    @Override
    public void reset() {
        pressed = false;
        this.icon = buttonimages[link.getLinkIndex() * 2];
    }
    
    public static class ButtonLinkedBlock extends BaseBlock implements LinkableBlock {

        private int index;
        private boolean steppable = false;
        
        public ButtonLinkedBlock(int ind) {
            super(buttonLinkedImages[ind * 2], "Button Linked Block");
            index = ind;
        }
        
        @Override
        public int getLinkIndex() { return index; }
        @Override
        public void onLinkedBlockChange() {
            icon = buttonLinkedImages[index * 2 + 1];
            steppable = true;
        }
        
        @Override
        public void onLand(Player player) {
            player.charState = CharacterState.NORMAL;
        }
        
        @Override
        public boolean canStep() {
            return steppable;
        }
        
        @Override
        public void reset() {
            steppable = false;
            icon = buttonLinkedImages[index * 2];
        }
        
        public int getIndex() {
            return index;
        }
    }
    
}
