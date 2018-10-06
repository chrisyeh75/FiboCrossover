/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboCrossover;

import java.util.EmptyStackException;

/**
 *
 * @author CY
 */
public class Spread {
    /*spread table
    currency    average         stdev           avg+std(input)  convert to usd
    EURUSD	0.00004869 	0.00003671 	0.00008540 	0.0000854 
    USDJPY	0.006371 	0.003899 	0.01027047 	0.0000906 
    AUDUSD	0.00006223 	0.00003882 	0.00010105 	0.0001010 
    USDCAD	0.00010587 	0.00006905 	0.00017491 	0.0001276 
    CADJPY	0.011064 	0.007714 	0.01877784 	0.0001657 
    NZDUSD	0.00011045 	0.00007053 	0.00018098 	0.0001810 
    EURJPY	0.012223 	0.008293 	0.02051611 	0.0001810 
    EURCAD	0.00016218 	0.00009396 	0.00025614 	0.0001868 
    USDCHF	0.00010972 	0.00009941 	0.00020913 	0.0002089 
    GBPUSD	0.00012733 	0.00008725 	0.00021458 	0.0002146 
    EURGBP	0.00009405 	0.00008522 	0.00017927 	0.0002310 
    EURCHF	0.00013087 	0.00013175 	0.00026262 	0.0002623 
    NZDJPY	0.017677 	0.013700 	0.03137674 	0.0002768 
    */
    
    public static double get (String currency){
        switch (currency){
           case "EURUSD" : return	0.00008540;
            case "USDJPY" : return	0.010270;
            case "AUDUSD" : return	0.00010105;
            case "USDCAD" : return	0.00017491;
            case "CADJPY" : return	0.018777;
            case "NZDUSD" : return	0.00018098;
            case "EURJPY" : return	0.020516;
            case "EURCAD" : return	0.00025614;
            case "USDCHF" : return	0.00020913;
            case "GBPUSD" : return	0.00021458;
            case "EURGBP" : return	0.00017927;
            case "EURCHF" : return	0.00026262;
            case "NZDJPY" : return	0.031376;
            default: throw new EmptyStackException();
        }
    }
    
    public static double conFact(String currency){
        String base = currency.substring(3, 6);
        switch (base){
            case "USD" : return 1;
            case "JPY" : return 1.13334;
            case "CAD" : return 1.37112;
            case "CHF" : return 1.00108;
            case "GBP" : return 0.776033;
            default: throw new EmptyStackException();
        }
    }
    
    public static double conFactNoPip (String currency){
        String base = currency.substring(3, 6);
        switch (base){
            case "USD" : return 1;
            case "JPY" : return 113.334;
            case "CAD" : return 1.37112;
            case "CHF" : return 1.00108;
            case "GBP" : return 0.776033;
            default: throw new EmptyStackException();
            
        }
    }
    
}
