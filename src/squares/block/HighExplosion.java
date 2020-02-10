/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.block;

import squares.level.BaseLevel;

/**
 *
 * @author piercelai
 */

public interface HighExplosion {
    public BaseLevel.LineExploder getLineExplosion(int startime, double ang, int starx, int stary);
}
