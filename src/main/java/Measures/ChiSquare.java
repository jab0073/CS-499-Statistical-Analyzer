package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.MeasureConstants;
import FrontEndUtilities.ErrorManager;
import GUI.CardTypes;
import Graphing.GraphTypes;
import Interfaces.IMeasure;
import Interfaces.IValidator;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.Arrays;
import java.util.List;

public class ChiSquare implements IMeasure {
    private DataSet inputData;
    private final String name = MeasureConstants.chi;
    private final int minimumSamples = 1;
    private final List<String> requiredVariables = Arrays.asList("p", "n");
    private final boolean isGraphable = false;
    private final List<GraphTypes> validGraphs = List.of();
    private final CardTypes cardType = CardTypes.NO_DATA_TWO_VARIABLE;

    public boolean isGraphable(){ return this.isGraphable; }

    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }

    public ChiSquare() {
        this.inputData = new DataSet();
    }

    public ChiSquare(DataSet inputData) {
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
        logger.debug("Running " + MeasureConstants.chi);

        if(!this.validate())
            return null;

        double dof = Double.parseDouble(Expressions.getArgument("d"));

        ChiSquaredDistribution csd = new ChiSquaredDistribution(dof);

        List<Double> result = inputData.getAllDataAsDouble().stream().map(d -> csd.density(d.intValue())).toList();

        if(result.stream().anyMatch(d -> Double.isNaN(d))) {
            return null;
        }

        return result;
    }

    @Override
    public CardTypes getCardType(){ return cardType; }
}
