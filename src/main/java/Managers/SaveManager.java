package Managers;

import BackEndUtilities.Sample;
import Constants.Constants;
import Enums.GraphTypes;
import FrontEndUtilities.GUIDataMaster;
import FrontEndUtilities.GUIMeasure;
import GUI.SingleRootFileSystemView;
import Settings.UserSettings;
import TableUtilities.Cell;
import TableUtilities.DataTable;
import TableUtilities.Row;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * A class that handles saving and loading of the program state
 */
public class SaveManager {
    private static final Gson gson = new Gson();
    private static boolean stateCurrentlySaved = false;
    private static String currentSaveFileName = "";

    public static Gson getGson() {
        return SaveManager.gson;
    }

    public static void setStateCurrentlySaved(boolean stateCurrentlySaved) {
        SaveManager.stateCurrentlySaved = stateCurrentlySaved;
    }

    public static void setCurrentSaveFileName(String currentSaveFileName) {
        SaveManager.currentSaveFileName = currentSaveFileName;
        GUIDataMaster.getFrameReference().changeStatus(SaveManager.currentSaveFileName);
    }

    /**
     * If the user has already saved a state, then save the state to the same file. If the user has not saved a state, then
     * prompt the user for a file name and save the state to that file
     *
     * @param saveNew If true, the user will be prompted to enter a new file name. If false, the current save file name
     * will be used.
     */
    public static void saveProgramState(boolean saveNew){
        if(stateCurrentlySaved && !saveNew){
            RepositoryManager.putSaveState(currentSaveFileName.replace(".wasp", ""));
        }else{
            String fileName = JOptionPane.showInputDialog(GUIDataMaster.getFrameReference(), "Save File Name");
            if(fileName != null) {
                RepositoryManager.putSaveState(fileName);
            }
        }
    }

