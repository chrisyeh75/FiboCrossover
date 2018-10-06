/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboCrossover;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author CY
 */
public class Plot {
    
    //plots data, data1 is arraylist of data top panel - price, emas
    //  each index represents a new plot
    //data2 is arraylist of data of bottom panel - rsi
        public static void emaRsi(ArrayList<XYDataset> data1,ArrayList<XYDataset> data2, ArrayList<String> dp){
        if(data1.size() != data2.size()){
            System.out.println("Graphing error: data size not match");
            return;
        }

      ArrayList<ChartPanel> chartlist = new ArrayList<>();
      for(int i = 0; i< data1.size(); ++i){
        final XYItemRenderer renderer1 = new StandardXYItemRenderer();
        final NumberAxis rangeAxis1 = new NumberAxis();
        rangeAxis1.setAutoRangeIncludesZero(false);
        final XYPlot subplot1 = new XYPlot(data1.get(i), null, rangeAxis1, renderer1);
        subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        
        
        // create subplot 2...
        final XYItemRenderer renderer2 = new StandardXYItemRenderer();
        final NumberAxis rangeAxis2 = new NumberAxis();
        rangeAxis2.setAutoRangeIncludesZero(false);
        final XYPlot subplot2 = new XYPlot(data2.get(i), null, rangeAxis2, renderer2);
        subplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);

        // parent plot...
        final NumberAxis domainAxis1 = new NumberAxis();
        domainAxis1.setAutoRangeIncludesZero(false);
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domainAxis1);
        plot.setGap(10.0);
        
        // add the subplots...
        plot.add(subplot1, 4);
        plot.add(subplot2, 1);
        plot.setOrientation(PlotOrientation.VERTICAL);

        // return a new chart containing the overlaid plot...
        JFreeChart chart = new JFreeChart(dp.get(i),
                              JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chartlist.add(new ChartPanel(chart, true, true, true, false, true));
      } //end for loop
      
      ArrayList<ApplicationFrame> flist = new ArrayList<>(); 
      for(int i = 0; i< Math.ceil((double) chartlist.size()/6); ++i){
        flist.add(new ApplicationFrame(""));
        if(data1.size() > 1)
            flist.get(i).setLayout(new GridLayout(2,3));
        else
            flist.get(i).setLayout(new GridLayout(1,1));
        for(int j = i*6; j<(i+1)*6; ++j) {
            if (j >= chartlist.size()) break;
            flist.get(i).add(chartlist.get(j)); 
        }
        flist.get(i).pack();
        RefineryUtilities.centerFrameOnScreen(flist.get(i));
        flist.get(i).setVisible(true);
      }
    }
        
        
        public static void graphHistogram (ArrayList<Double> data){
        double[] datas = new double[data.size()];
           int number = 50;
       for(int i = 0; i < data.size(); ++i)
           datas[i] = data.get(i);
       HistogramDataset dataset = new HistogramDataset();
       dataset.setType(HistogramType.FREQUENCY);
       dataset.addSeries("Histogram",datas,number);
       String plotTitle = "Histogram"; 
       String xaxis = "number";
       String yaxis = "value"; 
       PlotOrientation orientation = PlotOrientation.VERTICAL; 
       boolean show = false; 
       boolean toolTips = false;
       boolean urls = false; 
       JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis, 
                dataset, orientation, show, toolTips, urls);
       int width = 500;
       int height = 300; 
       
       JFrame chartx = new JFrame();
           JPanel histo = new ChartPanel(chart);
           chartx.add(histo);
           chartx.setSize(640, 480);
           chartx.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      chartx.setVisible(true);  
    }
        
        public static void plot (ArrayList<Double> p){
           XYSeriesCollection d = new XYSeriesCollection();
           XYSeries s = new XYSeries("");
           for(int i = 0; i < p.size(); ++i)
               s.add((double) i+1, p.get(i));
           d.addSeries(s);
           
           
           JFreeChart chart = ChartFactory.createXYLineChart("", "x", "y", d);
           XYPlot plot = chart.getXYPlot();
           XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
             plot.setRenderer(renderer);
             plot.setBackgroundPaint(Color.WHITE);
             plot.setRangeGridlinePaint(Color.GRAY);
             plot.setDomainGridlinePaint(Color.GRAY);
             
        NumberAxis domain = new NumberAxis();
        domain.setAutoRangeIncludesZero(false);
        NumberAxis range = new NumberAxis();
        range.setAutoRangeIncludesZero(false);
             plot.setDomainAxis(domain);
             plot.setRangeAxis(range);

           JFrame chartx = new JFrame();
           JPanel linechart = new ChartPanel(chart);
           chartx.add(linechart);
           chartx.setSize(640, 480);
           chartx.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      chartx.setVisible(true);  
    }
           
}
   
