package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.List;

public class Mean implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.mean;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();

    public Mean() {
        this.inputData = new DataSet();
    }

    public Mean(DataSet inputData) {
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
        logger.debug("Running " + MeasureConstants.mean);
        Double[] values = inputData.getAllDataAsDouble().toArray(Double[]::new);
        return StatUtils.mean(ArrayUtils.toPrimitive(values));
    }
}
