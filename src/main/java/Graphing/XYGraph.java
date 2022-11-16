package Graphing;

import FrontEndUtilities.GUIMeasure;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;

public class XYGraph implements IGraph{
    private final GraphTypes type = GraphTypes.X_Y;
    private String data = "";
    private DataFormat dataFormat = DataFormat.MX_PLUS_B;

    public XYGraph(){

    }

    @Override
    public void setData(Object data, DataFormat format) {
        this.dataFormat = format;
        if(format == DataFormat.MX_PLUS_B){
            this.data = (String) data;
        }else if(format == DataFormat.PROBABILITY){
            ArrayList<String> i = (ArrayList<String>) data;

            StringBuilder r = new StringBuilder();

            for(String s : i){
                if(i.iterator().hasNext()){
                    s = s+",";
                }

                r.append(s);
            }

            this.data = r.toString();

        }

        //TODO: Handle string conversion
    }

    @Override
    public void graphData(GUIMeasure measure) {
        //Create XY chart
        JFreeChart xylinechart = ChartFactory.createXYLineChart(
                measure.getName(),
                "X",
                "Y",
                createDataSet(measure.getName(), measure),
                PlotOrientation.VERTICAL,
                false, true, false
        );



        ChartPanel chartPanel = new ChartPanel(xylinechart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560,367));
        chartPanel.setName(measure.getName());
        final XYPlot plot = xylinechart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.red);
        renderer.setSeriesPaint(1, Color.blue);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(0.5f));
        renderer.setSeriesShape(1, new Rectangle(2,2));
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);
        plot.setRenderer(renderer);

        GraphManager.addGraphPanel(chartPanel);
    }

    @Override
    public GraphTypes getGraphType() {
        return type;
    }

    /**
     * Create a dataset for the XYGraph
     * @param title The title of the graph
     * @param measure The measure that is being graphed
     * @return The dataset created
     */
    private XYDataset createDataSet(String title, GUIMeasure measure){
        switch (this.dataFormat){
            case MX_PLUS_B -> {
                return mxPlusBDataset(title, measure);
            }
            case PROBABILITY -> {
                return probabilityDataset(title, measure);
            }
            default -> {
                return null;
            }
        }

    }

    private XYDataset mxPlusBDataset(String title, GUIMeasure measure){
        final XYSeries a = new XYSeries(title);
        final XYSeries d = new XYSeries("Points");

        double minX = findSmallest(measure.getData()[0]);
        double maxX = findLargest(measure.getData()[0]);
        double scale = maxX - minX;

        minX = minX - (scale*0.1);
        maxX = maxX + (scale*0.1);

        String[] bm = data.split(",");
        double b = Double.parseDouble(bm[0].split("=")[1]);
        double m = Double.parseDouble(bm[1].split("=")[1]);

        a.add(maxX, YAtPoint(maxX, m, b));
        a.add(minX, YAtPoint(minX, m, b));

        ArrayList<String> dataX = measure.getData()[0];
        ArrayList<String> dataY = measure.getData()[1];

        if(dataX.size() > dataY.size()){
            dataX.subList(dataY.size(), dataX.size()).clear();
        }

        for(int i = 0; i < dataX.size(); i++){
            d.add(Double.parseDouble(dataX.get(i)), Double.parseDouble(dataY.get(i)));
        }



        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(a);
        dataset.addSeries(d);

        return  dataset;
    }

    private XYDataset probabilityDataset(String title, GUIMeasure measure){
        final XYSeries a = new XYSeries(title);

        String[] data = this.data.split(",");

        for(String s : data){
            String[] point =  s.split(":");
            a.add(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
        }

        final  XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(a);
        return dataset;
    }

    private double YAtPoint(double x, double slope, double yInt){
        return (slope * x) + yInt;
    }



    private double findSmallest(ArrayList<String> data){
        double smallest = 999999999999999999999.0;
        for(String s : data){
            double d = Double.parseDouble(s);
            if(d < smallest){
                smallest = d;
            }
        }

        return smallest;
    }

    private double findLargest(ArrayList<String> data){
        double largest = -999999999999999999999.0;
        for(String s : data){
            double d = Double.parseDouble(s);
            if(d > largest){
                largest = d;
            }
        }

        return largest;
    }
}
