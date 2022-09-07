package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;

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
        StdDiv stddiv = new StdDiv();
        Mean mn = new Mean();

        return (stddiv.function(inputData) / mn.function(inputData)) * 100.0;
    }
}
