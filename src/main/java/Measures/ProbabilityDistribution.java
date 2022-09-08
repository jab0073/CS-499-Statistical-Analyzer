package Measures;
import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;

/**
 * Measure to calculate Probability Distribution
 */
public class ProbabilityDistribution implements IMeasureBigDecimal {
    private String name = "probability distribution";

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
        //TODO: define function for probability distribution
        return BigDecimal.ZERO;
    }
}
