package BackEndUtilities;

import Interfaces.IMeasure;
import Respository.RepositoryManager;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MeasureManager {
    private static final ArrayList<IMeasure> measures = new ArrayList<>();

    public static void init() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String packageName = "Measures";

        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends IMeasure>> set = new HashSet<>(reflections.getSubTypesOf(IMeasure.class));

        for(Class<? extends IMeasure> c : set){
            IMeasure a = c.getDeclaredConstructor().newInstance();
            measures.add(a);
        }

        measures.addAll(RepositoryManager.getAllUserDefinedMeasures());
    }

    /**
     * retrieves the instance of the measure of a given name
     * @param measureName the name of the measure to search for
     * @return if measure is found, return the measure, otherwise return null
     */
    public static IMeasure getMeasure(String measureName){
        return measures.stream().filter(measure -> measureName.equals(measure.getName())).findFirst().orElse(null);
    }
}

