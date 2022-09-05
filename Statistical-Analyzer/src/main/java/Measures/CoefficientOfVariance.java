package Measures;
import Interfaces.IMeasure;
import tech.tablesaw.api.DoubleColumn;

public class CoefficientOfVariance implements IMeasure {
    private String name = "coefficient of variance";

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
        double stdDiv =  inputData.removeMissing().standardDeviation();
        double mean = inputData.removeMissing().mean();
        return (stdDiv / mean) * 100.0;
    }
}
