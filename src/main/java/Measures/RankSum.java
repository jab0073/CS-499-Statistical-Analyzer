package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import FrontEndUtilities.ErrorManager;
import Graphing.DataFormat;
import Graphing.GraphTypes;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankSum implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.rank;
    private final int minimumSamples = 2;
    private final List<String> requiredVariables = new ArrayList<>();
    private final boolean isGraphable = false;
    private final List<GraphTypes> validGraphs = List.of();

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

    public RankSum() {
        this.inputData = new DataSet();
    }

    public RankSum(DataSet inputData) {
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
        logger.debug("Running " + MeasureConstants.rank);

        if(!this.validate())
            return null;
        double[] xList = ArrayUtils.toPrimitive(inputData.getSample(0).getDataAsDouble().toArray(Double[]::new));
        double[] yList = ArrayUtils.toPrimitive(inputData.getSample(1).getDataAsDouble().toArray(Double[]::new));

        Arrays.sort(xList);
        Arrays.sort(yList);

        // Begin Calculating Ranks for each sample
        ArrayList<Double> xRanks = new ArrayList();
        ArrayList<Double> yRanks = new ArrayList();

        int xIndex = 0;
        int yIndex = 0;

        for(int i = 1; i <= xList.length + yList.length; i++){
            if(xIndex >= xList.length){
                yRanks.add((double) i);
                yIndex++;
                continue;
            }else if(yIndex >= yList.length){
                xRanks.add((double) i);
                xIndex++;
                continue;
            }

            if(xList[xIndex] < yList[yIndex]){
                xRanks.add((double) i);
                xIndex++;
            }else if(yList[yIndex] < xList[xIndex]){
                yRanks.add((double) i);
                yIndex++;
            }else{
                xRanks.add(i+0.5);
                yRanks.add(i+0.5);
                xIndex++;
                yIndex++;
                i++;
            }

        }

        int nOne = xList.length;
        int nTwo = yList.length;

        //Calculate Rank Sums for each sample
        double tOne = sum(xRanks);
        double tTwo = sum(yRanks);

        //Calculate U values for each sample
        double uOne = calcU(nOne, nTwo, tOne);
        double uTwo = calcU(nTwo, nOne, tTwo);

        //Calculate U value for test
        double u = Math.min(uOne, uTwo);

        //Calculate expected U Value
        double uExpected = ((double) nOne * (double) nTwo)/2;

        //Calculate Standard Error
        double stdError = Math.sqrt(((double) nOne * (double) nTwo * (double) (nOne + nTwo + 1))/12);

        //Calculate Z value
        double z = (u - uExpected)/stdError;


        String result = String.format("""
                n\u2081: %d
                n\u2082: %d
                T\u2081: %,.1f
                T\u2082: %,.1f
                U\u2081: %,.1f
                U\u2082: %,.1f
                U : %,.1f
                \u03BCU: %,.1f
                \u03C3U: %,.4f
                z : %,.4f""", nOne, nTwo, tOne, tTwo, uOne, uTwo, u, uExpected, stdError, z);
        
        return result;
    }

    private double sum(ArrayList<Double> values){
        double sum = 0;
        for(double d : values){
            sum += d;
        }

        return sum;
    }

    private double calcU(int nMe, int nOther, double t){
        return (nMe * nOther) + ((double)(nMe * (nMe + 1))/2) - t;
    }

    @Override
    public DataFormat getOutputFormat(){ return DataFormat.SINGLE_STRING; }
}
