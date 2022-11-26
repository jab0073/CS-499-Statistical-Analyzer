package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import BackEndUtilities.Sample;
import FrontEndUtilities.ErrorManager;
import GUI.CardTypes;
import Graphing.GraphTypes;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.util.List;

public class Percentiles implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.percentiles;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = List.of("x");
    private final boolean isGraphable = false;
    private final List<GraphTypes> validGraphs = List.of();
    private final CardTypes cardType = CardTypes.ONE_DATA_ONE_VARIABLE;

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

    public Percentiles() {
        this.inputData = new DataSet();
    }

    public Percentiles(DataSet inputData) {
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
        logger.debug("Running " + MeasureConstants.percentiles);

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

        Percentile p = new Percentile();
        p.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        double x = Double.parseDouble(Expressions.getArgument("x"));

        double result = p.evaluate(x);

        if(Double.isNaN(result))
            return null;

        return result;
    }

    @Override
    public CardTypes getCardType(){ return cardType; }
}
