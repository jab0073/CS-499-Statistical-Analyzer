package BackEndUtilities;

import Interfaces.IMeasure;

import java.lang.reflect.InvocationTargetException;

import static BackEndUtilities.ClassMap.getMeasureClass;

@Deprecated
public final class FunctionCaller {

    /**
     * Takes a measure name and a data set,
     * and returns the result of running the
     * measure on the data set
     *
     * @param measureName the name of the measure
     *                    you want to run.
     * @param arg the data set to be measured
     * @return The result of the measure.
     */
    public static Object measureRunner(String measureName, DataSet arg) {
        Class<? extends IMeasure<?>> measure = getMeasureClass(measureName);
        IMeasure<?> msr;
        try {
            msr = measure != null ? measure.getDeclaredConstructor().newInstance() : null;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return msr != null ? msr.function(arg) : null;

    }

}
