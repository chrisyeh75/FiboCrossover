package FiboCrossover;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class FiboCrossover {

    final public static String symbol = "AUDUSD";
    public static double spread;
    public static String year = "2014";
    public static double pipvalue; //mini-pipvalue
    
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

    
    
    //timeframe has been optimized to 30min for EURUSD
    final public static int timeframe = 30;

    public static long t1, t2, t3, t4, t5, t6;
    //ema not accurate until max period (slowEma) + 2
    
    
    
    //rsi mechanisms has been determined to have negative effect
    public static void main(String[] args) throws Exception {
        
        
        if(symbol.indexOf("JPY") != -1)
            pipvalue = .01;
        else
            pipvalue = .0001;
        
        
        spread = Spread.get(symbol);
        
        //65 9 17 29 0.00015 successful in 2013-2016 not 2012
        
        /*
        Prices p = Prices.readParsedFile(symbol + "_"+ year + "_1m.dat", year);       
        //changes the time frame
        p = p.oneMshrinkTo(39);
        //create an arraylist for the close price
        ArrayList<Double> close = p.creatList("close");
        
        
        
        //97 19 17 18 0.0001 0.0058 165.8 135 15 close
        //48 9 23 37 0.000125 0.0058 165.8 135 15 close
        TradeList results = runWith(20, 39, 41, 0.56667, 58, 165.8, 135, 15, close, p);
        //212 162
        //results.produceGraphData("by candle size", 10);
        results.printStats();
        

        
        /*
        for(double sl = 0.00001; sl <= 0.03; sl += 0.00001){
                TradeList results = runWith(14, 19, 25, 0.000125, sl, 165.8, 135, 15, close, p);
                System.out.printf("%.4f     %.7f    %.2f    %d%n", sl, results.calcCum(), results.winChance(), results.size());
        }*/
        
        
        
        
        /*
        ArrayList<Double> rsi = Indicator.calcRsi(close, 10);
        boolean rsiCrossed; int i;
        for(Trade t:results.tl){
            i = t.open;
            rsiCrossed = false;
            if(t.type == "long") {
                if(rsi.get(i) > 50 && rsi.get(i-1) < rsi.get(i))
                    rsiCrossed = true; }
            if(t.type == "short") {
                if(rsi.get(i) < 50 && rsi.get(i-1) > rsi.get(i))
                    rsiCrossed = true;
            }
            
            System.out.printf("%d   %s  %.7f    %b%n", i, t.type, t.profit, rsiCrossed);
        }*/

        
        
        Prices p2009 = Prices.readParsedFile(symbol + "_2009_1m.dat", "2009");
        Prices p2010 = Prices.readParsedFile(symbol + "_2010_1m.dat", "2010");
        Prices p2011 = Prices.readParsedFile(symbol + "_2011_1m.dat", "2011");
        Prices p2012 = Prices.readParsedFile(symbol + "_2012_1m.dat", "2012");
        Prices p2013 = Prices.readParsedFile(symbol + "_2013_1m.dat", "2013");
        Prices p2014 = Prices.readParsedFile(symbol + "_2014_1m.dat", "2014");
        Prices p2015 = Prices.readParsedFile(symbol + "_2015_1m.dat", "2015");
        Prices p2016 = Prices.readParsedFile(symbol + "_2016_1m.dat", "2016");
        
        PrintWriter out = new PrintWriter(symbol + "_Optoutput_1.0.txt");
        ArrayList<Double> close09, close10, close11, close12, close13, close14, close15, close16;
        Prices ps09, ps10, ps11, ps12, ps13, ps14, ps15, ps16;
        for(int m = 30; m <= 165; ++m){
            ps09 = p2009.oneMshrinkTo(m);
            ps10 = p2010.oneMshrinkTo(m);
            ps11 = p2011.oneMshrinkTo(m);
            ps12 = p2012.oneMshrinkTo(m);
            ps13 = p2013.oneMshrinkTo(m);
            ps14 = p2014.oneMshrinkTo(m);
            ps15 = p2015.oneMshrinkTo(m);
            ps16 = p2016.oneMshrinkTo(m);
            
            close09 = ps09.creatList("close");
            close10 = ps10.creatList("close");
            close11 = ps11.creatList("close");
            close12 = ps12.creatList("close");
            close13 = ps13.creatList("close");
            close14 = ps14.creatList("close");
            close15 = ps15.creatList("close");
            close16 = ps16.creatList("close");
            
        for (int i = 5; i <= 40; ++i){
            System.out.printf("%d   %d%n", m, i);
            out.flush();
            for (int j = 6; j <= 40; ++j){
                for (int k = j + 1; k <= 50; ++k){
                    for (double l = 0; l <= 1.25 * Spread.conFact(symbol); l += 0.25 * Spread.conFact(symbol)) {
                TradeList results14 = runWith(i, j, k, l, 58, 165.8, 135, 15, close14, ps14);
                if(results14.calcCum() > 0.03 * Spread.conFactNoPip(symbol) && results14.size() > 100 && results14.winChance() > 60 && results14.calcCrit() > -0.01 * Spread.conFactNoPip(symbol) && results14.calcGrossWinLossRatio() > 1.2 && results14.losingMonths() < 4) {
                    TradeList results15 = runWith(i, j, k, l, 58, 165.8, 135, 15, close15, ps15);
                    if(results15.calcCum() > 0.03 * Spread.conFactNoPip(symbol) && results15.winChance() > 60 && results15.calcCrit() > -0.01 * Spread.conFactNoPip(symbol) && results15.calcGrossWinLossRatio() > 1.2 && results15.losingMonths() < 4) {
                        TradeList results09 = runWith(i, j, k, l, 58, 165.8, 135, 15, close09, ps09);
                        TradeList results10 = runWith(i, j, k, l, 58, 165.8, 135, 15, close10, ps10);
                        TradeList results11 = runWith(i, j, k, l, 58, 165.8, 135, 15, close11, ps11);
                        TradeList results12 = runWith(i, j, k, l, 58, 165.8, 135, 15, close12, ps12);
                        TradeList results13 = runWith(i, j, k, l, 58, 165.8, 135, 15, close13, ps13);
                        TradeList results16 = runWith(i, j, k, l, 58, 165.8, 135, 15, close16, ps16);
                        double crit14 = results14.calcCrit(), crit15 = results15.calcCrit();
                        out.printf("period %d    ema %d  fsma %d ssma %d  abs %.7f  profit %.7f winchance %.2f  crit: %.7f  gw: %.7f    gl: %.7f    gwlr: %.4f  losingMonths: %d    trades %d   2009: %.7f  2010 %.7f   2011 %.7f   2012 %.7f   2013: %.7f  2016: %.7f%n",
                            m, i, j, k, l, results14.calcCum(), results14.winChance(), Math.min(crit14, crit15) + Math.abs(crit14 - crit15) * .2, results14.calcGrossProfit(), results14.calcGrossLoss(),
                            results14.calcGrossWinLossRatio(), results14.losingMonths(), results14.size(), results09.calcCum(), results10.calcCum(), results11.calcCum(), results12.calcCum(), results13.calcCum(), results16.calcCum());
                }}
        }}}}}
        out.close();
        
        
        
        

        
        

        


        
    }
    
    
    public static void runAll() throws Exception {
        Prices p = Prices.readParsedFile(symbol + "_"+ year + "_1m.dat", year);       
        //changes the time frame
        p = p.oneMshrinkTo(39);
        //create an arraylist for the close price
        ArrayList<Double> close = p.creatList("close");
        
        
        
        //97 19 17 18 0.0001 0.0058 165.8 135 15 close
        //48 9 23 37 0.000125 0.0058 165.8 135 15 close
        TradeList results = runWith(20, 39, 41, 0.56667, 58, 165.8, 135, 15, close, p);
        //212 162
        //results.produceGraphData("by candle size", 10);
        results.printStats();
    }
    
    public static void print(String s){
        System.out.println(s);
    }
    
    //runs an instance of the entire trade using the parameters entered
    //repeated calls for optimization
    public static TradeList runWith(int ema, int fastSma, int slowSma, double crossLevel, double maxSL, double takeProfitLevel, double secondtakeProfitLevel, 
            int candlesInWave, ArrayList<Double> close, Prices p){
        
        crossLevel = pipvalue*crossLevel;
        maxSL = pipvalue*maxSL;
        
        ArrayList<Double> ema5 = Indicator.calcEma(close, ema);
        ArrayList<Double> sma8 = Indicator.calcSma(close, fastSma);
        ArrayList<Double> sma20 = Indicator.calcSma(close, slowSma);
        
        
        TradeList trades = new TradeList(p, spread, ema5, sma8, sma20);
        boolean shortcross, longcross, shortexitcross, longexitcross;
        double stopLoss, takeProfit, pointB;
        

        for(int i = slowSma + candlesInWave + 24; i < close.size(); ++i){
            shortcross = false;
            longcross = false;
            shortexitcross = false;
            longexitcross = false;
            
            

           
            
            //short trades
            if( (ema5.get(i) < sma20.get(i) - crossLevel && ema5.get(i-1) >= sma20.get(i-1) && 
                    ema5.get(i) < sma8.get(i)) )
                shortcross = true;
            
            
            //long trades
            else if( (ema5.get(i) > sma20.get(i) + crossLevel && ema5.get(i-1) <= sma20.get(i-1) && 
                    ema5.get(i) > sma8.get(i)) ) 
                longcross = true;
            
            
            
            if( shortcross && trades.inTrade() == false ) {
                
                pointB = Prices.highestIn(p, i-candlesInWave, i);
                
                
                //stopLoss = Indicator.fibo(p.c(i), pointB, stopLossLevel);
                //if (stopLoss - p.c(i) > maxSL && maxSL != 0)
                    stopLoss = p.c(i) + maxSL;
                takeProfit = Indicator.fibo(p.c(i), pointB, takeProfitLevel);
                if (takeProfit < Prices.lowestIn(p, i - 24, i))
                    takeProfit = Indicator.fibo(p.c(i), pointB, secondtakeProfitLevel);
                
                trades.addTrade(i, "short", stopLoss, takeProfit);
            }
            
            else if( longcross && trades.inTrade() == false){
                
                pointB = Prices.lowestIn(p, i-candlesInWave, i);
                
                //stopLoss = Indicator.fibo(p.c(i), pointB, stopLossLevel);
                //if(p.c(i) - stopLoss > maxSL && maxSL != 0)
                    stopLoss = p.c(i) - maxSL;
                takeProfit = Indicator.fibo(p.c(i), pointB, takeProfitLevel);
                if (takeProfit > Prices.highestIn(p, i-24, i))
                    takeProfit = Indicator.fibo(p.c(i), pointB, secondtakeProfitLevel);
                
                trades.addTrade(i, "long", stopLoss, takeProfit);
            }
            
            
            //close trades
            else if(trades.isInShort){
                
                //check if exceeds stoploss or take profit
                trades.checkExit(i);
               
                if(trades.isInShort)
                    if(ema5.get(i) >= sma8.get(i))
                        trades.exitTrade(i, "Ema reversed", p.c(i));   
            }
            

            
            
            else if(trades.isInLong){
                
                trades.checkExit(i);
                
                if(trades.isInLong)
                    if(ema5.get(i) <= sma8.get(i))
                        trades.exitTrade(i, "Ema reversed", p.c(i));
            }
                
            
            
            
        }//end for loop
        
        trades.finalize(close.size()-1);
        //t4 = System.nanoTime();
        //print(String.format("Trades complete. %d", (t4-t3)/1000000));
        
        
        return trades;
    }
    
       
    
       
    
    

    
    
        
}
