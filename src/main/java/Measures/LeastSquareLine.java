package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import BackEndUtilities.Sample;
import FrontEndUtilities.ErrorManager;
import GUI.CardTypes;
import Graphing.DataFormat;
import Graphing.GraphTypes;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeastSquareLine implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.least;
    private final int minimumSamples = 2;
    private final List<String> requiredVariables = new ArrayList<>();
    private final boolean isGraphable = true;
    private final List<GraphTypes> validGraphs = List.of(GraphTypes.NONE,GraphTypes.X_Y);
    private final CardTypes cardType = CardTypes.TWO_DATA_NO_VARIABLE;

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

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

        if(Expressions.isEvaluationOn()){
            DataSet newDS = new DataSet();
            for(Sample s : inputData.getSamples()){
                List<Double> eval = Expressions.eval(s);
                Sample newS = new Sample(eval);

                newDS.addSample(newS);
            }

            inputData = newDS;
        }

        if(!this.validate())
            return null;
        SimpleRegression sr = new SimpleRegression(true);

        List<Double> x = inputData.getSample(0).getDataAsDouble();
        List<Double> y = inputData.getSample(1).getDataAsDouble();

        Double[] xArray = x.toArray(Double[]::new);
        Double[] yArray = y.toArray(Double[]::new);

        int maxLen = Math.min(xArray.length, yArray.length)-1;
        xArray = trimSamples(xArray, maxLen);
        yArray = trimSamples(yArray, maxLen);

        double[][] xyArray = pair(ArrayUtils.toPrimitive(xArray), ArrayUtils.toPrimitive(yArray));
        sr.addData(xyArray);

        double b = sr.getIntercept();
        double m = sr.getSlope();

        if(Double.isNaN(b) || Double.isNaN(m))
            return null;

        return "b=" + b + ",m=" + m;
    }

    private Double[] trimSamples(Double[] arr, int maxLength){
        return Arrays.copyOf(arr, maxLength);
    }

    @Override
    public DataFormat getOutputFormat(){return DataFormat.MX_PLUS_B; }

    @Override
    public CardTypes getCardType(){ return cardType; }
}
