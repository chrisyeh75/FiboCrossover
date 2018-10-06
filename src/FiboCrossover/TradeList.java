/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboCrossover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author CY
 */
public class TradeList {
    ArrayList<Trade> tl;
    Prices p;
    public double spread = -1;
    boolean isInLong = false;
    boolean isInShort = false;
    
    
    //for graphing
    ArrayList<Double> ema5;
    ArrayList<Double> sma8;
    ArrayList<Double> sma20;
    
    
    public ArrayList<Double> byMonth(){
        int i = 1;
        double sum = tl.get(0).profit;
        ArrayList<Double> monthlyProfit = new ArrayList<>();
        while(i < tl.size()){
            while((i < tl.size()) && (p.t(tl.get(i).close).getMonthValue() == p.t(tl.get(i-1).close).getMonthValue())){
                sum += tl.get(i).profit;
                ++i;
            }
            monthlyProfit.add(sum);
            if(i >= tl.size()) break;
            sum = tl.get(i).profit;
            ++i;            
        }
        
        return monthlyProfit;
    }
    
    public double calcCrit(){
        ArrayList<Double> monthlyProfit = byMonth();
        /*
        if(monthlyProfit.size() != 12)
            throw new AssertionError("Calculate monthly profit wrong, not 12 months, only " + monthlyProfit.size() + " months.");*/

        //calculate average
        double sum = 0;
        for(double p:monthlyProfit)
            sum += p;
        double avg = sum/12;
        
        //calculate std
        sum = 0;
        for(double p:monthlyProfit)
            sum += Math.pow((p - avg), 2);
        double std = Math.sqrt(sum/12);

        return avg-std;
    }
    
    public void produceGraphData(String mode, int number){
        ArrayList<Trade> tl2 = tl;
        if (mode == "sorted")
            Collections.sort(tl2);
            
        else if (mode == "worst"){
            Collections.sort(tl2);
            tl2 = new ArrayList<Trade>(tl2.subList(0, number));
        }
        
        else if (mode == "best"){
            Collections.sort(tl2);
            Collections.reverse(tl2);
            tl2 = new ArrayList<Trade>(tl2.subList(0, number));
        }
        
        for(Trade t:tl2){
            System.out.printf("---- %d   %d  %.7f %s/ %s %s  %d%n", t.open, t.close, t.profit, t.type, t.exitMethod, p.t(t.open).getDayOfWeek(), p.t(t.open).getHour());
            
            for(int i = t.open - 25; i <= t.close + 25; ++i){
                if(i > p.size()-1) break;
                
            if(i >= t.open && i <= t.close)
                System.out.printf("%d   %.7f %.7f    %.7f    %.7f    %.7f    %.7f    %.7f   %.7f    %.7f%n", i, p.o(i), p.h(i), p.l(i), p.c(i), 
                    ema5.get(i), sma8.get(i), sma20.get(i), t.takeProfit, t.stopLoss);               
                
            else
            System.out.printf("%d   %.7f %.7f    %.7f    %.7f    %.7f    %.7f    %.7f%n", i, p.o(i), p.h(i), p.l(i), p.c(i), 
                    ema5.get(i), sma8.get(i), sma20.get(i));
            }
        }
        
    }
    
    public void produceGraphOf(ArrayList<Integer> index){
        
        
        for(int j = 0; j < index.size(); ++j){
            Trade t = tl.get(index.get(j));
            System.out.printf("---- %d   %d  %.7f %s/ %s %s  %d%n", t.open, t.close, t.profit, t.type, t.exitMethod, p.t(t.open).getDayOfWeek(), p.t(t.open).getHour());
            
            for(int i = t.open - 25; i <= t.close + 25; ++i){
                if(i > p.size()-1) break;
                
            if(i >= t.open && i <= t.close)
                System.out.printf("%d   %.7f %.7f    %.7f    %.7f    %.7f    %.7f    %.7f   %.7f    %.7f%n", i, p.o(i), p.h(i), p.l(i), p.c(i), 
                    ema5.get(i), sma8.get(i), sma20.get(i), t.takeProfit, t.stopLoss);               
                
            else
            System.out.printf("%d   %.7f %.7f    %.7f    %.7f    %.7f    %.7f    %.7f%n", i, p.o(i), p.h(i), p.l(i), p.c(i), 
                    ema5.get(i), sma8.get(i), sma20.get(i));
            }
        }
        
    }
    
    public boolean inTrade(){
        return (isInLong || isInShort);
    }
    
    public TradeList(Prices p_, double s, ArrayList<Double> e5, ArrayList<Double> s8, ArrayList<Double> s20){
        p = p_;
        spread = s;
        tl = new ArrayList<>();
        ema5 = e5;
        sma8 = s8;
        sma20 = s20;
    }
    
