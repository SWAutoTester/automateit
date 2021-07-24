/**
 * This file is part of Automate It's free and open source web and mobile 
 * application testing framework.
 * 
 * Automate It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Automate It is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Automate It.  If not, see <http://www.gnu.org/licenses/>.
 **/

package org.automateit.data;

import java.io.File;
import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.*;
import org.jfree.util.*;
import org.jfree.data.xy.*;
import org.jfree.data.category.*;
import org.jfree.chart.renderer.category.*;

import org.apache.log4j.Logger;
        
/**
 * Data archive class for producing x-y bar charts using jfreechart.
 * 
 * @author mburnside
 */
public class XYBarChartDataArchive extends DataArchiveBase {
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(XYBarChartDataArchive.class);
    
    /**
     * title of the chart
     */
    private String title = null;
    
    /**
     * Copy constructor
     * 
     * @param title The title of the chart
     */
    public XYBarChartDataArchive(String title) { this.title = title; }
    
    /**
     * Save the data to a file.
     * 
     * @param filename The filename to save the data to
     * 
     * @throws Exception 
     */
    public void saveData(String filename) throws Exception {
        
        log.info("Attempt to save png bar chart to file: " + filename);
        
        try {
      
            JFreeChart chart = createChart(createDataset(), title);
            
            File file = new File(filename + ".barchart.png");
            
            ChartUtilities.saveChartAsPNG(file, chart, 1200, 1200);
            
            log.info("Successfully saved bar chart to png file: " + file.getName());
            
        }
        catch(Exception e) { log.error(e); throw e; }
        
    }
   
    /**
     * Get result value to create the pie chart.
     * 
     * @return 
     */
    
    private CategoryDataset createDataset() {
        
        log.info("Creating dataset for bar chart, list size for chart: " + list.size());
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for(int i = 0; i < list.size(); i++) {
                 
            String[] data = list.get(i); 
            
            String category = data[0];
            
            String row = "Event Count Data Collection";
            
            double value = Double.valueOf(data[1]);
  
            dataset.addValue(value, row, category);
   
            log.info("Added data to dataset for bar chart: " + data[0] + "|" +  data[1]);
            
        }
        
        return dataset;
        
    }


    /**
     * create the chart.
     * 
     * @param dataset
     * @param title
     * @return 
     */
    private JFreeChart createChart(CategoryDataset dataset, String title) {
        
        final JFreeChart chart = ChartFactory.createBarChart(
             title,       // chart title
             "Event",               // domain axis label
             "Count",                  // range axis label
             dataset,                  // data
             PlotOrientation.HORIZONTAL, // orientation
             true,                     // include legend
             true,                     // tooltips?
             false                     // URLs?
          );
       
        chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
          
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
 
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(new Color(0, 206, 209));
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(new Color(0, 206, 209));
        plot.setDomainCrosshairPaint(new Color(0, 206, 209));
        plot.setRangeCrosshairPaint(new Color(0, 206, 209));
        
        plot.setRenderer(new DifferenceBarRenderer());
        
        return chart;
        
    }
    
    /**
     * Internal class to change the paint color of the bar totals
     */
    public class DifferenceBarRenderer extends BarRenderer {
  
        public DifferenceBarRenderer() { super(); }
  
        public Paint getItemPaint(int x_row, int x_col) { return new Color(30, 144, 255); }
        
        public Paint getItemLabelPaint() { return Color.blue; }
        
        public Paint getBasePaint() { return Color.white; }
        
        public Paint getBaseOutlinePaint() { return new Color(30, 144, 255); }
        
        public Paint getBaseItemLabelPaint() { return Color.cyan; }
        
        public Paint getItemOutlinePaint(int row, int column) { return new Color(72, 209, 204); }
        
        public Paint getSeriesOutlinePaint(int series) { return Color.cyan; }
        
        public Paint getSeriesItemLabelPaint(int series) { return Color.blue; }

    }
  
}

