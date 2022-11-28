package FrontEndUtilities;

import BackEndUtilities.Constants;
import GUI.SingleRootFileSystemView;
import Respository.FileSystemRepository;
import Respository.RepositoryManager;
import Settings.UserSettings;
import TableUtilities.DataTable;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;

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

    public static boolean isStateCurrentlySaved() {
        return SaveManager.stateCurrentlySaved;
    }

    public static String getCurrentSaveFileName() {
        return SaveManager.currentSaveFileName;
    }

    public static void setCurrentSaveFileName(String currentSaveFileName) {
        SaveManager.currentSaveFileName = currentSaveFileName;
        GUIDataMaster.getFrameReference().changeStatus(SaveManager.currentSaveFileName);
    }

    public static void saveProgramState(boolean saveNew){
        if(stateCurrentlySaved && !saveNew){
            RepositoryManager.putSaveState(currentSaveFileName.replace(".sasf", ""));
        }else{
            String fileName = JOptionPane.showInputDialog(GUIDataMaster.getFrameReference(), "Save File Name");
            if(fileName != null) {
                RepositoryManager.putSaveState(fileName);
            }
        }
    }

    @Deprecated
    public static boolean saveProgramStateAs(String fName){
        ArrayList<GUIMeasure> guiMeasures = GUIDataMaster.getAllMeasures();
        DataTable table = GUIDataMaster.getFrameReference().getCellsTable().getTableAsDT();

        SaveObject saveObject = new SaveObject(guiMeasures, table);

        String saveJson = gson.toJson(saveObject);

        try {
            FileWriter fileWriter = new FileWriter(fName);
            fileWriter.write(saveJson);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void openSaveFile(){
        File root = new File(UserSettings.getWorkingDirectory() + "/" + Constants.PROJECTS_FOLDER);
        FileSystemView fsv = new SingleRootFileSystemView(root);
        JFileChooser fileChooser = new JFileChooser(fsv);

        //JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(UserSettings.getWorkingDirectory() + "/" + Constants.PROJECTS_FOLDER));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Statistical Analyzer Save (*.sasf)", "sasf");
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

    public static class SaveObject{
        public ArrayList<GUIMeasure> measures;
        public DataTable table;

        public SaveObject(ArrayList<GUIMeasure> m, DataTable t){
            measures = m;
            table = t;
        }
    }
}
