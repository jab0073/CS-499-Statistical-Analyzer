package Graphing;

import FrontEndUtilities.GUIMeasure;
import FrontEndUtilities.OutputManager;
import Interfaces.IMeasure;
import Measures.UserDefinedMeasure;
import org.jfree.chart.ChartPanel;
import org.reflections.Reflections;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GraphManager {
    private static final ArrayList<IGraph> graphs = new ArrayList<>();
    private static final ArrayList<ChartPanel> graphPanels = new ArrayList<>();

    public static void init()throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException{
        String packageName = "Graphing";

        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends IGraph>> set = new HashSet<>(reflections.getSubTypesOf(IGraph.class));

        for(Class<? extends IGraph> c : set){
            IGraph a = c.getDeclaredConstructor().newInstance();
            graphs.add(a);
        }
    }

    public static void graphOutput(GraphTypes type, Object data, GUIMeasure measure){
        IGraph graph = graphs.stream().filter(g -> g.getGraphType() == type).findFirst().orElse(null);

        if(graph == null) return;

        graph.setData(data, measure.getOutputFormat());

        graph.graphData(measure);
    }

    public static void displayGraphs(){
        if(graphPanels.size() == 0) return;

        for(ChartPanel g : graphPanels){
            OutputManager.addGraph(g);
        }
    }

    public static void addGraphPanel(ChartPanel panel){
        graphPanels.add(panel);
    }

    public static void clearGraphs(){
        graphPanels.clear();
    }
}
