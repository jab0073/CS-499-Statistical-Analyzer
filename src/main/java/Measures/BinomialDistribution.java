package Measures;
import Interfaces.IMeasure;
import Utilities.DataSet;
import tech.tablesaw.api.DoubleColumn;

public class BinomialDistribution implements IMeasure {
    private String name = "binomial distribution";

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
        //TODO: define function for binomial distribution
        return 0.0;
    }
}
