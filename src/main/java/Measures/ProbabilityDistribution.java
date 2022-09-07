package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;

public class ProbabilityDistribution implements IMeasure {
    private String name = "probability distribution";

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
        //TODO: define function for probability distribution
        return 0.0;
    }
}
