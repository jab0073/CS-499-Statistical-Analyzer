package Measures;
import Interfaces.IMeasure;
import tech.tablesaw.api.DoubleColumn;

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
    public double function(DoubleColumn inputData) {
        return inputData.removeMissing().standardDeviation();
    }
}
