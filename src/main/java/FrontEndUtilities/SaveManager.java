package FrontEndUtilities;

import Settings.UserSettings;
import TableUtilities.DataTable;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;

public class SaveManager {
    private static final Gson gson = new Gson();
    private static boolean stateCurrentlySaved = false;
    private static String currentSaveFileName = "";


    public static void test(){

        String t = gson.toJson(GUIDataMaster.getGUIMeasure(0));

        System.out.println(t);

        GUIDataMaster.addMeasure(gson.fromJson(t, GUIMeasure.class));
    }

    public static void saveProgramState(){
        if(stateCurrentlySaved){
            saveProgramStateAs(currentSaveFileName);
        }else{
            //Display file selector
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(UserSettings.getWorkingDirectory()));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Statistical Analyzer Save (*.sasf)", "sasf");
            fileChooser.setFileFilter(filter);

            JDialog dialog = new JDialog();

            int result = fileChooser.showSaveDialog(dialog);

            dialog.setVisible(true);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                String format = fileChooser.getFileFilter().getDescription().split("\\.")[1].replace(")", "");

                //If the file name does not have the proper extension
                if (!FilenameUtils.getExtension(selectedFile.getName()).equalsIgnoreCase(format)) {
                    selectedFile = new File(selectedFile.getParentFile(), FilenameUtils.getBaseName(selectedFile.getName()) + "." + format);
                }

                currentSaveFileName = selectedFile.getAbsolutePath();
                stateCurrentlySaved = true;

                saveProgramStateAs(currentSaveFileName);
            }
        }
    }

    public static void saveProgramStateAs(String fName){
        ArrayList<GUIMeasure> guiMeasures = GUIDataMaster.getAllMeasures();
        DataTable table = GUIDataMaster.getFrameReference().getCellsTable().getTableAsDT();

        SaveObject saveObject = new SaveObject(guiMeasures, table);

        String saveJson = gson.toJson(saveObject);

        try {
            FileWriter fileWriter = new FileWriter(fName);

            fileWriter.write(saveJson);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void openSaveFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(UserSettings.getWorkingDirectory()));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Statistical Analyzer Save (*.sasf)", "sasf");
        fileChooser.setFileFilter(filter);

        JDialog dialog = new JDialog();

        int result = fileChooser.showOpenDialog(dialog);

        dialog.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader(selectedFile));
                String inputJson = fileReader.readLine();

                SaveObject input = gson.fromJson(inputJson, SaveObject.class);

                for(GUIMeasure m : input.measures){
                    GUIDataMaster.addMeasure(m);
                }

                GUIDataMaster.getFrameReference().getCellsTable().loadFromDT(input.table);

                currentSaveFileName = selectedFile.getAbsolutePath();
                stateCurrentlySaved = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dialog.dispose();
    }

    private static class SaveObject{
        ArrayList<GUIMeasure> measures;
        DataTable table;

        SaveObject(ArrayList<GUIMeasure> m, DataTable t){
            measures = m;
            table = t;
        }
    }
}
