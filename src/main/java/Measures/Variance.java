package Measures;

import BackEndUtilities.DataSet;
import Interfaces.IMeasureBigDecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Variance implements IMeasureBigDecimal {

    String name = "variance";
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigDecimal function(DataSet inputData) {
        Mean mn = new Mean();
        BigDecimal mean = mn.function(inputData);
        return inputData.getDataAsDouble(true)
                .stream()
                .map(d -> BigDecimal.valueOf(Math.pow(d.subtract(mean).doubleValue(), 2)))
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(inputData.getSize() - 1), RoundingMode.HALF_UP);
    }
}
