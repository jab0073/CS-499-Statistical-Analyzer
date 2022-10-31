package FrontEndUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OutputManager {
    private static final ArrayList<String> outputs = new ArrayList<>();
    private static final ArrayList<JPanel> graphs = new ArrayList<>();

    public static void addOutput(String output){
        outputs.add(output);
    }

    public static void displayOutputs(){
        if(outputs.size() == 0) return;

        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLayout(new BorderLayout());

        JTextPane textPane = new JTextPane();

        for(String s : outputs){
            textPane.setText(textPane.getText() + "\n" + s);
        }

        if(graphs.size() > 0){
            JTabbedPane mainTab = new JTabbedPane();
            JTabbedPane graphTab = new JTabbedPane();

            for(JPanel g : graphs){
                graphTab.addTab(g.getName(), g);
            }

            mainTab.addTab("Outputs", textPane);
            mainTab.addTab("Graphs", graphTab);

            frame.add(mainTab);
        }else{
            frame.add(textPane);
        }


        frame.setVisible(true);
    }

    public static void addGraph(JPanel graph){
        graphs.add(graph);
    }

    public static void clearOutput(){
        outputs.clear();
        graphs.clear();
    }
}
