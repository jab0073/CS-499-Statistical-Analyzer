package BackEndUtilities;

import Interfaces.IMeasure;
import Measures.UserDefinedMeasure;
import Respository.RepositoryManager;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class MeasureManager {
    private static final ArrayList<IMeasure> measures = new ArrayList<>();

    /**
     * Gets all the classes that implement the IMeasure interface and creates an instance of each of them
     */
    public static void init() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String packageName = "Measures";

        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends IMeasure>> set = new HashSet<>(reflections.getSubTypesOf(IMeasure.class));

        for(Class<? extends IMeasure> c : set){
            if(!c.getName().equals(UserDefinedMeasure.class.getName())) { // Making sure the UserDefinedMeasure class instance isn't created
                IMeasure a = c.getDeclaredConstructor().newInstance();
                measures.add(a);
            }
        }
        measures.addAll(RepositoryManager.getAllUserDefinedMeasures()); // Adding all the User Defined Measures from the Repository Manager
    }

    /**
     * retrieves the instance of the measure of a given name
     * @param measureName the name of the measure to search for
     * @return if measure is found, return the measure, otherwise return null
     */
    public static IMeasure getMeasure(String measureName){
        return measures.stream().filter(measure -> measureName.equals(measure.getName())).findFirst().orElse(null);
    }

    /***
     * Can be used to retrieve all the valid measure names. This could be used to populate the list of measures in the GUI
     * @return a list of all the measure names
     */
    public static List<String> getAllMeasureNames() {
        return measures.stream().map(IMeasure::getName).sorted(Comparator.comparing(String::toLowerCase)).collect(Collectors.toList());
    }
}

