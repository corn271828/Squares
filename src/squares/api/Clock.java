/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.api;

/**
 *
 * @author piercelai
 */
public class Clock {
    private int timestamp;
    
    public Clock() {
        timestamp = 0;
    }
    
    public int getTimestamp() {
        return timestamp;
    }
    
    public int increment() {
        return ++timestamp;
    }
    
    public void reset() {
        timestamp = 0;
    }
    
    public void setTime(int t) {
        timestamp = t;
    }
}
