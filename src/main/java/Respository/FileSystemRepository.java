package Respository;

import BackEndUtilities.Constants;
import Interfaces.IStorage;
import Measures.UserDefinedMeasure;
import Settings.UserSettings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileSystemRepository implements IStorage {

    @Override
    public boolean put(UserDefinedMeasure udm, String folder) {
        ensurePath(folder);
        Path targetName = getFileName(udm.getName(), folder);

        return udm.saveToFile(targetName.toString());

    }

    @Override
    public UserDefinedMeasure get(String name, String folder) {
        Path targetName = getFileName(name, folder);
        if (Files.exists(targetName)) {
            return UserDefinedMeasure.loadFromFile(targetName.toString());
        }
        return null;
    }

    private Path getFileName(String name, String folder) {
        return Paths.get(folder + "/" + name + ".json");
    }

    @Override
    public void init() {
        System.out.println("Working directory set at " + UserSettings.getWorkingDirectory());
        ensurePath(UserSettings.getWorkingDirectory());
        ensurePath(UserSettings.getWorkingDirectory() + Constants.MAIN_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + Constants.MAIN_FOLDER + "/" + Constants.GRAPH_OUTPUT_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + Constants.MAIN_FOLDER + "/" + Constants.UDM_FOLDER);
    }

    @Override
    public List<UserDefinedMeasure> loadUserDefinedMeasures(String folder) {
        Gson gson = new Gson();
        try {
            return Files.list(Paths.get(folder))
                    .map(path -> {
                        if (path.toString().toLowerCase().endsWith(".json"))
                            return UserDefinedMeasure.loadFromFile(path.toString());
                        //return gson.fromJson(new JsonReader(new FileReader(path.toString())), UserDefinedMeasure.class);
                        return null;
                    }).filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void ensurePath(String path) {
        Path pathy = Paths.get(path);
        if (!Files.exists(pathy)) {
            try {
                Files.createDirectory(pathy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void putFile(File file, String fileID, String folder) {
        ensurePath(UserSettings.getWorkingDirectory() + folder);
        try {
            Files.copy(file.toPath(), Paths.get(UserSettings.getWorkingDirectory() + folder + "/" + fileID), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public File getFile(String fileID, String folder) {
        Path p = Paths.get(UserSettings.getWorkingDirectory() + folder + "/" + fileID);
        if (Files.exists(p))
            return Paths.get(UserSettings.getWorkingDirectory() + folder + "/" + fileID).toFile();
        return null;
    }

    @Override
    public boolean deleteFile(String fileID, String folder, boolean softDelete) {
        Path p = Paths.get(folder + "/" + fileID + ".json"); // Removed version
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
