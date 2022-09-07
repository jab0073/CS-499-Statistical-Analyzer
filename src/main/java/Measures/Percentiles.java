package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;

public class Percentiles implements IMeasure {
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
    public double function(DataSet inputData) {
        //TODO: define function for percentile
        return 0.0;
    }
}
