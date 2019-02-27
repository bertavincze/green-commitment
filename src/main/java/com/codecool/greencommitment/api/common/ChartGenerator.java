package com.codecool.greencommitment.api.common;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.w3c.dom.Element;

public class ChartGenerator {

    private DefaultCategoryDataset dcd;
    private XMLHandler xmlHandler;

    public ChartGenerator() {
        this.dcd = new DefaultCategoryDataset();
        this.xmlHandler = new XMLHandler();
    }
    
    private String convertMillisIntoDate(long data) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(data));
    }

    private void generateChart(String id, ChartType chartType) throws IOException {
        JFreeChart chart;
        addDataToChart(id);
        if (chartType == ChartType.BAR) {
            chart = ChartFactory.createBarChart("Time & Temperature", "Time",
                "Temperature", dcd, PlotOrientation.VERTICAL,
                true, true, false);
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            Color color = new Color(79, 129, 189);
            renderer.setSeriesPaint(0, color);
        } else if (chartType == ChartType.LINE) {
            chart = ChartFactory.createLineChart("Time & Temperature", "Time",
                "Temperature", dcd, PlotOrientation.VERTICAL,
                true, true, false);
            CategoryPlot plot = chart.getCategoryPlot();
            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            Color color = new Color(79, 129, 189);
            renderer.setSeriesPaint(0, color);
        } else {
            throw new IllegalStateException();
        }
        saveToFile(id, chart);
    }

    private void saveToFile(String id, JFreeChart chart) throws IOException {
        int width = 640;
        int height = 480;
        File file = new File(id + ".jpeg");
        ChartUtilities.saveChartAsJPEG(file, chart, width, height);
    }

    private void addDataToChart(String fileName) {
        xmlHandler.loadXml(fileName + ".xml");
        Element rootNode = xmlHandler.getDoc().getDocumentElement();
        List<Element> measurementNodes = xmlHandler.getElements(rootNode);
        for (Element measurementNode : measurementNodes) {
            List<Element> childNodes = xmlHandler.getElements(measurementNode);
            long time = Long.valueOf(xmlHandler.getString(childNodes, "time"));
            int value = Integer.valueOf(xmlHandler.getString(childNodes, "value"));
            dcd.addValue(value, "temp", convertMillisIntoDate(time));
        }
    }
}
