/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.api.block;

import squares.api.Coordinate;

/**
 *
 * @author piercelai
 */
public interface Resettable {
    
    public void resetToOrigin();
    
    public void setOrigin();
    
    public void updateToPanel(Coordinate render, Coordinate start);
    
}
