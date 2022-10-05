package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.math3.distribution.BinomialDistribution;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Binomial implements IMeasure {

    private DataSet inputData;
    private final String name = MeasureConstants.binomial;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = Arrays.asList("p", "n");

    public Binomial() {
        this.inputData = new DataSet();
    }

    public Binomial(DataSet inputData) {
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
    public List<Double> run() {
        logger.debug("Running " + MeasureConstants.binomial);

        if(!this.validate())
            return null;

        int n = Integer.parseInt(Expressions.getArgument("n"));

        double p = Double.parseDouble(Expressions.getArgument("p"));

        BinomialDistribution bd = new BinomialDistribution(n, p);

        return inputData.getAllDataAsDouble().stream().map(d -> bd.probability(d.intValue())).toList();
    }
}
