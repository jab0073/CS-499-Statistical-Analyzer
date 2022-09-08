package Measures;

import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class StdDiv implements IMeasureBigDecimal {
    private String name = "standard deviation";

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
        Variance vnc = new Variance();
        return vnc.function(inputData).sqrt(new MathContext(10, RoundingMode.HALF_UP));
    }
}
