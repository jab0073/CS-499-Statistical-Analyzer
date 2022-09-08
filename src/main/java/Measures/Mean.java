package Measures;
import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Measure to calculate Mean
 */
public class Mean implements IMeasureBigDecimal {
    private String name = "mean";

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
        return inputData.getDataAsDouble(true).stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(inputData.getSize()), RoundingMode.HALF_UP);
    }
}
