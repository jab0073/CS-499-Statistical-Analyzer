package FrontEndUtilities;

import BackEndUtilities.Constants;
import GUI.SingleRootFileSystemView;
import Settings.UserSettings;
import org.apache.commons.io.FilenameUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class OutputManager {
    private static final ArrayList<String> outputs = new ArrayList<>();
    private static final ArrayList<ChartPanel> graphs = new ArrayList<>();

    public static void addOutput(String measureName, String output){
        outputs.add(measureName + "," + output);
    }

    /**
     * It creates a JFrame with a JTextPane and a JScrollPane, and then adds the textPane to the scrollPane, and then adds
     * the scrollPane to the frame
     */
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
            StringBuilder data = new StringBuilder();
            for(int i = 1; i < sArr.length; i++){
                if(sArr[i].contains(" ")){
                    sArr[i] = sArr[i].replace(" ", "");
                }
                if(sArr[i].startsWith("[")){
                    sArr[i] = sArr[i].replace("[", "");
                }

                if(sArr[i].endsWith("]")){
                    sArr[i] = sArr[i].replace("]", "");
                }

                data.append(sArr[i]);

                if(i != sArr.length -1){
                    data.append("\n");
                }
            }
            data = new StringBuilder(data.toString().indent(3));

            String out = textPane.getText();

            if(!Objects.equals(out, "")){
                out += "\n";
            }

            out += name;
            out += "\n" + data;

            textPane.setText(out);
        }

        JScrollPane textScroll = new JScrollPane(textPane);
        textScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel textPanel = new JPanel(new BorderLayout());
        JButton btnSaveText = new JButton("Save Output");
        btnSaveText.addActionListener(e -> saveOutputsToFile());

        textPanel.add(textScroll);
        textPanel.add(btnSaveText, BorderLayout.SOUTH);

        if(graphs.size() > 0){
            JTabbedPane mainTab = new JTabbedPane();
            JTabbedPane graphTab = new JTabbedPane();

            for(ChartPanel g : graphs){
                JPanel graphPane = new JPanel(new BorderLayout());
                JButton btnSave = new JButton("Save Graph");
                btnSave.addActionListener(e -> saveGraph(g));

                graphPane.add(g);
                graphPane.add(btnSave, BorderLayout.SOUTH);

                graphTab.addTab(g.getName(), graphPane);
            }

            mainTab.addTab("Outputs", textPanel);
            mainTab.addTab("Graphs", graphTab);

            frame.add(mainTab);
        }else{
            frame.add(textPanel);
        }

        frame.setVisible(true);

        textScroll.getViewport().setViewPosition(new Point(0,0));
        textScroll.getVerticalScrollBar().setValue(0);

        textPane.setSelectionStart(0);
        textPane.setSelectionEnd(0);

        frame.repaint();
    }

    /**
     * It adds a graph to the list of graphs
     *
     * @param graph The graph to be added to the list of graphs.
     */
    public static void addGraph(ChartPanel graph){
        graphs.add(graph);
    }

    /**
     * It clears the output
     */
    public static void clearOutput(){
        outputs.clear();
        graphs.clear();
    }

    /**
     * This function will open a file browser and allow the user to select where to save the output files
     */
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

    /**
     * This function displays a dialog box that allows the user to select which outputs they want to save to a file
     */
    private static void displayOutputSelection(){
        boolean[] outputSelections = new boolean[outputs.size()];

        JPanel[] selectors = new JPanel[outputSelections.length];

        int i = 0;

        //Build list of selectors to display
        for(String s : outputs){
            String name = s.split(",")[0];
            JLabel label = new JLabel(name);
            JCheckBox selectionBox = new JCheckBox();
            selectionBox.setName("sel");

            int finalI = i;
            selectionBox.addActionListener(e -> outputSelections[finalI] = selectionBox.isSelected());

            outputSelections[finalI] = true;
            selectionBox.setSelected(true);

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

        JDialog dialog = new JDialog();

        for(JPanel p : selectors){
            selectionPanel.add(p);
        }

        content.add(selectionPanel, BorderLayout.CENTER);

        //Add Save button to panel
        JButton btnSave = new JButton("Save Outputs");
        btnSave.addActionListener(e -> {
            displayFileBrowser(outputSelections);
            dialog.dispose();
        });

        content.add(btnSave, BorderLayout.SOUTH);

        content.revalidate();
        content.repaint();

        dialog.setContentPane(content);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.getRootPane().setDefaultButton(btnSave);

        dialog.pack();

        dialog.setLocationRelativeTo(GUIDataMaster.getFrameReference());

        dialog.setVisible(true);
    }

    /**
     * It creates a file browser that only allows the user to select a file in a specific folder
     *
     * @param selectedOutputs boolean array of the selected outputs
     */
    private static void displayFileBrowser(boolean[] selectedOutputs) {
        //File browser stuff
        String singleFolder = UserSettings.getWorkingDirectory() + "/" + Constants.EXPORT_FOLDER;
        File root = new File(singleFolder);
        FileSystemView fsv = new SingleRootFileSystemView(root);
        JFileChooser fileChooser = new JFileChooser(fsv);
        //JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(singleFolder));
        FileNameExtensionFilter filtercsv = new FileNameExtensionFilter(
                "Comma Separated (*.csv)", "csv");
        FileNameExtensionFilter filtertsv = new FileNameExtensionFilter(
                "Tab Separated (*.tsv)", "tsv");
        FileNameExtensionFilter filtertxt = new FileNameExtensionFilter(
                "Text File (*.txt)", "txt");
        fileChooser.setFileFilter(filtercsv);
        fileChooser.addChoosableFileFilter(filtertsv);
        fileChooser.addChoosableFileFilter(filtertxt);

        JDialog dialog = new JDialog();

        int result = fileChooser.showSaveDialog(dialog);

        dialog.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            String format = fileChooser.getFileFilter().getDescription().split("\\.")[1].replace(")", "");

            if (!FilenameUtils.getExtension(selectedFile.getName()).equalsIgnoreCase(format)) {
                selectedFile = new File(selectedFile.getParentFile(), FilenameUtils.getBaseName(selectedFile.getName()) + "." + format);
            }

            prepareAndSaveFile(selectedFile.getAbsolutePath(), selectedOutputs);
        }
        dialog.dispose();
    }

    /**
     * It takes the file location and the boolean array of selected outputs, and then it creates a string builder, and then
     * it loops through the boolean array, and if the boolean is true, it gets the name of the output, and then it splits
     * the output by commas, and then it copies the array of strings from the second element to the end, and then it
     * appends the name of the output to the string builder, and then it loops through the array of strings, and it removes
     * the brackets from the beginning and end of the string, and then it replaces the new line characters with commas, and
     * then it appends the string to the string builder, and then it appends a comma to the string builder, and then it
     * appends a new line character to the string builder, and then it writes the string builder to the file, and then it
     * closes the file
     *
     * @param fileLocation The location of the file to be saved.
     * @param selectedOutputs A boolean array that is the same length as the number of outputs. If the value at a given
     * index is true, then the output at that index will be included in the output file.
     */
    private static void prepareAndSaveFile(String fileLocation, boolean[] selectedOutputs){

        StringBuilder outputFileString = new StringBuilder();

        String separator = ",";

        if(FilenameUtils.getExtension(fileLocation).equals("tsv")){
            separator = "\t";
        }

        int i = 0;
        for(boolean b : selectedOutputs){
            if(b){
                String name = outputs.get(i).split(",")[0];
                String[] data = outputs.get(i).split(",");
                    data = Arrays.copyOfRange(data, 1, data.length);

                outputFileString.append(name).append(separator);

                int j = 0;
                for(String s: data){
                    String l = s;
                    if(l.startsWith("[")){
                        l = l.replace("[", "");
                    }

                    if(l.endsWith("]")){
                        l = l.replace("]", "");
                    }

                    l = l.replace("\n", separator);

                    outputFileString.append(l);

                    if(j != data.length-1){
                        outputFileString.append(separator);
                    }

                    j++;
                }

                outputFileString.append("\n");
            }
            i++;
        }

        try {
            FileWriter outFile = new FileWriter(fileLocation);
            outFile.write(outputFileString.toString());
            outFile.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * It creates a JFileChooser that only allows you to save files in a specific folder
     *
     * @param panel The ChartPanel object that contains the chart you want to save.
     */
    private static void saveGraph(ChartPanel panel){
        String singleFolder = UserSettings.getWorkingDirectory() + "/" + Constants.GRAPH_OUTPUT_FOLDER;
        File root = new File(singleFolder);
        FileSystemView fsv = new SingleRootFileSystemView(root);
        JFileChooser fileChooser = new JFileChooser(fsv);
        //JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(singleFolder));
        FileNameExtensionFilter filterpng = new FileNameExtensionFilter(
                "PNG (*.png)", "png");
        FileNameExtensionFilter filterjpeg = new FileNameExtensionFilter(
                "JPEG (*.jpg)", "jpg");
        fileChooser.setFileFilter(filterpng);
        fileChooser.addChoosableFileFilter(filterjpeg);
        JDialog dialog = new JDialog();

        int result = fileChooser.showSaveDialog(dialog);

        dialog.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            String format = fileChooser.getFileFilter().getDescription().split("\\.")[1].replace(")", "");

            if (!FilenameUtils.getExtension(selectedFile.getName()).equalsIgnoreCase(format)) {
                selectedFile = new File(selectedFile.getParentFile(), FilenameUtils.getBaseName(selectedFile.getName())+"."+format);
            }

            try {
                if(format.equals("png")) {
                    OutputStream out = new FileOutputStream(selectedFile);
                    ChartUtils.writeChartAsPNG(out, panel.getChart(), panel.getWidth(), panel.getHeight());
                    out.close();
                }
                else
                    ChartUtils.saveChartAsJPEG(selectedFile, panel.getChart(), panel.getWidth(), panel.getHeight());

            }catch(IOException e){
                e.printStackTrace();
            }
        }

        dialog.dispose();
    }
}
