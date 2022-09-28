package BackEndUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariuszgromada.math.mxparser.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Expressions {
    private static final Logger logger = LogManager.getLogger(Sample.class.getName());
    private static boolean evaluate = false;
    private static List<Pair> arguements = new ArrayList<>();

    public static void enableEvaluation() {
        logger.debug("Expression evaluation on");
        Expressions.evaluate = true;
    }

    public static void disableEvaluation() {
        logger.debug("Expression evaluation on");
        Expressions.evaluate = false;
    }

    public static boolean isEvaluationOn() {
        logger.debug("Expression evaluation is " + (Expressions.evaluate ? "on" : "off"));
        return Expressions.evaluate;
    }

    public static void addArgument(String variable, String value) {
        Pair toAdd = new Pair(variable);
        if(!Expressions.arguements.contains(toAdd)) {
            toAdd.setValue(value);
            Expressions.arguements.add(toAdd);
            logger.debug("Argument added.");
        }
        else {
            logger.debug("Argument variable already exists.");
        }
    }

    public static String getArgument(String variable) {
        return Expressions.arguements.stream().filter(a -> variable.equals(a.getVariable())).map(Pair::getValue).toList().get(0);
    }

    public static boolean ensureArgument(String variable) {
        return !Expressions.arguements.stream().filter(a -> variable.equals(a.getVariable())).map(Pair::getValue).toList().isEmpty();
    }

    public static List<Pair> getArguements() {
        return Expressions.arguements;
    }

    public static List<String> getStringArguements() {
        return Expressions.arguements.stream().map(Pair::get).collect(Collectors.toList());
    }

    public static void clearArguments() {
        Expressions.arguements.clear();
    }

    public static void removeArgument(String variable) {
        Expressions.arguements = Expressions.arguements.stream().filter(p -> !variable.equals(p.getVariable())).collect(Collectors.toList());
    }

    /**
     * It takes a string expression and a list of arguments and returns the result of the expression
     *
     * @param expression The expression to be evaluated.
     * @return The result of the expression.
     */
    public static double eval(String expression) {
        logger.debug("Evaluating expressions");
        Expression exp = new Expression(expression);
        if(!Expressions.arguements.isEmpty()) {
            for (Pair argues : Expressions.arguements) {
                Argument arg = new Argument(argues.get());
                exp.addArguments(arg);
            }
        }
        return exp.calculate();
    }


    /**
     * For each data point in the dataset, create an expression, add the variables as arguments, and then calculate the
     * expression
     *
     * @param dataset The dataset to be evaluated.
     * @return A list of BigDecimal values.
     */
    public static List<Double> eval(Sample dataset) {
        logger.debug("Evaluating expressions");

        List<Expression> exps = dataset.getData().stream().map(d -> {
            Expression exp = new Expression(d);
            if(!Expressions.arguements.isEmpty()) {
                for (Pair var : Expressions.arguements) {
                    Argument arg = new Argument(var.get());
                    exp.addArguments(arg);
                }
            }
            return exp;
        }).toList();

        return exps.stream().map(Expression::calculate).peek(System.out::println).collect(Collectors.toList());
    }

    /**
     * Creates a list of expressions from the input list of strings, add the variables to each expression, and then
     * calculate the value of each expression and return a list of the results
     *
     * @param expressions A list of expressions to evaluate.
     * @return A list of BigDecimal values.
     */
    public static List<Double> eval(List<String> expressions) {
        logger.debug("Evaluating expressions");
        List<Expression> exps = expressions.stream().map(d -> {
            Expression exp = new Expression(d);
            if(!Expressions.arguements.isEmpty()) {
                Expressions.arguements.forEach(v -> {
                    Argument arg = new Argument(v.get());
                    exp.addArguments(arg);
                });
            }
            return exp;
        }).toList();
        return exps.stream().map(Expression::calculate).collect(Collectors.toList());
    }
}
