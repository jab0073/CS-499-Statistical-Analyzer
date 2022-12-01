package Respository;

import Constants.Constants;
import FrontEndUtilities.GUIDataMaster;
import FrontEndUtilities.GUIMeasure;
import Managers.SaveManager;
import Settings.UserSettings;
import TableUtilities.DataTable;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class FileSystemRepository {

    private static final Logger logger = LogManager.getLogger(FileSystemRepository.class.getName());

    public static void ensurePath(String path) {
        Path pathy = Paths.get(path);
        if (!Files.exists(pathy)) {
            try {
                Files.createDirectories(pathy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean put(String fileName, String folderName) {
        ensurePath(folderName);
        Path targetName = getFileName(fileName, folderName);

        ArrayList<GUIMeasure> guiMeasures = GUIDataMaster.getAllMeasures();
        DataTable table = GUIDataMaster.getFrameReference().getCellsTable().getTableAsDT();

        SaveManager.SaveObject saveObject = new SaveManager.SaveObject(guiMeasures, table);
        try {
            Writer writer = new FileWriter(targetName.toString());
            SaveManager.getGson().toJson(saveObject, writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void get(String fileName, String folderName) {
        ensurePath(folderName);
        Path targetName = getFileName(fileName, folderName);

        try {
            SaveManager.SaveObject input = SaveManager.getGson().fromJson(new FileReader(targetName.toString()), SaveManager.SaveObject.class);

            GUIDataMaster.removeAllMeasures();

            input.measures.forEach(GUIDataMaster::addMeasure);

            GUIDataMaster.getFrameReference().getCellsTable().loadFromDT(input.table);

            SaveManager.setCurrentSaveFileName(FilenameUtils.getBaseName(targetName.getFileName().toString()));
            SaveManager.setStateCurrentlySaved(true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getFileName(String name, String folder) {
        return Paths.get(folder + "/" + name + ".sasf");
    }

    public void init() {
        ensurePath(UserSettings.getWorkingDirectory());
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.GRAPH_OUTPUT_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.UDM_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.PROJECTS_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.EXPORT_FOLDER);
    }

    public void putFile(File file, String fileID, String folder) {
        ensurePath(UserSettings.getWorkingDirectory() + folder);
        try {
            Files.copy(file.toPath(), Paths.get(UserSettings.getWorkingDirectory() + folder + "/" + fileID), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile(String fileID, String folder) {
        Path p = Paths.get(UserSettings.getWorkingDirectory() + folder + "/" + fileID);
        if (Files.exists(p))
            return Paths.get(UserSettings.getWorkingDirectory() + folder + "/" + fileID).toFile();
        return null;
    }

    public boolean deleteFile(String fileID, String folder) {
        Path p = Paths.get(folder + "/" + fileID);
        if (Files.exists(p)) {
            try {
                Files.delete(p);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
