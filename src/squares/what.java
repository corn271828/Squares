
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author piercelai
 */
public class what {
    
    public static void main (String args[]) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("cave.out")));
        pw.println("1234");
        pw.close();
    }
    
}
