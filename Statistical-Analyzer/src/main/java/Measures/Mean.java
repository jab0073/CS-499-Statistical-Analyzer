package Measures;
import Interfaces.IMeasure;
import tech.tablesaw.api.DoubleColumn;

public class Mean implements IMeasure {
    private String name = "mean";

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
        return inputData.removeMissing().mean();
    }
}
