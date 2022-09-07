package Measures;

import BackEndUtilities.DataSet;
import Interfaces.IMeasure;

public class Variance implements IMeasure {

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
    public double function(DataSet inputData) {
        Mean mn = new Mean();
        double mean = mn.function(inputData);
        return inputData.getDataAsDouble(true).stream().mapToDouble(d -> Math.pow(d - mean, 2)).sum() / (inputData.getSize() - 1);
    }
}
