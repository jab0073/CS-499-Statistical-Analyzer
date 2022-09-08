package Measures;
import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;

public class Percentiles implements IMeasureBigDecimal {
    private String name = "percentiles";

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
        //TODO: define function for percentile
        return BigDecimal.ZERO;
    }
}
