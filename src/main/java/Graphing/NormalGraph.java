package Graphing;

import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
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
import java.util.*;

public class NormalGraph implements IGraph{
    private final GraphTypes type = GraphTypes.NORMAL_CURVE;
    private String data = "";
    private DataFormat dataFormat = DataFormat.MX_PLUS_B;

    private double mean = 0;
    private double std = 0;

    public NormalGraph(){

    }

    @Override
    public void setData(Object data, DataFormat format) {
        this.dataFormat = format;
        if(format == DataFormat.DOUBLE_LIST){
            ArrayList<Double> i = new ArrayList<Double>((Collection<? extends Double>) data);

            StringBuilder r = new StringBuilder();

            for(double d : i){
                String s = String.valueOf(d);
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
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesStroke(0, new BasicStroke(0.5f));
        renderer.setSeriesShape(1, new Rectangle(2,2));
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        GraphManager.addGraphPanel(chartPanel);
    }

    @Override
    public GraphTypes getGraphType() {
        return type;
    }

    /**
     * Create a dataset for the HorizontalBarGraph
     * @param title The title of the graph
     * @param measure The measure that is being graphed
     * @return The dataset created
     */
    private XYDataset createDataSet(String title, GUIMeasure measure){
        switch (this.dataFormat){
            case DOUBLE_LIST -> {
                return doubleDataset(title, measure);
            }
            default -> {
                return null;
            }
        }

    }

    private XYDataset doubleDataset(String title, GUIMeasure measure){
        String[] data = this.data.split(",");

        final XYSeries curve = createNormalCurveDataset(measure);
        final XYSeries bars = new XYSeries("Bars");

        ArrayList<Double> measureData = sortMeasureData(measure.getData()[0]);

        int i = 0;
        for(String s : data){
            if(i - 1 > 0){
                bars.add(measureData.get(i), (Number) Double.parseDouble(data[i-1]));
            }

            bars.add(measureData.get(i), (Number) Double.parseDouble(s));
            i++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(curve);
        dataset.addSeries(bars);

        return  dataset;


    }

    private XYSeries createNormalCurveDataset(GUIMeasure measure){

        if(Objects.equals(measure.getName(), MeasureConstants.binomial)){
            double n = Double.parseDouble(Expressions.getArgument("n"));
            double p = Double.parseDouble(Expressions.getArgument("p"));

            mean = n*p;
            std = Math.sqrt((n*p)*(1.0-p));
        }

        double minimum = findSmallest(measure.getData()[0]);
        double maximum = findLargest(measure.getData()[0]);
        double samples = 1000;

        double sampleRate = (maximum - minimum)/samples;

        XYSeries series = new XYSeries("Curve");

        for(double i = minimum; i <= maximum; i+=sampleRate){
            double point = pointOnNormal(i);
            series.add(i, point);
        }

        return series;
    }

    private double pointOnNormal(double x){
        double exp = -1.0 * (Math.pow(x-mean, 2) / (2 * Math.pow(std, 2)));
        return (1.0/(std * Math.sqrt(2 * Math.PI))) * Math.pow(Math.E, exp);
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

    private ArrayList<Double> sortMeasureData(ArrayList<String> in){
        ArrayList<Double> result = new ArrayList<>();

        for(String s : in){
            result.add(Double.parseDouble(s));
        }

        Collections.sort(result);

        return result;
    }

}
