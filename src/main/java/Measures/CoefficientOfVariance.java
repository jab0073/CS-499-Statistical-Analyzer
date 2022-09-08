package Measures;
import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Measure to calculate Coefficient of Variance
 */
public class CoefficientOfVariance implements IMeasureBigDecimal {
    private String name = "coefficient of variance";

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
        StdDiv stddiv = new StdDiv();
        Mean mn = new Mean();

        return (stddiv.function(inputData).divide(mn.function(inputData), RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100.0));
    }
}
