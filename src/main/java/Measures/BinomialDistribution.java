package Measures;
import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;

public class BinomialDistribution implements IMeasureBigDecimal {
    private String name = "binomial distribution";

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
        //TODO: define function for binomial distribution
        return BigDecimal.ZERO;
    }
}
