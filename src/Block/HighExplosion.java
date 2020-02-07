/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Block;

import squares.Level;

/**
 *
 * @author piercelai
 */
    
public interface HighExplosion {
    public Level.BossLevel.LineExploder getLineExplosion(int startime, double ang, int starx, int stary);
}
