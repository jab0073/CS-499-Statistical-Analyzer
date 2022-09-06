package Measures;
import Interfaces.IMeasure;
import Utilities.DataSet;
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
    public double function(DataSet inputData) {
        DoubleColumn dc = DoubleColumn.create("", inputData.getDataAsDouble());
        return dc.removeMissing().mean();
    }
}
