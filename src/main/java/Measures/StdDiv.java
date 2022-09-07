package Measures;

import Interfaces.IMeasure;
import BackEndUtilities.DataSet;

public class StdDiv implements IMeasure {
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
    public double function(DataSet inputData) {
        Variance vnc = new Variance();
        return Math.sqrt(vnc.function(inputData));
    }
}
