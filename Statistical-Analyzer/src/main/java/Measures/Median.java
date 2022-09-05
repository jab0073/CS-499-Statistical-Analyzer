package Measures;
import Interfaces.IMeasure;
import tech.tablesaw.api.DoubleColumn;

public class Median implements IMeasure {
    private String name = "median";

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
        return inputData.removeMissing().median();
    }
}
