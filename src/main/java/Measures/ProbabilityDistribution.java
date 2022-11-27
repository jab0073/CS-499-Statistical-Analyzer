package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import BackEndUtilities.Sample;
import FrontEndUtilities.ErrorManager;
import GUI.CardTypes;
import Graphing.DataFormat;
import Graphing.GraphTypes;
import Interfaces.BiasCorrectable;
import Interfaces.IMeasure;
import Interfaces.IValidator;

import java.util.ArrayList;
import java.util.List;

public class ProbabilityDistribution extends BiasCorrectable implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.probability;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = new ArrayList<>();
    private final boolean isGraphable = true;
    private final List<GraphTypes> validGraphs = List.of(GraphTypes.X_Y, GraphTypes.HORIZONTAL_BAR, GraphTypes.VERTICAL_BAR, GraphTypes.PIE_CHART);
    private final CardTypes cardType = CardTypes.ONE_DATA_NO_VARIABLE;

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

    public ProbabilityDistribution() {
        this.inputData = new DataSet();
    }

    public ProbabilityDistribution(DataSet inputData) {
        this.inputData = inputData;
    }

    public ProbabilityDistribution(DataSet inputData, boolean isBiasCorrected) {
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
        if (this.inputData == null || this.inputData.getAllDataAsString().size() == 0) {
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
    public List<String> run() {
        logger.debug("Running " + MeasureConstants.probability);

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

        List<String> data = this.inputData.getAllDataAsString();

        ArrayList<String> values = new ArrayList<>();
        ArrayList<Integer> occurrences = new ArrayList<>();

        for(String s : data){
            if(values.contains(s)){
                int i = values.indexOf(s);
                occurrences.set(i, occurrences.get(i) + 1);
            }else{
                values.add(s);
                occurrences.add(1);
            }
        }

        ArrayList<String> result = new ArrayList<>();

        for(int i = 0; i < values.size(); i++){
            double p = (double)occurrences.get(i) / (double)data.size();
            String v = values.get(i);

            result.add(v + ": " + p);
        }

        return result;
    }

    @Override
    public DataFormat getOutputFormat(){ return DataFormat.PROBABILITY; }

    @Override
    public CardTypes getCardType(){ return cardType; }
}
