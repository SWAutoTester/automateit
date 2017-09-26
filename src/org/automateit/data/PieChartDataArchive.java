/**
 * This file is part of Automate It!'s free and open source web and mobile 
 * application testing framework.
 * 
 * Automate It! is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Automate It! is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Automate It!.  If not, see <http://www.gnu.org/licenses/>.
 **/

package org.automateit.data;

import java.io.File;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.*;
import org.jfree.util.*;

import org.apache.log4j.Logger;
        
/**
 * Data archive claas for creating pie charts using jfreecharts.
 * 
 * @author mburnside
 */
public class PieChartDataArchive extends DataArchiveBase {
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(PieChartDataArchive.class);
    
    /**
     * title of the chart
     */
    private String title = null;
    
    /**
     * Copy constructor
     * 
     * @param title The title of the chart
     */
    public PieChartDataArchive(String title) { this.title = title; }
    
    /**
     * Save the data to a file.
     * 
     * @param filename The filename to save the data to
     * 
     * @throws Exception 
     */
    public void saveData(String filename) throws Exception {
        
        log.info("Attempt to save png pie chart to file: " + filename);
        
        try {
      
            JFreeChart chart = createChart(createDataset(), title);
            
            File file = new File(filename + ".png");
            
            ChartUtilities.saveChartAsPNG(file, chart, 1200, 1200);
            
            log.info("Successfully saved pie chart to png file: " + filename);
            
        }
        catch(Exception e) { log.error(e); throw e; }
        
    }
   
    /**
     * Get result value to create the pie chart.
     * 
     * @return 
     */
    private PieDataset createDataset() {
        
        log.info("Creating pie dataset for pie chart, list size for chart: " + list.size());
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for(int i = 0; i < list.size(); i++) {
                 
            String[] data = list.get(i); 
  
            dataset.setValue(data[0], (new Integer(data[1]).intValue()));
   
            log.info("Added data to dataset for pie chart: " + data[0] + "|" +  data[1]);
            
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
    private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(title,          // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        
        
        return chart;
        
    }
    
}

