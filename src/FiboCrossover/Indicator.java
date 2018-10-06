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
public class Indicator {
        //consider recalculating for each week
    public static ArrayList<Double> calcEma (ArrayList<Double> price, int period){
        double c = 2/((double)period + 1);
        ArrayList<Double> gen = new ArrayList<>();
        for(int i = 0; i<period; ++i){
            gen.add(price.get(i));
        }
        
        gen.add(Average(gen));
        gen.remove(gen.size()-2);
        for(int i = period; i < price.size(); ++i){
            gen.add(c * price.get(i) + (1-c) * gen.get(i-1));
        }
        return gen;
    }
    
    
    //level is by percent (eg. 161.8)
    //a is the closing price, b is the previous high
    public static double fibo (double a, double b, double level){
        return b + level/100*(a-b);
    }
    
    public static ArrayList<Double> calcSma (ArrayList<Double> price, int period){
        ArrayList<Double> gen = new ArrayList<>();
        for(int i = 0; i < period; ++i){
            gen.add(price.get(i));
        }
        gen.add(Average(gen));
        gen.remove(gen.size()-2);
        
        double sum = 0;
        for(int i = 0; i < period; ++i){
            sum += price.get(i);
        }
        
        for(int i = period; i < price.size(); ++i){
            sum = sum - price.get(i-period) + price.get(i);
            gen.add( sum / period );
        }
        
        return gen;
    }
    
    
    public static ArrayList<Double> calcRsi (ArrayList<Double> price, int period){
        ArrayList<Double> gen = new ArrayList<>();
        ArrayList<Double> avgGain = new ArrayList<>();
        ArrayList<Double> avgLoss = new ArrayList<>();
        ArrayList<Double> firstGain = new ArrayList<>();
        ArrayList<Double> firstLoss = new ArrayList<>();
        for(int i = 0; i<period; ++i){
            gen.add(50.0);
            avgGain.add(0.0);
            avgLoss.add(0.0);
        }
        
        firstGain.add(0.0); firstLoss.add(0.0);
        for(int i = 1; i<period; ++i){
            if(price.get(i)>=price.get(i-1)){
                firstGain.add(price.get(i)-price.get(i-1));
                firstLoss.add(0.0);
            }
            else{
                firstGain.add(0.0);
                firstLoss.add(price.get(i-1)-price.get(i));
            }
        }
        
        avgGain.add(Average(firstGain)); avgLoss.add(Average(firstLoss));
        gen.add(100 - 100/(1+(avgGain.get(period)/avgLoss.get(period))));
        for(int i = period+1; i<price.size(); ++i){
            if(price.get(i) >= price.get(i-1)) {
                avgGain.add( (avgGain.get(i-1)*(period-1) + (price.get(i)-price.get(i-1)))/period );
                avgLoss.add( (avgLoss.get(i-1)*(period-1) + 0)/ period );
            }
            else {
                avgGain.add( (avgGain.get(i-1)*(period-1) + 0)/period );
                avgLoss.add( (avgLoss.get(i-1)*(period-1) + (price.get(i-1) - price.get(i)))/period );
            }
            gen.add(100 - 100/(1+(avgGain.get(i)/avgLoss.get(i))));
        }
        
        return gen;
    }
    
    private static double Average(ArrayList <Double> list) {
        Double sum = 0.0;
        if(!list.isEmpty()) {
          for (Double item : list) {
              sum += item;
          }
          return sum / list.size();
        }
        return sum; 
        
        
      }
    
}
