import Interfaces.IMeasure;
import tech.tablesaw.api.DoubleColumn;
import Utilities.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        DataSet ds = new DataSet();
        ds.addData(Stream.of(1.0, 2.0, 3.0, 4.0, 5.0, 3.0, 3.0, 1.0, 6.0, 9.0).map(String::valueOf).collect(Collectors.toList()));
        double output = (double) FunctionCaller.measureRunner("mode", ds);
        double value = Expressions.eval("5*y", "y=" + output);
        System.out.println(value);
    }
}
