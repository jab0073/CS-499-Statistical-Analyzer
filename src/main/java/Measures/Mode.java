package Measures;
import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;
import java.util.HashMap;

public class Mode implements IMeasureBigDecimal {
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
    public BigDecimal function(DataSet inputData) {
        HashMap<BigDecimal, Integer> map = new HashMap<>();
        BigDecimal result = BigDecimal.valueOf(-999), max = BigDecimal.ONE;
        for (BigDecimal arrayItem : inputData.getDataAsDouble(true)) {
            if (map.putIfAbsent(arrayItem, 1) != null) {
                int count = map.get(arrayItem) + 1;
                map.put(arrayItem, count);
                if (count > max.doubleValue()) {
                    max = BigDecimal.valueOf(count);
                    result = arrayItem;
                }
            }
        }
        return result;
    }
}
