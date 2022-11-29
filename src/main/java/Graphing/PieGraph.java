package Graphing;

import Enums.DataFormat;
import Enums.GraphTypes;
import FrontEndUtilities.GUIMeasure;
import Interfaces.IGraph;
import Managers.GraphManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.util.ArrayList;
import java.util.TreeMap;

public class PieGraph implements IGraph {
    private final GraphTypes type = GraphTypes.PIE_CHART;
    private String data = "";
    private DataFormat dataFormat = DataFormat.MX_PLUS_B;

    public PieGraph(){

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
        final JFreeChart chart = ChartFactory.createPieChart(
                measure.getName(),         // chart title
                createDataSet(measure.getName(), measure),                    // data
                false,                       // include legend
                true,
                false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560,367));
        chartPanel.setName(measure.getName());

        final PiePlot plot = (PiePlot) chart.getPlot();
        //plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);

        GraphManager.addGraphPanel(chartPanel);
    }

    /**
     * Create a dataset for the HorizontalBarGraph
     * @param title The title of the graph
     * @param measure The measure that is being graphed
     * @return The dataset created
     */
    private PieDataset createDataSet(String title, GUIMeasure measure){
        switch (this.dataFormat){
            case PROBABILITY -> {
                return probabilityDataset(title, measure);
            }
            default -> {
                return null;
            }
        }

    }

    private PieDataset probabilityDataset(String title, GUIMeasure measure){
        String[] data = this.data.split(",");
        DefaultPieDataset dataset = new DefaultPieDataset();

        for(String s : data){
            String[] point =  s.split(":");

            dataset.setValue(point[0], Double.parseDouble(point[1]));
        }

        return dataset;
    }

    @Override
    public GraphTypes getGraphType() {
        return type;
    }

    private String sortProbability(String in){
        TreeMap<String, String> map = new TreeMap<>();

        String[] data = in.split(",");
        for(String s : data){
            String[] split = s.split(":");
            map.put(split[0], split[1]);
        }

        StringBuilder out = new StringBuilder();
        for(String key : map.keySet()){
            String build = key + ":" + map.get(key) + ",";
            out.append(build);
        }

        return out.toString();
    }
}