    public void addTrade(int o, String t, double sl, double tp){
        if(isInLong || isInShort)
            throw new AssertionError("Trade already opened.");
        tl.add(new Trade(o, t, sl, tp));
        if(t == "long")
            isInLong = true;
        if(t == "short")
            isInShort = true;
    }
    
    public double getStartPrice(){
        return p.c(tl.get(tl.size()-1).open);
    }
    
    public void exitTrade(int c, String exitMethod, double exitPrice){
        tl.get(tl.size()-1).close = c;
        
        if(tl.get(tl.size()-1).type == "long"){
            tl.get(tl.size()-1).profit = exitPrice - p.c(tl.get(tl.size()-1).open) - spread;
        }
        
        if(tl.get(tl.size()-1).type == "short"){
            tl.get(tl.size()-1).profit = p.c(tl.get(tl.size()-1).open) - exitPrice - spread;
        }
        
        tl.get(tl.size()-1).exitMethod = exitMethod;
        isInLong = false; isInShort = false;
    }
    
    public void checkExit(int i){
        if(tl.get(tl.size()-1).type == "long"){
            if(p.l(i) <= tl.get(tl.size()-1).stopLoss)
                exitTrade(i, "stop loss", tl.get(tl.size()-1).stopLoss);
            else if(p.h(i) >= tl.get(tl.size()-1).takeProfit)
                exitTrade(i, "take profit", tl.get(tl.size()-1).takeProfit);
        }
        
        else if(tl.get(tl.size()-1).type == "short"){
            if(p.h(i) >= tl.get(tl.size()-1).stopLoss)
                exitTrade(i, "stop loss", tl.get(tl.size()-1).stopLoss);
            else if (p.l(i) <= tl.get(tl.size()-1).takeProfit)
                exitTrade(i, "take profit", tl.get(tl.size()-1).takeProfit);
        }
    }
    
        
    public ArrayList<Double> profitList(){
        ArrayList<Double> pl = new ArrayList<>();
        for(Trade t:tl)
            pl.add(t.profit);
        return pl;
    }
    
    public int size(){
        return profitList().size();
    }
    
    public double winChance(){
        return (double)winningTrades().size()/profitList().size()*100;
    }
    
    public ArrayList<Double> winningTrades(){
        ArrayList<Double> wt = new ArrayList<>();
        for(Trade t:tl){
            if(t.profit > 0)
                wt.add(t.profit);
        }
        return wt;
    }
    
    
    public void finalize(int at){
        if(!tl.isEmpty())
            if(tl.get(tl.size()-1).close == -1){
                exitTrade(at, "data end", p.c(at));
            }
    }
    
    public void printProfit(){
        System.out.printf("Number of trades: %d%n", tl.size());
        for(Trade t:tl)
            System.out.printf("%s  %f%n", p.t(t.close).plusHours(7), t.profit);
    }
    
    public double calcAvg(){
        double sum = 0;
        for(Trade t:tl)
            sum = sum+t.profit;
        return sum/tl.size();
    }
    
    public double calcCum(){
        double sum = 0;
        for(Trade t:tl)
            sum = sum+t.profit;
        return sum;
    }
    
    public double calcGrossProfit(){
        double sum = 0;
        for(Trade t:tl){
            if (t.profit > 0)
                sum += t.profit;
        }
        return sum;
    }
    
    public double calcGrossLoss(){
        double sum = 0;
        for(Trade t:tl){
            if (t.profit < 0)
                sum += t.profit;
        }
        return sum*-1;
    }
    
    public double calcGrossWinLossRatio(){
        return calcGrossProfit()/calcGrossLoss();
    }
    
    public double calcStd(){
        double avg = calcAvg();
        double sum = 0;
        for(Trade t:tl)
            sum = sum + Math.pow((t.profit - avg), 2);
        return Math.sqrt(sum/tl.size());
    }
    

    
    public void removeOutliers(){
        double avg = calcAvg(), std = calcStd();
        tl.removeIf( i -> {
      return i.profit > avg+std*3;
    });
    }
    
    public void sort(){
        Collections.sort(tl);
    }
    
    public int losingMonths(){
        ArrayList<Double> monthly = byMonth();
        int count = 0;
        for(int i = 0; i < monthly.size(); ++i){
            if (monthly.get(i) < 0) ++count;
        }
        return count;
    }
    
