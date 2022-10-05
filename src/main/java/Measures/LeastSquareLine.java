package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.List;

public class LeastSquareLine implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.least;
    private final int minimumSamples = 2;
    private final List<String> requiredVariables = new ArrayList<>();

    public LeastSquareLine() {
        this.inputData = new DataSet();
    }

    public LeastSquareLine(DataSet inputData) {
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

    private static double[][] pair(double[] arr1, double[] arr2) {
        double[][] paired = new double[arr1.length][2];
        for (int i = 0; i < arr1.length; i++) {
            paired[i][0] = arr1[i];
            paired[i][1] = arr2[i];
        }
        return paired;
    }
    @Override
    public String run() {
        logger.debug("Running " + MeasureConstants.least);

        if(!this.validate())
            return null;

        SimpleRegression sr = new SimpleRegression(true);

        List<Double> x = inputData.getSample(0).getDataAsDouble();
        List<Double> y = inputData.getSample(1).getDataAsDouble();

        Double[] xArray = x.toArray(Double[]::new);
        Double[] yArray = y.toArray(Double[]::new);

        double[][] xyArray = pair(ArrayUtils.toPrimitive(xArray), ArrayUtils.toPrimitive(yArray));
        sr.addData(xyArray);

        return "Y=" + sr.getIntercept() + "+" + sr.getSlope() + "X";
    }
}
