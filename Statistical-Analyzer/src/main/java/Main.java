import Interfaces.IMeasure;
import tech.tablesaw.api.DoubleColumn;
import Utilities.*;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DoubleColumn dc = DoubleColumn.create("Column", 1.0,2.0,3.0,4.0,5.0);
        double output = (double) FunctionCaller.measureRunner("mean", dc);
        
        System.out.println(output);
    }
}