    public void printStats(){
        System.out.printf("Average:%.7f, Stdev:%.7f, Total:%.7f, Critical:%.7f, Profit per day:%.7f\n", calcAvg(), calcStd(), calcCum(), calcCrit(), calcCum()/365);
        ArrayList<Trade> tl2 = new ArrayList<>();
        for(Trade t:tl){
            tl2.add(t);
        }
        Collections.sort(tl2);
        int winningTrades = winningTrades().size();
        int losingTrades = tl2.size()-winningTrades;
        
        
        System.out.printf("Losing trades: %d, %.2f%%          winning trades: %d, %.2f%%%n", losingTrades, (double)losingTrades/tl2.size()*100, winningTrades, (double)winningTrades/tl2.size()*100);
        System.out.printf("Gross profit: %.7f   Gross loss: %.7f    WinLossRatio: %.7f%n", calcGrossProfit(), calcGrossLoss(), calcGrossWinLossRatio());
        System.out.printf("Losing months; %d%n", losingMonths());
        
        
        
        System.out.println("by month:");
        ArrayList<Double> monthly = byMonth();
        for(Double k:monthly)
            System.out.printf("%.7f\t", k);
        System.out.println();
        
        System.out.println("Worst:");
        for(int i = 0; i < 10; ++i)
            System.out.print(String.format("%.7f\t", tl2.get(i).profit));
        System.out.println("\nBest:");
        for(int i = tl2.size()-20; i < tl2.size(); ++i)
            System.out.print(String.format("%.7f\t", tl2.get(i).profit));
        System.out.println();
            
    }
    
    
    public void profitHistogram(){
        Plot.graphHistogram(profitList());
    }
    
    
    public void winningHistogram(){
        Plot.graphHistogram(winningTrades());
    }
    
    public void graphProfit(String mode, int number){
        int index;
        ArrayList<Trade> tr;
        ArrayList<Trade> tl2 = new ArrayList<>();
        for(Trade t:tl){
            tl2.add(t);
        }
        if(mode == "sample") {
            index = ThreadLocalRandom.current().nextInt(0, tl.size()-number);
            tr = new ArrayList<Trade>(tl.subList(index, index+number));
        }
        else if (mode == "specific")
            tr = new ArrayList<Trade>(tl.subList(number, number+1));
        else if(mode == "worst"){
            Collections.sort(tl2);
            tr = new ArrayList<Trade>(tl2.subList(0, number));
        }
        else
            throw new AssertionError("Mood error.");
 
        int start, end, extraRange;
        ArrayList<XYDataset> tops = new ArrayList<>();
        ArrayList<XYDataset> bots = new ArrayList<>();
        ArrayList<String> datenprofits = new ArrayList<>();
    for(Trade t:tr) {
            XYSeriesCollection topchart = new XYSeriesCollection();
            XYSeriesCollection bottomchart = new XYSeriesCollection();
            
            if(t.open-t.close < 10)
                extraRange = 15;
            else extraRange = 5;
            start = t.open - extraRange;
            end = t.close + extraRange;
            
            XYSeries ema5_ = new XYSeries("ema5");
            XYSeries ema10_ = new XYSeries("ema10");
            XYSeries price_ = new XYSeries("price");
            XYSeries rsi_ = new XYSeries("rsi");
            
            XYSeries tradeStart = new XYSeries("start");
            XYSeries tradeEnd = new XYSeries("end");
            XYSeries tradeStartr = new XYSeries("start r");
            XYSeries tradeEndr = new XYSeries("end r");
            XYSeries fiftyLine = new XYSeries("fifty line");
            
            
            for(int i = start; i <= end; ++i){
                ema5_.add((double) i, ema5.get(i));
                //ema10_.add((double) i, ema10.get(i));
                price_.add((double) i, p.c(i));
                //rsi_.add((double) i, rsi.get(i));
            }
                tradeStart.add(t.open, p.c(t.open)-.003);
                tradeStart.add(t.open, p.c(t.open)+.003);
                
                tradeEnd.add(t.close, p.c(t.close)-.003);
                tradeEnd.add(t.close, p.c(t.close)+.003);
            
                tradeStartr.add(t.open, 0);
                tradeStartr.add(t.open, 100);
                tradeEndr.add(t.close, 0);
                tradeEndr.add(t.close, 100);
                fiftyLine.add(start, 50);
                fiftyLine.add(end, 50);
                
            
            topchart.addSeries(ema5_);
            topchart.addSeries(ema10_);
            topchart.addSeries(price_);
            topchart.addSeries(tradeStart);
            topchart.addSeries(tradeEnd);

            bottomchart.addSeries(rsi_);
            bottomchart.addSeries(tradeStartr);
            bottomchart.addSeries(tradeEndr);
            bottomchart.addSeries(fiftyLine);
            
        tops.add(topchart);
        bots.add(bottomchart);
        datenprofits.add(String.format("id:%d, %s, %s, p:%.2f", tl.indexOf(t), p.t(t.close), t.type, t.profit*10000));
        }
        
        
        Plot.emaRsi(tops, bots, datenprofits);
    }
        
        
}
