/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboCrossover;

import java.util.ArrayList;

/**
 *
 * @author CY
 */
public class Trade implements Comparable<Trade> {
    public int open = -1;
    public int close = -1;
    public String type;
    public double profit = -1;
    public String exitMethod;
    
    //stoploss and takeprofit is actual price
    double stopLoss = -1;
    double takeProfit = -1;
    
    public Trade(){}
    
    public Trade(int o, String t, double sl, double tp){
        open = o;
        
        if(t != "short" && t!= "long") 
             throw new AssertionError("Invalid trade, must be long or short");
        
        stopLoss = sl;
        takeProfit = tp;
        type = t;
    }
    
    public int compareTo(Trade b){
        if (profit < b.profit) return -1;
        if (profit > b.profit) return 1;
        return 0;
    }
}
