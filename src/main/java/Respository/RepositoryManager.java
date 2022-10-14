package Respository;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import BackEndUtilities.Sample;
import Interfaces.IStorage;
import Interop.UIServices;
import Measures.UserDefinedMeasure;
import Settings.UserSettings;
import TableUtilities.DataTable;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.data.general.DatasetChangeEvent;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryManager {
    public static List<UserDefinedMeasure> loadedUserDefinedMeasures;
    public static List<DataSet> loadedDataSets;
    private static final Logger logger = LogManager.getLogger(RepositoryManager.class.getName());

    private static IStorage storage;

    public static List<UserDefinedMeasure> getAllUserDefinedMeasures() {
        return loadedUserDefinedMeasures;
    }

    public static List<DataSet> getAllDataSets() {
        return loadedDataSets;
    }

    public static UserDefinedMeasure getUserDefinedMeasureFromStorage(String name) {
        String folder = getUDMFolderPath();
        return loadedUserDefinedMeasures.stream().filter(udm->udm.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(()->{
                    try {
                        UserDefinedMeasure r= storage.get(name, folder);
                        loadedUserDefinedMeasures.add(r);
                        return r;
                    } catch (Exception e) {
                        logger.error(e);
                        return null;
                    }
                });
    }

    public static DataSet getDataSetFromStorage(String name) {
        String folder = getDataSetFolderPath();
        return loadedDataSets.stream().filter(ds->ds.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(()->{
                    try {
                        DataSet r= storage.getDataSet(name, folder);
                        loadedDataSets.add(r);
                        return r;
                    } catch (Exception e) {
                        logger.error(e);
                        return null;
                    }
                });
    }

    public static UserDefinedMeasure getUserDefinedMeasure(String name) {
        return loadedUserDefinedMeasures.stream().filter(udm->udm.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static DataSet getDataSet(String name) {
        // loadedDataSets.forEach(d -> System.out.println(d.getName()));
        return loadedDataSets.stream().filter(ds->ds.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static UserDefinedMeasure getUserDefinedMeasure(int index) {
        return loadedUserDefinedMeasures.get(index);
    }

    public static DataSet getDataSet(int index) {
        return loadedDataSets.get(index);
    }

    public static boolean hasUserDefinedMeasure(String name) {
        return loadedUserDefinedMeasures.stream().map(udm -> udm.getName().equalsIgnoreCase(name)).findAny().isPresent();
    }

    public static boolean hasDataSet(String name) {
        return loadedDataSets.stream().map(ds -> ds.getName().equalsIgnoreCase(name)).findAny().isPresent();
    }

    public static UserDefinedMeasure putUserDefinedMeasure(UserDefinedMeasure udm, String name) {
        if (udm == null) throw new AssertionError("This method must be called with a User Defined Measure, even if it is an empty instance");
        UserDefinedMeasure udm2 = getUserDefinedMeasure(name);  //If the udm already exists, get the instance

        if (udm2 == null) {   //If it does not, make a new one and add it to loaded user defined measures
            udm2 = udm;
            loadedUserDefinedMeasures.add(udm2);
        }
        storage.put(udm2, getUDMFolderPath());
        return udm2;
    }

    public static DataSet putDataSet(DataSet ds, String name) {
        if (ds == null) throw new AssertionError("This method must be called with a DataSet, even if it is an empty instance");
        DataSet ds2 = getDataSet(name);  //If the udm already exists, get the instance

        if (ds2 == null) {   //If it does not, make a new one and add it to loaded user defined measures
            ds2 = ds;
            loadedDataSets.add(ds2);
        }
        storage.put(ds2, name, getDataSetFolderPath());
        return ds2;
    }

    private static String getUDMFolderPath()
    {
        return UserSettings.getWorkingDirectory() + "/" + Constants.MAIN_FOLDER + "/" + Constants.UDM_FOLDER;
    }

    private static String getDataSetFolderPath()
    {
        return UserSettings.getWorkingDirectory() + "/" + Constants.MAIN_FOLDER + "/" + Constants.DATASET_FOLDER;
    }

    private static String getExportFolderPath()
    {
        return UserSettings.getWorkingDirectory() + "/" + Constants.MAIN_FOLDER + "/" + Constants.EXPORT_FOLDER;
    }

    public static boolean exportCSV(DataSet ds, String name) {
        String fileName = getExportFolderPath() + "/" + name + ".csv";
        File file = new File(fileName);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            ds.getSamples().forEach(s -> {
                writer.writeNext(s.getData().toArray(String[]::new));
            });

            // closing writer connection
            writer.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean exportCSV(Sample s, String name) {
        String fileName = getExportFolderPath() + "/" + name + ".csv";
        File file = new File(fileName);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            writer.writeNext(s.getData().toArray(String[]::new));

            // closing writer connection
            writer.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static DataSet importCSV(String fileName) {
        DataTable dt = UIServices.fromCSV(fileName);
        if (dt != null)
            return dt.toDataSet();
        return null;
    }

    public static void deleteUserDefinedName(String name) {
        UserDefinedMeasure udm = getUserDefinedMeasure(name);
        if (udm != null && udm.getExpression() != null){
            storage.deleteFile(name, getUDMFolderPath());
            loadedUserDefinedMeasures.remove(udm);
        }
    }

    public static void deleteDataSet(String name) {
        DataSet ds = getDataSet(name);
        if (ds != null && ds.getAllDataAsDouble() != null){
            storage.deleteFile(name, getDataSetFolderPath());
            loadedDataSets.remove(ds);
        }
    }

    public static void removeFromLoadedUserDefinedMeasures(String name) {
        UserDefinedMeasure udm = getUserDefinedMeasure(name);
        if (udm != null){
            loadedUserDefinedMeasures.remove(udm);
        }
    }

    public static void removeFromLoadedDataSets(String name) {
        DataSet ds = getDataSet(name);
        if(ds != null) {
            loadedDataSets.remove(ds);
        }
    }

    public static void init() {
        loadedUserDefinedMeasures = new ArrayList<>();
        loadedDataSets = new ArrayList<>();
        storage = new FileSystemRepository();
        storage.init();
        loadedUserDefinedMeasures.addAll(storage.loadUserDefinedMeasures(getUDMFolderPath()));
        loadedDataSets.addAll(storage.loadDataSets(getDataSetFolderPath()));
    }
}
