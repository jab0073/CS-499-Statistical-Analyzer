package Measures;

import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
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
    public double function(DataSet inputData) {
        DoubleColumn dc = DoubleColumn.create("", inputData.getDataAsDouble(true));
        return dc.removeMissing().standardDeviation();
    }
}
