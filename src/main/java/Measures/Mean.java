package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import Interfaces.IValidator;
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
        logger.debug("Running " + MeasureConstants.mean);

        if(!this.validate())
            return null;

        Double[] values = inputData.getAllDataAsDouble().toArray(Double[]::new);
        return StatUtils.mean(ArrayUtils.toPrimitive(values));
    }
}
