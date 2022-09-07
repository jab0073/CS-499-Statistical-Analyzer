import BackEndUtilities.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataSet ds = new DataSet();
        ds.addData(Arrays.asList("x*12", "y/3", "y*12", "x/3", "12", "19", "72"));
        ds.addVariables(Arrays.asList("x=3", "y=12"));
        double output = (double) FunctionCaller.measureRunner("standard deviation", ds);

        List<Double> value = Expressions.eval(ds);
        value.sort(Comparator.naturalOrder());
        value.forEach(System.out::println);
        System.out.println("standard deviation: " + output);
    }
}
