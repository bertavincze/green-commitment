package com.codecool.greencommitment.cmdProg;

import java.io.*;

import com.codecool.greencommitment.api.common.Measurement;
import com.codecool.greencommitment.api.common.MeasurementType;
import com.codecool.greencommitment.api.common.XMLHandler;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart {
    
    public static void main( String[ ] args ) throws Exception {
        LineChart lc = new LineChart();
        lc.showLineChart("joli","buli");
    }
    public void showLineChart(String id, String type) throws InterruptedException, IOException {
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        Measurement m = new Measurement(12,12, MeasurementType.CELSIUS);
        Thread.sleep(1000);
        Measurement m1 = new Measurement(66,93, MeasurementType.CELSIUS);
        Thread.sleep(1000);
        Measurement m2 = new Measurement(45,34, MeasurementType.CELSIUS);
        Thread.sleep(1000);
        Measurement m3 = new Measurement(18,45, MeasurementType.CELSIUS);
        Thread.sleep(1000);
        Measurement m4 = new Measurement(99,66, MeasurementType.CELSIUS);
    
        line_chart_dataset.addValue(m.getValue(),"temp", String.valueOf(m.getTime()));
        line_chart_dataset.addValue(m1.getValue(),"temp", String.valueOf(m1.getTime()));
        line_chart_dataset.addValue(m2.getValue(),"temp", String.valueOf(m2.getTime()));
        line_chart_dataset.addValue(m3.getValue(),"temp", String.valueOf(m3.getTime()));
        line_chart_dataset.addValue(m4.getValue(),"temp", String.valueOf(m4.getTime()));
        
        XMLHandler xml = new XMLHandler();
        JFreeChart chart;
        if (type.equalsIgnoreCase("line")){
            chart = ChartFactory.createLineChart(
                "Time & Temperature","Time",
                "Temperature",
                line_chart_dataset,PlotOrientation.VERTICAL,
                true,true,false);
        } else {
            chart = ChartFactory.createBarChart("Time & Temperature","Time",
                "Temperature",
                line_chart_dataset,PlotOrientation.VERTICAL,
                true,true,false);
        }
        
        
        
        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */
        File lineChart = new File( id + ".jpeg" );
        ChartUtilities.saveChartAsJPEG(lineChart ,chart, width ,height);
    }
}
