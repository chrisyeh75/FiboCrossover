/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboCrossover;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author CY
 */
public class Inputs {
    public int period;
    public int ema;
    public int fsma;
    public int ssma;
    public double abs;
    
    public Inputs(int p, int e, int f, int s, double a){
        period = p; ema = e; fsma = f; ssma = s; abs = a;
    }
    
    static public ArrayList<Inputs> readIn(String fileName) throws Exception{
        ArrayList<Inputs> ins = new ArrayList<>();
        File file = new File(fileName);
        Scanner input = new Scanner(file);
        
        int p, e, f, s; double a;
        while (input.hasNext()) {
            input.next();
            p = Integer.parseInt(input.next());
            input.next();
            e = Integer.parseInt(input.next());
            input.next();
            f = Integer.parseInt(input.next());
            input.next();
            s = Integer.parseInt(input.next());
            input.next();
            a = Double.parseDouble(input.next());
            input.next(); input.next(); input.next(); input.next();
            input.next(); input.next(); input.next(); input.next();
            
            
            ins.add(new Inputs(p, e, f, s, a));
        }
        
        return ins;
        
    }
    
}
