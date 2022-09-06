package Measures;
import Interfaces.IMeasure;
import Utilities.DataSet;
import tech.tablesaw.api.DoubleColumn;

import java.util.HashMap;

public class Mode implements IMeasure {
    private String name = "mode";

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double function(DataSet inputData) {
        HashMap<Double, Integer> map = new HashMap<>();
        double result = -999, max = 1;
        for (double arrayItem : inputData.getDataAsDouble()) {
            if (map.putIfAbsent(arrayItem, 1) != null) {
                int count = map.get(arrayItem) + 1;
                map.put(arrayItem, count);
                if (count > max) {
                    max = count;
                    result = arrayItem;
                }
            }
        }
        return result;
    }
}
