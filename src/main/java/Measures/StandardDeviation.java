package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import Constants.MeasureConstants;
import BackEndUtilities.Sample;
import Managers.ErrorManager;
import Enums.CardTypes;
import Enums.GraphTypes;
import Interfaces.BiasCorrectable;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class StandardDeviation extends BiasCorrectable implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.std;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();
    private final boolean isGraphable = false;
    private final List<GraphTypes> validGraphs = List.of();
    private final CardTypes cardType = CardTypes.ONE_DATA_NO_VARIABLE;

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

    public StandardDeviation() {
        this.inputData = new DataSet();
    }

    public StandardDeviation(DataSet inputData) {
        this.inputData = inputData;
    }

    public StandardDeviation(DataSet inputData, boolean isBiasCorrected) {
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
        logger.debug("Running " + MeasureConstants.std);

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

        org.apache.commons.math3.stat.descriptive.moment.StandardDeviation std = new org.apache.commons.math3.stat.descriptive.moment.StandardDeviation();
        std.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        std.setBiasCorrected(this.isBiasCorrected);

        double result = std.evaluate();

        if(Double.isNaN(result))
            return null;

        return result;
    }

    @Override
    public CardTypes getCardType(){ return cardType; }
}
