/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboCrossover;

import static FiboCrossover.FiboCrossover.symbol;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author CY
 */
public class Prices implements Serializable {
        public ArrayList<Candle> prices;
        
        
    public static double highestIn (Prices p, int start, int end){
        ArrayList<Double> prevMax = new ArrayList<>();
                for(int j = start+1; j <= end; ++j){
                    prevMax.add(p.h(j));
                }
                
                return Collections.max(prevMax, null);
    }    
    
    public static double lowestIn (Prices p, int start, int end){
        ArrayList<Double> prevLow = new ArrayList<>();
            for(int j = start+1; j <= end; ++j){
                prevLow.add(p.l(j));
            }
            
            return Collections.min(prevLow, null);
    }
    
    public static Prices readParsedSer (String filename) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
	Prices k = (Prices) ois.readObject();
        ois.close();
        return k;
    }
    
    public ArrayList<Double> creatList (String type){
        ArrayList<Double> gen = new ArrayList<>();
        for (Candle p:prices){
            switch(type){             
                case "close": gen.add(p.close);
                    break;
                case "median": gen.add((p.high + p.low)/2);
                    break;
                case "open": gen.add(p.open);
                    break;
                case "high": gen.add(p.high);
                    break;
                case "low": gen.add(p.low);
                    break;
                default: System.out.println("Error in create list, input invalid");
                    break;
            }
        }
        return gen;
    }
    
    public ArrayList<LocalDateTime> createTimeList(){
        ArrayList<LocalDateTime> gen = new ArrayList<>();
        for(Candle c:prices)
            gen.add(c.time);
        return gen;
    }
        
    public static Prices readParsedFile (String filename, String year) throws Exception {
        File file = new File(filename);
        Scanner input = new Scanner(file);
        input.useDelimiter("  |\\n");
        
        ArrayList<Candle> p = new ArrayList<>();
        LocalDateTime dt; double o; double h; double l; double c;
        while (input.hasNext()) {
            String date = input.next();
                if(!date.substring(0, 4).equals(year)) {
                    System.out.println("Error   " + date);
                    break;
                }
            dt = (LocalDateTime.parse(date));
            o = Double.parseDouble(input.next()); 
            h = Double.parseDouble(input.next()); 
            l = Double.parseDouble(input.next()); 
            c = Double.parseDouble(input.next()); 
            p.add(new Candle(dt, o, h, l, c));
        }
        
        return new Prices(p);
    }
    
    //shrinks 1m data to whatever period specified
    public Prices oneMshrinkTo(int period){
        int counter = 0, current = 0, curlimit = 7200;
        ArrayList<Candle> gen = new ArrayList<>();
        while(current < prices.size()){
            if((7200 - (current % 7200)) < period && (current % 7200) != 0){
                current = current + (7200 - (current % 7200));
                if(current >= prices.size()) break;
            }
            //close = current+period-1
            //open = current
            //time = current
        double max = 0; double min = 999999999;
        for(int i = current; i < current+period; ++i){
            if(prices.get(i).high > max)
                max = prices.get(i).high;
            if(prices.get(i).low < min)
                min = prices.get(i).low;
        }
        gen.add(new Candle(prices.get(current).time, prices.get(current).open, max, min, prices.get(current+period-1).close));
            current = current + period;
        }
        return new Prices(gen);
    }
    
    
    
    
    
    
    
    public int size() {
        return prices.size();
    }
    
    public Prices subList (int start, int end){
        return new Prices(new ArrayList<Candle>(prices.subList(start, end)));
    }
    
    public int indexOfTime(LocalDateTime t){
        int counter = 0;
            while(counter < prices.size()){
                if(t.equals(prices.get(counter).time))
                    return counter;
                ++counter;
            }
        return -1;
    }
    
    public Prices(ArrayList<Candle> p){
        prices = p;
    }
    
    public LocalDateTime t(int index){
        return prices.get(index).time;
    }
    
    public double o(int index){
        return prices.get(index).open;
    }
    
    public double h(int index){
        return prices.get(index).high;
    }
    
    public double l(int index){
        return prices.get(index).low;
    }
    
    public double c(int index){
        return prices.get(index).close;
    }
    
    public double m(int index){
        return (prices.get(index).high + prices.get(index).low)/2;
    }
    public void printIndex(int index){
        prices.get(index).print();
    }
}
