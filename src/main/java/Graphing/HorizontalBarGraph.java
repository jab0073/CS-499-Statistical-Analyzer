package Graphing;

import FrontEndUtilities.GUIMeasure;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class HorizontalBarGraph implements IGraph{
    private final GraphTypes type = GraphTypes.HORIZONTAL_BAR;
    private String data = "";
    private DataFormat dataFormat = DataFormat.MX_PLUS_B;

    public HorizontalBarGraph(){

    }

    @Override
    public void setData(Object data, DataFormat format) {
        this.dataFormat = format;

        if(format == DataFormat.PROBABILITY){
            ArrayList<String> i = (ArrayList<String>) data;

            StringBuilder r = new StringBuilder();

            for(String s : i){
                if(i.iterator().hasNext()){
                    s = s+",";
                }

                r.append(s);
            }



            this.data = sortProbability(r.toString());

        }
    }



    @Override
    public void graphData(GUIMeasure measure) {
        final JFreeChart chart = ChartFactory.createBarChart(
                measure.getName(),         // chart title
                "Category",                 // domain axis label
                "Score",                // range axis label
                createDataSet(measure.getName(), measure),                    // data
                PlotOrientation.HORIZONTAL, // orientation
                false,                       // include legend
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560,367));
        chartPanel.setName(measure.getName());
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

        BarRenderer renderer = new BarRenderer();
        plot.setRenderer(renderer);

        GraphManager.addGraphPanel(chartPanel);
    }

    /**
     * Create a dataset for the HorizontalBarGraph
     * @param title The title of the graph
     * @param measure The measure that is being graphed
     * @return The dataset created
     */
    private CategoryDataset  createDataSet(String title, GUIMeasure measure){
        switch (this.dataFormat){
            case PROBABILITY -> {
                return probabilityDataset(title, measure);
            }
            default -> {
                return null;
            }
        }

    }

    private CategoryDataset probabilityDataset(String title, GUIMeasure measure){
        String[] data = this.data.split(",");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(String s : data){
            String[] point =  s.split(":");

            dataset.addValue(Double.parseDouble(point[1]),"0", point[0]);
        }

        return dataset;
    }

    @Override
    public GraphTypes getGraphType() {
        return type;
    }

    private String sortProbability(String in){
        TreeMap<Double, String> map = new TreeMap<>();

        String[] data = in.split(",");
        for(String s : data){
            String[] split = s.split(":");
            map.put(Double.parseDouble(split[0]), split[1]);
        }

        StringBuilder out = new StringBuilder();
        for(Double key : map.keySet()){
            String build = key + ":" + map.get(key) + ",";
            out.append(build);
        }

        return out.toString();
    }
}
