package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.MeasureConstants;
import Interfaces.BiasCorrectable;
import Interfaces.IMeasure;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class Variance extends BiasCorrectable implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.variance;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();

    public Variance() {
        this.inputData = new DataSet();
    }

    public Variance(DataSet inputData) {
        this.inputData = inputData;
    }

    public Variance(DataSet inputData, boolean isBiasCorrected) {
        this.inputData = inputData;
        this.isBiasCorrected = isBiasCorrected;
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
        logger.debug("Running " + MeasureConstants.std);

        org.apache.commons.math3.stat.descriptive.moment.StandardDeviation std = new org.apache.commons.math3.stat.descriptive.moment.StandardDeviation();
        std.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        std.setBiasCorrected(this.isBiasCorrected);
        return std.evaluate();
    }
}
