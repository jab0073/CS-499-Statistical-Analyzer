package FrontEndUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OutputManager {
    private static final ArrayList<String> outputs = new ArrayList<>();
    private static final ArrayList<JPanel> graphs = new ArrayList<>();

    public static void addOutput(String measureName, String output){
        outputs.add(measureName + "," + output);
    }

    public static void displayOutputs(){
        if(outputs.size() == 0) return;

        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLayout(new BorderLayout());

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);

        for(String s : outputs){
            String[] sArr = s.split(",");
            String name = sArr[0];
            String data = sArr[1];
            for(int i = 2; i < sArr.length; i++){
                data = data + "\n" + sArr[i];
            }
            data = data.indent(3);

            String out = textPane.getText();

            if(out != ""){
                out += "\n";
            }

            out += name;
            out += "\n" + data;

            textPane.setText(out);
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
