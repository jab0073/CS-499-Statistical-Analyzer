package Respository;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import BackEndUtilities.Sample;
import Interfaces.IStorage;
import Interop.UIServices;
import Measures.Measures;
import Measures.UserDefinedMeasure;
import Settings.UserSettings;
import TableUtilities.DataTable;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileSystemRepository implements IStorage {

    private static final Logger logger = LogManager.getLogger(FileSystemRepository.class.getName());
    @Override
    public boolean put(UserDefinedMeasure udm, String folder) {
        ensurePath(folder);
        Path targetName = getFileName(udm.getName(), folder);
        Gson gson = new Gson();
        try {
            Writer writer = new FileWriter(targetName.toString());
            gson.toJson(udm, writer);
            writer.flush();
            writer.close();
            logger.debug(udm.getName() + " written to " + targetName);
            return true;
        } catch (IOException e) {
            logger.error(udm.getName() + " failed to write to " + targetName);
            return false;
        }
    }

    @Override
    public boolean put(DataSet ds, String name, String folder) {
        ensurePath(folder);
        Path targetName = getFileName(name, folder);
        ds.setName(name);
        Gson gson = new Gson();
        try {
            Writer writer = new FileWriter(targetName.toString());
            gson.toJson(ds, writer);
            writer.flush();
            writer.close();
            logger.debug(name + " written to " + targetName);
        return true;
        } catch (IOException e) {
            logger.error(name + " failed to write to " + targetName);
            return false;
        }
    }

    @Override
    public boolean put(Sample s, String name, String folder) {
        ensurePath(folder);
        Path targetName = getFileName(name, folder);
        Gson gson = new Gson();
        try {
            Writer writer = new FileWriter(targetName.toString());
            gson.toJson(s, writer);
            writer.flush();
            writer.close();
            logger.debug(name + " written to " + targetName);
            return true;
        } catch (IOException e) {
            logger.error(name + " failed to write to " + targetName);
            return false;
        }
    }

    @Override
    public UserDefinedMeasure get(String name, String folder) {
        Path targetName = getFileName(name, folder);
        if (Files.exists(targetName)) {
            Gson gson = new Gson();
            try {
                UserDefinedMeasure obj =  gson.fromJson(new FileReader(targetName.toString()), UserDefinedMeasure.class);
                logger.debug("Successfully loaded measure from " + targetName);
                return obj;
            } catch (FileNotFoundException e) {
                logger.error("!!! Failed to load measure from " + targetName);
                return null;
            }
        }
        return null;
    }

    @Override
    public DataSet getDataSet(String name, String folder) {
        Path targetName = getFileName(name, folder);
        if (Files.exists(targetName)) {
            Gson gson = new Gson();
            try {
                DataSet obj =  gson.fromJson(new FileReader(targetName.toString()), DataSet.class);
                logger.debug("Successfully loaded dataset from " + targetName);
                return obj;
            } catch (FileNotFoundException e) {
                logger.error("!!! Failed to load dataset from " + targetName);
                return null;
            }
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
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.GRAPH_OUTPUT_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.UDM_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.DATASET_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.SAMPLE_FOLDER);
        ensurePath(UserSettings.getWorkingDirectory() + "/" + Constants.EXPORT_FOLDER);
    }

    @Override
    public List<UserDefinedMeasure> loadUserDefinedMeasures(String folder) {
        try {
            return Files.list(Paths.get(folder))
                    .map(path -> {
                        if (path.toString().toLowerCase().endsWith(".json")) {
                            Gson gson = new Gson();
                            try {
                                UserDefinedMeasure obj =  gson.fromJson(new FileReader(path.toString()), UserDefinedMeasure.class);
                                logger.debug("Successfully loaded measure from " + path);
                                return obj;
                            } catch (FileNotFoundException e) {
                                logger.error("!!! Failed to load measure from " + path);
                                return null;
                            }
                        }
                        return null;
                    }).filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DataSet> loadDataSets(String folder) {
        try {
            return Files.list(Paths.get(folder))
                    .map(path -> {
                        if (path.toString().toLowerCase().endsWith(".json")) {
                            Gson gson = new Gson();
                            try {
                                DataSet obj =  gson.fromJson(new FileReader(path.toString()), DataSet.class);
                                logger.debug("Successfully loaded dataset from " + path);
                                return obj;
                            } catch (FileNotFoundException e) {
                                logger.error("!!! Failed to load dataset from " + path);
                                return null;
                            }
                        }
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
                Files.createDirectories(pathy);
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
