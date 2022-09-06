package Utilities;

import Interfaces.IMeasure;
import tech.tablesaw.api.DoubleColumn;

import java.lang.reflect.InvocationTargetException;

import static Utilities.ClassMap.getMeasureClass;

public final class FunctionCaller {

    public static Object measureRunner(String measureName, DataSet arg) {
        Class<? extends IMeasure> measure = getMeasureClass(measureName);
        IMeasure msr;
        try {
            msr = measure.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return msr.function(arg);
    }

}
