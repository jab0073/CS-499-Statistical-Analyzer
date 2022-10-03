package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;

public class UserDefinedMeasure {

    public enum aggregateMode {SUM, DIFF, MEAN}

    private String name;
    private String expression;
    private String dataVariable;
    private aggregateMode aggregate;

    public UserDefinedMeasure() {
        this.name = "";
        this.expression = "";
        this.dataVariable = "x";
        this.aggregate = aggregateMode.SUM;
    }

    public UserDefinedMeasure(String name, String expression, String dataVariable) {
        this.name = name;
        this.expression = expression;
        this.dataVariable = dataVariable;
        this.aggregate = aggregateMode.SUM;
    }

    public String getName() {
        return name;
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

    public double run() {
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

    public boolean saveToFile(String fileName) {
        Gson gson = new Gson();
        try {
            Writer writer = new FileWriter(fileName);
            gson.toJson(this, writer);
            writer.flush();
            writer.close();
            Measures.getLogger().debug(this.name + " written to " + fileName);
            return true;
        } catch (IOException e) {
            Measures.getLogger().error(this.name + " failed to write to " + fileName);
            return false;
        }
    }

    public static UserDefinedMeasure loadFromFile(String fileName) {
        Gson gson = new Gson();
        try {
            UserDefinedMeasure obj =  gson.fromJson(new FileReader(fileName), UserDefinedMeasure.class);
            Measures.getLogger().debug("Successfully loaded measure from " + fileName);
            return obj;
        } catch (FileNotFoundException e) {
            Measures.getLogger().error("!!! Failed to load measure from " + fileName);
            return null;
        }
    }
}
