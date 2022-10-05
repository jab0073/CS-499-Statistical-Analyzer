package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class Median implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.median;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();

    public Median() {
        this.inputData = new DataSet();
    }

    public Median(DataSet inputData) {
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
    public boolean validate() {
        if (this.inputData == null)
            return false;
        if (this.inputData.getNumberOfSamples() < this.minimumSamples)
            return false;
        if (this.inputData.status == IValidator.ValidationStatus.INVALID)
            return false;
        if (this.requiredVariables.stream()
                .map(Expressions::ensureArgument).anyMatch(b -> !b)) {
            return false;
        }
        return true;
    }

    @Override
    public Double run() {
        logger.debug("Running " + MeasureConstants.median);

        if(!this.validate())
            return null;

        org.apache.commons.math3.stat.descriptive.rank.Median median = new org.apache.commons.math3.stat.descriptive.rank.Median();
        median.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        return median.evaluate();
    }
}
