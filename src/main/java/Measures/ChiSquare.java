package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.Arrays;
import java.util.List;

public class ChiSquare implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.chi;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = Arrays.asList("p", "n");

    public ChiSquare() {
        this.inputData = new DataSet();
    }

    public ChiSquare(DataSet inputData) {
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
    public List<Double> run() {
        logger.debug("Running " + MeasureConstants.chi);

        if(!this.validate())
            return null;

        double dof = Double.parseDouble(Expressions.getArgument("d"));

        ChiSquaredDistribution csd = new ChiSquaredDistribution(dof);

        return inputData.getAllDataAsDouble().stream().map(d -> csd.density(d.intValue())).toList();
    }
}
