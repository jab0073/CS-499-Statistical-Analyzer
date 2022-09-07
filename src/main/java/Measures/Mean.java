package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;

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
    public double function(DataSet inputData) {

        return inputData.getDataAsDouble(true).stream().mapToDouble(f -> f).sum() / inputData.getSize();
    }
}
