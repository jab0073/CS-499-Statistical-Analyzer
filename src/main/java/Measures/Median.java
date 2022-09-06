package Measures;
import Interfaces.IMeasure;
import Utilities.DataSet;
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
    public double function(DataSet inputData) {
        DoubleColumn dc = DoubleColumn.create("", inputData.getDataAsDouble());
        return dc.removeMissing().median();
    }
}
