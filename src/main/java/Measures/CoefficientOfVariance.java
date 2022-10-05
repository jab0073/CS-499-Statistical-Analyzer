package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.BiasCorrectable;
import Interfaces.IMeasure;
import Interfaces.IValidator;

import java.util.ArrayList;
import java.util.List;

public class CoefficientOfVariance extends BiasCorrectable implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.coefficient;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();

    public CoefficientOfVariance() {
        this.inputData = new DataSet();
    }

    public CoefficientOfVariance(DataSet inputData) {
        this.inputData = inputData;
    }

    public CoefficientOfVariance(DataSet inputData, boolean isBiasCorrected) {
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
    public boolean validate() {
        if (this.inputData == null)
            return false;
        if (this.inputData.getNumberOfSamples() < this.minimumSamples)
            return false;
        if (this.inputData.status == IValidator.ValidationStatus.INVALID)
            return false;
        if (this.requiredVariables.stream()
                .anyMatch(Expressions::ensureArgument)) {
            return false;
        }
        return true;
    }

    @Override
    public Double run() {
        logger.debug("Running " + MeasureConstants.coefficient);

        if(!this.validate())
            return null;

        Double stddiv = new StandardDeviation(this.inputData, this.isBiasCorrected).run();
        Double mean = new Mean(this.inputData).run();
        return (stddiv / mean) * (100.0);
    }
}
