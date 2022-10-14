package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.util.ArrayList;
import java.util.List;

public class CorrelationCoefficient implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.correlation;
    private final int minimumSamples = 2;
    private final List<String> requiredVariables = new ArrayList<>();

    public CorrelationCoefficient() {
        this.inputData = new DataSet();
    }

    public CorrelationCoefficient(DataSet inputData) {
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
    public Double run() {
        logger.debug("Running " + MeasureConstants.correlation);

        if(!this.validate())
            return null;

        PearsonsCorrelation pc = new PearsonsCorrelation();

        Double[] xList = inputData.getSample(0).getDataAsDouble().toArray(Double[]::new);
        Double[] yList = inputData.getSample(1).getDataAsDouble().toArray(Double[]::new);

        return pc.correlation(ArrayUtils.toPrimitive(xList), ArrayUtils.toPrimitive(yList));
    }
}
