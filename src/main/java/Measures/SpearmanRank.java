package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import java.util.ArrayList;
import java.util.List;

public class SpearmanRank implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.spearman;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();

    public SpearmanRank() {
        this.inputData = new DataSet();
    }

    public SpearmanRank(DataSet inputData) {
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
        if(this.requiredVariables.size() > 0) {
            return this.requiredVariables.stream()
                    .anyMatch(Expressions::ensureArgument);
        }
        if(this.inputData.getSample(1).getSize() < 2) {
            return false;
        }
        return true;
    }

    @Override
    public Double run() {
        logger.debug("Running " + MeasureConstants.spearman);

        if(!this.validate())
            return null;

        Double[] xList = inputData.getSample(0).getDataAsDouble().toArray(Double[]::new);
        Double[] yList = inputData.getSample(1).getDataAsDouble().toArray(Double[]::new);

        SpearmansCorrelation sc = new SpearmansCorrelation();

        double result = sc.correlation(ArrayUtils.toPrimitive(xList), ArrayUtils.toPrimitive(yList));

        if(Double.isNaN(result))
            return null;

        return result;
    }
}
