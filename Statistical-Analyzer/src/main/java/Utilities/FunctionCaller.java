package Utilities;

import Interfaces.IMeasure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static Utilities.ClassMap.getMeasureClass;

public final class FunctionCaller {

    public static Object measureRunner(String measureName, Object... args) {
        Class<? extends IMeasure> measure = getMeasureClass(measureName);
        if(Stream.of(measure.getDeclaredMethods()).map(Method::getName).map(String::toLowerCase).toList().contains("function")) {
            try {
                Method callable = measure.getMethod("function");
                return callable.invoke(callable, args);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
