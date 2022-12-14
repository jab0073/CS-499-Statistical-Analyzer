package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import Constants.MeasureConstants;
import BackEndUtilities.Sample;
import Managers.ErrorManager;
import Enums.CardTypes;
import Enums.GraphTypes;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpearmanRank implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.spearman;
    private final int minimumSamples = 2;
    private final List<String> requiredVariables = new ArrayList<>();
    private final boolean isGraphable = false;
    private final List<GraphTypes> validGraphs = List.of();
    private final CardTypes cardType = CardTypes.TWO_DATA_NO_VARIABLE;

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

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
    public Double run() {
        logger.debug("Running " + MeasureConstants.spearman);

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

        Double[] xArray = inputData.getSample(0).getDataAsDouble().toArray(Double[]::new);
        Double[] yArray = inputData.getSample(1).getDataAsDouble().toArray(Double[]::new);

        int maxLen = Math.min(xArray.length, yArray.length)-1;
        xArray = trimSamples(xArray, maxLen);
        yArray = trimSamples(yArray, maxLen);

        SpearmansCorrelation sc = new SpearmansCorrelation();

        double result = sc.correlation(ArrayUtils.toPrimitive(xArray), ArrayUtils.toPrimitive(yArray));

        if(Double.isNaN(result))
            return null;

        return result;
    }

    private Double[] trimSamples(Double[] arr, int maxLength){
        return Arrays.copyOf(arr, maxLength);
    }

    @Override
    public CardTypes getCardType(){ return cardType; }
}
