package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.Pair;
import Interfaces.IMeasure;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;

public class UserDefinedMeasure implements IMeasure {

    public enum aggregateMode {SUM, DIFF, MEAN}

    private String name;
    private String expression;
    private String dataVariable;
    private aggregateMode aggregate;
    private List<String> requiredVariables = new ArrayList<>();

    public UserDefinedMeasure() {
        this.name = "UNUSABLE MEASURE";
        this.expression = "";
        this.dataVariable = "data";
        this.aggregate = aggregateMode.SUM;
    }

    public UserDefinedMeasure(String name, String expression, String dataVariable) {
        this.name = name;
        this.expression = expression;
        this.dataVariable = dataVariable;
        this.aggregate = aggregateMode.SUM;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMinimumSamples() {
        return 1;
    }

    @Override
    public List<String> getRequiredVariables() {
        return requiredVariables;
    }

    public void setRequiredVariables(List<String> requiredVariables) {
        this.requiredVariables = requiredVariables;
    }

    @Override
    public void setInputData(DataSet inputData) {
        Measures.setInputData(inputData);
    }

    @Override
    public boolean validate() {
        return this.requiredVariables.stream()
                .noneMatch(Expressions::ensureArgument);
    }

    @Override
    public DataSet getInputData() {
        return Measures.getInputData();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDataVariable() {
        return dataVariable;
    }

    public void setDataVariable(String dataVariable) {
        this.dataVariable = dataVariable;
    }

    public aggregateMode getAggregate() {
        return aggregate;
    }

    public void setAggregate(aggregateMode aggregate) {
        this.aggregate = aggregate;
    }

    @Override
    public Double run() {
        logger.debug("Running " + this.name);

        if(!this.validate())
            return null;

        Expression exp = new Expression(this.expression);
        if(!Expressions.ensureArgument(this.dataVariable))
            Expressions.addArgument(this.dataVariable, "0");

        List<Pair> args = Expressions.getArguements();
        args.forEach(a -> {
            Argument arg = new Argument(a.get());
            exp.addArguments(arg);
        });
        AtomicReference<Double> result = new AtomicReference<>((double) 0);
        DataSet inputData = Measures.getInputData();
        inputData.getAllDataAsDouble().forEach(d -> {
            exp.setArgumentValue(this.dataVariable, d);
            if (this.aggregate.equals(aggregateMode.SUM) || this.aggregate.equals(aggregateMode.MEAN)) {
                result.updateAndGet(v -> (v + exp.calculate()));
            } else if (this.aggregate.equals(aggregateMode.DIFF)) {
                result.updateAndGet(v -> (v - exp.calculate()));
            }

        });
        double finalResult = result.get();
        if (this.aggregate.equals(aggregateMode.MEAN)) {
            finalResult /= inputData.getAllDataAsDouble().size();
        }
        return finalResult;
    }
}
