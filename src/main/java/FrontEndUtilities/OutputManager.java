package FrontEndUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public static void saveOutputsToFile(){
        //Open Selection menu
            //User Selects what outputs to save
        displayOutputSelection();

        //Open file browser
            //User selects where to save files and what name to give them

        //Convert outputs to save format
        //Build output file
        //save file
    }

    private static boolean[] displayOutputSelection(){
        boolean[] outputSelections = new boolean[outputs.size()];

        JPanel[] selectors = new JPanel[outputSelections.length];

        int i = 0;

        //Build list of selectors to display
        for(String s : outputs){
            String name = s.split(",")[0];
            JLabel label = new JLabel(name);
            JCheckBox selectionBox = new JCheckBox();

            int finalI = i;
            selectionBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    outputSelections[finalI] = selectionBox.isSelected();
                }
            });

            JPanel selector = new JPanel();
            selector.setLayout(new GridLayout(1,2, 10, 10));

            selector.add(label);
            selector.add(selectionBox);

            selectors[i] = selector;

            i++;
        }

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout((int) Math.ceil(((float) selectors.length)/2.0), 2));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        for(JPanel p : selectors){
            frame.add(p);
        }

        frame.setVisible(true);

        return outputSelections;
    }
}
