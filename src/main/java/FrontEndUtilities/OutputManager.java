package FrontEndUtilities;

import GUI.CellsTable;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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

    private static void displayOutputSelection(){
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

        //Create new panel to hold selectors
        // Has 2 Columns and splits the measure selectors between the two columns
        JPanel selectionPanel = new JPanel(new GridLayout((int) Math.ceil(((float) selectors.length)/2.0), 2));

        JPanel content = new JPanel(new BorderLayout());

        JFrame frame = new JFrame();
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        for(JPanel p : selectors){
            selectionPanel.add(p);
        }

        content.add(selectionPanel, BorderLayout.CENTER);

        //Add Save button to panel
        JButton btnSave = new JButton("Save Outputs");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayFileBrowser(outputSelections);
                frame.dispose();
            }
        });

        content.add(btnSave, BorderLayout.SOUTH);

        content.revalidate();
        content.repaint();

        frame.pack();

        frame.setVisible(true);
    }

    private static void displayFileBrowser(boolean[] selectedOutputs){
        //File browser stuff
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV Files", "csv");
        fileChooser.setFileFilter(filter);

        JDialog dialog = new JDialog();

        int result = fileChooser.showSaveDialog(dialog);

        dialog.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (FilenameUtils.getExtension(selectedFile.getName()).equalsIgnoreCase("csv")) {
                // filename is OK as-is
            } else {
                selectedFile = new File(selectedFile.getParentFile(), FilenameUtils.getBaseName(selectedFile.getName())+".csv"); // ALTERNATIVELY: remove the extension (if any) and replace it with ".xml"
            }


            System.out.println(selectedFile.getAbsolutePath());

            prepareAndSaveFile(selectedFile.getAbsolutePath(), selectedOutputs);
        }

        dialog.dispose();


    }

    private static void prepareAndSaveFile(String fileLocation, boolean[] selectedOutputs){
        System.out.println(fileLocation +  "," + Arrays.toString(selectedOutputs));

        StringBuilder outputFileString = new StringBuilder();

        int i = 0;
        for(boolean b : selectedOutputs){
            if(b){
                String name = outputs.get(i).split(",")[0];
                String[] data = outputs.get(i).split(",");
                    data = Arrays.copyOfRange(data, 1, data.length);

                outputFileString.append(name).append(",");

                int j = 0;
                for(String s: data){
                    String l = s;
                    if(l.startsWith("[")){
                        l = l.replace("[", "");
                    }

                    if(l.endsWith("]")){
                        l = l.replace("]", "");
                    }

                    l = l.replace("\n", ",");

                    outputFileString.append(l);

                    if(j != data.length-1){
                        outputFileString.append(",");
                    }

                    j++;
                }
            }

            outputFileString.append("\n");

            i++;
        }

        try {
            FileWriter outFile = new FileWriter(fileLocation);
            outFile.write(outputFileString.toString());
            outFile.close();
        }catch (IOException e){
            System.out.println("Shits fucked");
        }
    }
}
