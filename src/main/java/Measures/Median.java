package Measures;
import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * Measure to calculate Median
 */
public class Median implements IMeasureBigDecimal {
    private String name = "median";

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
        List<BigDecimal> inputCopy = inputData.getDataAsDouble(true).stream().sorted(Comparator.naturalOrder()).toList();

        return inputCopy.get(BigDecimal.valueOf(inputCopy.size() / 2).setScale(0, RoundingMode.HALF_UP).intValue());
    }
}
