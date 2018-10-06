/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboCrossover;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author CY
 */
public class Candle implements Serializable {
        public LocalDateTime time;
    public double open;
    public double high;
    public double low;
    public double close;
    
    public Candle(LocalDateTime t, double o, double h, double l, double c){
        time = t; open = o; high = h; low = l; close = c;
    }
    
    public boolean equals (Candle b){
        if(time.equals(b.time) && open == b.open && high == b.high && low == b.low && close == b.close)
            return true;
        return false;
    }
    
    public void print(){
        System.out.printf("%s   open:%f     high:%f     low:%f      close:%f%n", time, open, high, low, close);
    }
}
