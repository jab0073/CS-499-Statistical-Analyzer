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
import org.apache.commons.math3.distribution.BinomialDistribution;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Binomial implements IMeasure {

    private DataSet inputData;
    private final String name = MeasureConstants.binomial;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = Arrays.asList("p", "n");
    private final boolean isGraphable = true;
    private final List<GraphTypes> validGraphs = List.of(GraphTypes.NORMAL_CURVE);
    private final CardTypes cardType = CardTypes.ONE_DATA_TWO_VARIABLE;

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

    public Binomial() {
        this.inputData = new DataSet();
    }

    public Binomial(DataSet inputData) {
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
    public List<Double> run() {
        logger.debug("Running " + MeasureConstants.binomial);

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

        int n = Integer.parseInt(Expressions.getArgument("n"));

        double p = Double.parseDouble(Expressions.getArgument("p"));

        BinomialDistribution bd = new BinomialDistribution(n, p);

        List<Double> sortedData = inputData.getAllDataAsDouble();
        Collections.sort(sortedData);

        List<Double> result = sortedData.stream().map(d -> bd.probability(d.intValue())).toList();

        if(result.stream().anyMatch(d -> Double.isNaN(d))) {
            return null;
        }

        return result;
    }

    @Override
    public DataFormat getOutputFormat(){ return DataFormat.DOUBLE_LIST; }

    @Override
    public CardTypes getCardType(){ return cardType; }
}
