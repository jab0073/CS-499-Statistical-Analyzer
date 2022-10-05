package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.BiasCorrectable;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.List;

public class ProbabilityDistribution extends BiasCorrectable implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.probability;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();

    public ProbabilityDistribution() {
        this.inputData = new DataSet();
    }

    public ProbabilityDistribution(DataSet inputData) {
        this.inputData = inputData;
    }

    public ProbabilityDistribution(DataSet inputData, boolean isBiasCorrected) {
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
                .map(Expressions::ensureArgument).anyMatch(b -> !b)) {
            return false;
        }
        return true;
    }

    @Override
    public List<Double> run() {
        logger.debug("Running " + MeasureConstants.probability);

        if(!this.validate())
            return null;

        Double mean = new Mean(this.inputData).run();
        Double std = new StandardDeviation(this.inputData, this.isBiasCorrected).run();
        NormalDistribution nd = new NormalDistribution(mean, std);

        return this.inputData.getAllDataAsDouble().stream().map(nd::density).toList();
    }
}
