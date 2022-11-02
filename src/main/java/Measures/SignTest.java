package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import FrontEndUtilities.ErrorManager;
import Graphing.GraphTypes;
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
    private final boolean isGraphable = false;
    private final List<GraphTypes> validGraphs = List.of();

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

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
        if (this.inputData == null || this.inputData.getAllDataAsDouble().size() == 0) {
            ErrorManager.sendErrorMessage(name, "No Data supplied to evaluate");
            return false;
        }
        if (this.inputData.getNumberOfSamples() < this.minimumSamples) {
            ErrorManager.sendErrorMessage(name, "Invalid number of samples");
            return false;
        }
        if (this.inputData.status == IValidator.ValidationStatus.INVALID) {
            ErrorManager.sendErrorMessage(name, "Input Data not able to be validated");
            return false;
        }
        if(this.requiredVariables.size() > 0) {
            return this.requiredVariables.stream()
                    .anyMatch(Expressions::ensureArgument);
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
