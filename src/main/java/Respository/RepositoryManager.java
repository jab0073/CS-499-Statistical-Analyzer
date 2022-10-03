package Respository;

import BackEndUtilities.Constants;
import Interfaces.IStorage;
import Measures.UserDefinedMeasure;
import Settings.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RepositoryManager {
    public static List<UserDefinedMeasure> loadedUserDefinedMeasures;
    private static final Logger logger = LogManager.getLogger(RepositoryManager.class.getName());

    private static IStorage storage;

    public static List<UserDefinedMeasure> getAllUserDefinedMeasures() {
        return loadedUserDefinedMeasures;
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

    public static UserDefinedMeasure getUserDefinedMeasure(String name) {
        return loadedUserDefinedMeasures.stream().filter(udm->udm.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static UserDefinedMeasure getUserDefinedMeasure(int index) {
        return loadedUserDefinedMeasures.get(index);
    }

    public static boolean hasUserDefinedMeasure(String name) {
        return loadedUserDefinedMeasures.stream().map(udm -> udm.getName().equalsIgnoreCase(name)).findAny().isEmpty();
    }

    public static UserDefinedMeasure putUserDefinedMeasure(UserDefinedMeasure udm, String name) {
        if (udm == null) throw new AssertionError("This method must be called with a User Defined Measure, even if it is an empty instance");
        UserDefinedMeasure udm2 = getUserDefinedMeasure(name);  //If the udm already exists, get the instance

        if (udm2 == null) {   //If it does not, make a new one and add it to loaded user defined measures
            udm2 = udm;
            loadedUserDefinedMeasures.add(udm2);
        }						//If the persistent rule was preexisting, change contents to the new rule
        storage.put(udm2, getUDMFolderPath());
        return udm2;
    }

    private static String getUDMFolderPath()
    {
        return UserSettings.getWorkingDirectory() + "/" + Constants.MAIN_FOLDER + "/" + Constants.UDM_FOLDER;
    }

    public static UserDefinedMeasure getUserDefinedMeasureByName (String name){
        return  loadedUserDefinedMeasures.stream().filter(udm-> udm.getName().trim().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static void deleteUserDefinedName(String name) {
        UserDefinedMeasure udm = getUserDefinedMeasure(name);
        if (udm != null && udm.getExpression() != null){
            storage.deleteFile(name, getUDMFolderPath(),  true);
            loadedUserDefinedMeasures.remove(udm);
        }
    }

    public static void removeFromLoadedUserDefinedMeasures(String name) {
        UserDefinedMeasure udm = getUserDefinedMeasure(name);
        if (udm != null){
            loadedUserDefinedMeasures.remove(udm);
        }
    }

    public static void init() {
        loadedUserDefinedMeasures = new ArrayList<>();
        storage = new FileSystemRepository();
        storage.init();
        loadedUserDefinedMeasures.addAll(storage.loadUserDefinedMeasures(getUDMFolderPath()));
    }
}
