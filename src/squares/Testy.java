/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author piercelai
 */
public class Testy {
    
    public static void main (String args[]) {
        PriorityQueue<Integer> p = new PriorityQueue<>();
        p.add(1);
        p.add(3);
        p.add(7);
        p.add(2);
        p.add(3);
        p.add(6);
        System.out.println(p);
        p.poll();
        System.out.println(p);
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate ldt = LocalDate.parse("10/25/2019", dtf);
        LocalDate dt2 = ldt.plusDays(1000);
        System.out.println(ldt.toString());
        System.out.println(dtf.format(dt2));
        Period joe = Period.between(ldt, dt2);
        System.out.println(joe.getYears() + " " + joe.getMonths() + " " + joe.getDays());
        System.out.println(ldt.isBefore(dt2));
        
    }
    
    
}
