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

import java.util.ArrayList;
import java.util.List;

public class CoefficientOfVariance extends BiasCorrectable implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.coefficient;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();
    private final boolean isGraphable = false;
    private final List<GraphTypes> validGraphs = List.of();
    private final CardTypes cardType = CardTypes.ONE_DATA_NO_VARIABLE;

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

    public CoefficientOfVariance() {
        this.inputData = new DataSet();
    }

    public CoefficientOfVariance(DataSet inputData) {
        this.inputData = inputData;
    }

    public CoefficientOfVariance(DataSet inputData, boolean isBiasCorrected) {
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
        logger.debug("Running " + MeasureConstants.coefficient);

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

        Double stddiv = new StandardDeviation(this.inputData, this.isBiasCorrected).run();

        if(stddiv.equals(0.0) || stddiv.isNaN()) {
            return 0.0;
        }

        Double mean = new Mean(this.inputData).run();

        if(mean.equals(0.0) || mean.isNaN()) {
            return null;
        }

        return (stddiv / mean) * (100.0);
    }

    @Override
    public CardTypes getCardType(){ return cardType; }
}
