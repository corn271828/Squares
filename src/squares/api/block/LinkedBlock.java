/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.api.block;

import javax.swing.ImageIcon;

import squares.api.Coordinate;

/**
 *
 * @author piercelai
 * @param <T>
 */
public interface LinkedBlock extends Block{
    void linkTo(LinkableBlock block);
    int getTarget();
}
