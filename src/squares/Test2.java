/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares;

/**
 *
 * @author piercelai
 */
public class Test2 extends Test1 {
    int bob;
    
    public Test2(int j, int b) {
        joe = j;
        bob = b;
    }
    
    public static void main(String args[]) {
        Test1 t1 = new Test2(1, 2);
        System.out.println(t1.joe);
    }
}
