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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import squares.Player;
import squares.api.CharacterState;
import squares.api.Clock;
import static squares.api.RenderingConstants.PIXELS_PER_INCH;
import static squares.api.RenderingConstants.STANDARD_ICON_WIDTH;
import squares.api.ResourceLoader;

/**
 *
 * @author piercelai
 */
public class ShankerBlock extends BaseBlock {
    
    private int period;
    private int delay = 4;
    private static final int WARNING_TIME = 6;
    private static final int OUCH_TIME = 4;
    private static final ImageIcon[] shankerPics = new ImageIcon[3];
    private static Clock time;
    static {
        try {
            BufferedImage all = ImageIO.read(new ResourceLoader("sprites/block", "shanker").asInputStream("png"));
            for (int i = 0; i < 3; i++) {
                shankerPics[i] = new ImageIcon(all.getSubimage(0, i * PIXELS_PER_INCH - (i == 0 ? 0 : 1), STANDARD_ICON_WIDTH, STANDARD_ICON_WIDTH));
            }
        } catch (IOException ex) {
            Logger.getLogger(ButtonBlock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ShankerBlock(String inp) {
        super(shankerPics[0], "Shanker Block");
        period = Integer.parseInt(inp.substring(0, 2));
        if (inp.length() > 2)
            delay = Integer.parseInt(inp.substring(2, 4));
        else
            delay = period - OUCH_TIME - 1;
    }

    public ShankerBlock(int per, int del) {
        super(shankerPics[0], "Shanker Block");
        period = per;
        del = delay;
    }
    
    @Override
    public boolean canStep() {
        return true;
    }

    public static void setTime(Clock time) {
        ShankerBlock.time = time;
    }
    
    @Override
    public void onLand(Player player) {
        player.charState = CharacterState.NORMAL;
    }
    
    @Override
    public boolean isOuch() {
        int t =  time.time() % period;
        if (t < delay)
            return  t + period < delay + OUCH_TIME;
        return t < delay + OUCH_TIME;
    }
    
    @Override
    public Icon getIcon() {
        int t =  time.time() % period;
        if (t < delay)
            if (t + period < delay + OUCH_TIME)
                return shankerPics[2];
            else if (t > delay - WARNING_TIME)
                return shankerPics[1];
            else
                return shankerPics[0];
        if (t < delay + OUCH_TIME)
            return shankerPics[2];
        if (t > delay + period - WARNING_TIME)
            return shankerPics[1];
        return shankerPics[0];
    }
    
}

