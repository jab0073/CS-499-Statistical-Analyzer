package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.util.List;

public class Percentiles implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.percentiles;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = List.of("x");

    public Percentiles() {
        this.inputData = new DataSet();
    }

    public Percentiles(DataSet inputData) {
        this.inputData = inputData;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getMinimumSamples() {
        return this.minimumSamples;
    }

    @Override
    public List<String> getRequiredVariables() {
        return this.requiredVariables;
    }

    @Override
    public void setInputData(DataSet inputData) {
        this.inputData = inputData;
    }

    @Override
    public DataSet getInputData() {
        return this.inputData;
    }

    @Override
    public Double run() {
        logger.debug("Running " + MeasureConstants.percentiles);

        Percentile p = new Percentile();
        p.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        double x = Double.parseDouble(Expressions.getArgument("x"));

        return p.evaluate(x);
    }
}
