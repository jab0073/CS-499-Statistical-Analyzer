package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Graphing.GraphTypes;
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
    private final boolean isGraphable = false;
    private final List<GraphTypes> validGraphs = List.of();

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

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
        if(this.requiredVariables.size() > 0) {
            return this.requiredVariables.stream()
                    .anyMatch(Expressions::ensureArgument);
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
        if(mean==null || std==null || std.equals(0.0))
            return null;

        NormalDistribution nd = new NormalDistribution(mean, std);

        List<Double> result = this.inputData.getAllDataAsDouble().stream().map(nd::density).toList();

        if(result.stream().anyMatch(d->d.isNaN()))
            return null;

        return result;
    }
}
