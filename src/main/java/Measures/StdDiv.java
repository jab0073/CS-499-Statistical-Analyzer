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
        Mean mn = new Mean();
        double mean = mn.function(inputData);
        return Math.sqrt(inputData.getDataAsDouble(true).stream().mapToDouble(d -> Math.pow(d - mean, 2)).sum() / inputData.getSize());
    }
}
