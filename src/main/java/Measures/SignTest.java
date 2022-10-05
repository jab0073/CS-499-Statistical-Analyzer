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

public class SignTest implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.sign;
    private final int minimumSamples = 2;
    private final List<String> requiredVariables = new ArrayList<>();

    public SignTest() {
        this.inputData = new DataSet();
    }

    public SignTest(DataSet inputData) {
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
    public String run() {
        logger.debug("Running " + MeasureConstants.sign);

        if(!this.validate())
            return null;

        StringBuilder result = new StringBuilder();
        List<Double> x;
        List<Double> y;
        if (inputData != null && inputData.getNumberOfSamples() >= 2) {
            try {
                x = inputData.getSample(0).getDataAsDouble();
                y = inputData.getSample(1).getDataAsDouble();

            } catch (IndexOutOfBoundsException e) {
                logger.debug("Out of Bounds Exception");
                return null;
            }

            int size = Math.min(x.size(), y.size());

            for (int i = 0; i < size; i++) {
                Double a = x.get(i);
                Double b = y.get(i);

                result.append(a).append(", ").append(b).append(", ");

                if (a.compareTo(b) < 0) {
                    result.append("-");
                } else if (a.compareTo(b) > 0) {
                    result.append("+");
                } else {
                    result.append("N/A");
                }

                result.append("\n");
            }
        }

        return result.toString();
    }
}