    /**
     * Open a file chooser dialog, and if the user selects a file, load the save state with the same name as the file.
     */
    public static void openSaveFile(){
        File root = new File(UserSettings.getWorkingDirectory() + "/" + Constants.PROJECTS_FOLDER);
        FileSystemView fsv = new SingleRootFileSystemView(root);
        JFileChooser fileChooser = new JFileChooser(fsv);

        //JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(UserSettings.getWorkingDirectory() + "/" + Constants.PROJECTS_FOLDER));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Statistical Analyzer Save (*.wasp)", "wasp");
        fileChooser.setFileFilter(filter);

        JDialog dialog = new JDialog();

        int result = fileChooser.showOpenDialog(dialog);

        dialog.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            RepositoryManager.getSaveState(FilenameUtils.getBaseName(selectedFile.getName()));
        }
        dialog.dispose();
    }

    /**
     * Object used is saving and loading of program state
     */
    public static class SaveObject{
        //The list of unique datasets each measure contains
        private final ArrayList<String> measureDataSet = new ArrayList<>();

        //The list of each measure and their associated datasets. Pattern is "name,ds0=Index,ds1=Index,...,var0=name=value,var0=name=value...,graph=name
        private final ArrayList<String> measures = new ArrayList<>();

        //Datatable for the cellsTable
        public String table;

        public SaveObject(ArrayList<GUIMeasure> m, DataTable t){

            for(GUIMeasure measure : m){
                //Array of dataSet indexes
                int[] dataIndexes = new int[measure.getMinimumSamples()];

                //Add unique datasets to the list of datasets and store indexes to each measure's associated datasets
                for(int i = 0; i < measure.getMinimumSamples(); i++){
                    String data = measure.getDataAsString(i);

                    if(measureDataSet.contains(data)){
                        dataIndexes[i] = measureDataSet.indexOf(data);
                    }else{
                        measureDataSet.add(data);
                        dataIndexes[i] = measureDataSet.size()-1;
                    }

                }

                //Begin building measure save string
                StringBuilder measureString = new StringBuilder(measure.getName());

                for(int i = 0; i < dataIndexes.length; i++){
                    measureString.append(",ds").append(i).append("=").append(dataIndexes[i]);
                }

                for(int i = 0; i < measure.getNumVariables(); i++){
                    String varName = measure.getVariableName(i);
                    String varVal = measure.getVariableValue(i);

                    measureString.append(",var").append(i).append("=").append(varName).append("=").append(varVal);
                }

                if(measure.getSelectedGraph() != null){
                    measureString.append(",graph=").append(measure.getSelectedGraph().toString());
                }

                measures.add(measureString.toString());
            }

            table = dtToCSV(t);

        }

        public ArrayList<GUIMeasure> buildMeasures(){
            ArrayList<GUIMeasure> newMeasures = new ArrayList<>();

            for(String measureString : measures){
                String[] measureComponents = measureString.split(",");

                GUIMeasure measure = new GUIMeasure(measureComponents[0]);

                //loop over each subsequent component of the measure string and assign the values to the GUIMeasure accordingly
                for(int i = 1; i < measureComponents.length; i++){
                    //Starting with d implies this is a dataset
                    if(measureComponents[i].startsWith("d")){
                        String[] dataSetComponents = measureComponents[i].split("\u003d");

                        //Get the dataset number from the component
                        int datasetNumber = Integer.parseInt(dataSetComponents[0].replaceAll("[^0-9]", ""));

                        //Get the index of the dataset
                        int index = Integer.parseInt(dataSetComponents[1]);

                        //Store the dataset to the measure
                        measure.addData(false, datasetNumber, measureDataSet.get(index).split(","));

                        continue;
                    }

                    //Starting with v implies this is a variable
                    if(measureComponents[i].startsWith("v")){
                        String[] variableComponents = measureComponents[i].split("\u003d");

                        //Get the variable number from the component
                        int varNumber = Integer.parseInt(variableComponents[0].replaceAll("[^0-9]", ""));

                        //Get the variable name
                        String varName = variableComponents[1];

                        //Get the value of the variable
                        String value = variableComponents[2];

                        //Store the variable to the measure
                        measure.setVariable(varName, value);

                        continue;
                    }

                    //Starting with g implies this is a graph
                    if(measureComponents[i].startsWith("g")){
                        String[] graphComponents = measureComponents[i].split("\u003d");

                        //Store the graph type to the measure
                        measure.setSelectedGraph(GraphTypes.valueOf(graphComponents[1]));

                    }
                }

                newMeasures.add(measure);

            }

            return newMeasures;
        }

        /**
         * converts the CSV style String table field to a DataTable
         *
         * @return A DataTable object.
         */
        public DataTable tableAsDT() {
            DataTable dt = new DataTable();
            try (CSVReader reader = new CSVReader(new StringReader(table))) {
                List<String[]> r = reader.readAll();
                r.forEach(x -> {
                    Sample sample = new Sample();
                    sample.addData(Arrays.asList(x));
                    int index = r.indexOf(x);
                    Row row = new Row(index, sample);
                    dt.getRows().add(row.clone());
                });
            } catch (IOException | CsvException e) {
                return null;
            }
            return dt;
        }

        private String dtToCSV(DataTable dt){
            StringBuilder csv = new StringBuilder();

            for(Row r : dt.getRows()){
                for(Cell c : r.getCells()){
                    String val = c.data;

                    if(val == null){
                        csv.append(" ");
                    }

                    if(isNumeric(val)){
                        csv.append(val);
                    }else{
                        csv.append('"').append(val).append('"');
                    }

                    csv.append(',');
                }

                csv.append('\n');
            }

            return csv.toString();
        }

        /**
         * Checks if a string is numeric
         * @param str the string to analyze
         * @return whether the string is numeric
         */
        private boolean isNumeric(String str){
            if(str == null){
                return false;
            }
            try{
                double d = Double.parseDouble(str);
            }catch(NumberFormatException e){
                return false;
            }

            return true;
        }

    }
}
