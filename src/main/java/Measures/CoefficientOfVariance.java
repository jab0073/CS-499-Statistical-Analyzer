package Measures;
import Interfaces.IMeasure;
import Utilities.DataSet;
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
    public double function(DataSet inputData) {
        DoubleColumn dc = DoubleColumn.create("", inputData.getDataAsDouble());
        double stdDiv = dc.removeMissing().standardDeviation();
        double mean = dc.removeMissing().mean();
        return (stdDiv / mean) * 100.0;
    }
}
