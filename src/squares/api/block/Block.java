/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.api.block;

import javax.swing.Icon;
import squares.api.Clock;

/**
 *
 * @author lai_889937
 */
public interface Block {
    boolean refreshIcon();

    void reset();

    Icon getIcon();
    boolean canStep();
    
    void onLand(squares.Player player);
    
    boolean isOuch();
}

