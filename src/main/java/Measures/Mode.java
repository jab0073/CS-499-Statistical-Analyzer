package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mode implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.mode;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();

    public Mode() {
        this.inputData = new DataSet();
    }

    public Mode(DataSet inputData) {
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
        return this.requiredVariables.stream()
                .anyMatch(Expressions::ensureArgument);
    }

    @Override
    public List<Double> run() {
        logger.debug("Running " + MeasureConstants.mode);

        if(!this.validate())
            return null;

        Double[] values = inputData.getAllDataAsDouble().toArray(Double[]::new);

        return Arrays.stream(StatUtils.mode(ArrayUtils.toPrimitive(values))).boxed().toList();
    }
}
