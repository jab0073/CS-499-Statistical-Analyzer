package BackEndUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariuszgromada.math.mxparser.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Expressions {
    private static final Logger logger = LogManager.getLogger(Sample.class.getName());

    /**
     * It takes a string expression and a list of arguments and returns the result of the expression
     *
     * @param expression The expression to be evaluated.
     * @return The result of the expression.
     */
    public static double eval(String expression, String... arguments) {
        logger.debug("Evaluating expressions");
        Expression exp = new Expression(expression);
        for (String argues : arguments) {
            Argument arg = new Argument(argues);
            exp.addArguments(arg);
        }
        return exp.calculate();
    }

    public static BigDecimal eval(String expression, List<String> arguments) {
        logger.debug("Evaluating expressions");
        Expression exp = new Expression(expression);
        for (String argues : arguments) {
            Argument arg = new Argument(argues);
            exp.addArguments(arg.clone());
        }

        System.out.println(exp.calculate());

        return BigDecimal.valueOf(exp.calculate());
    }

    /**
     * It takes a string, parses it, evaluates the expression, and returns a double
     *
     * @param expression The expression to be evaluated.
     * @return The result of the expression.
     */
    public static double eval(String expression) {
        logger.debug("Evaluating expressions");
        Expression exp = new Expression(expression);
        return exp.calculate();
    }

    /**
     * For each data point in the dataset, create an expression, add the variables as arguments, and then calculate the
     * expression
     *
     * @param dataset The dataset to be evaluated.
     * @return A list of BigDecimal values.
     */
    public static List<BigDecimal> eval(Sample dataset) {
        logger.debug("Evaluating expressions");
        List<Expression> exps = dataset.getData().stream().map(d -> {
            Expression exp = new Expression(d);
            for(String var: dataset.getVariables()) {
                Argument arg = new Argument(var);
                exp.addArguments(arg);
            }
            return exp;
        }).toList();

        return exps.stream().map(Expression::calculate).map(BigDecimal::valueOf).collect(Collectors.toList());
    }

    /**
     * Creates a list of expressions from the input list of strings, add the variables to each expression, and then
     * calculate the value of each expression and return a list of the results
     *
     * @param expressions A list of expressions to evaluate.
     * @param variables ex. ["x=2", "y=3", "z=4"]
     * @return A list of BigDecimal values.
     */
    public static List<BigDecimal> eval(List<String> expressions, List<String> variables) {
        logger.debug("Evaluating expressions");
        List<Expression> exps = expressions.stream().map(d -> {
            Expression exp = new Expression(d);
            variables.forEach(v -> {
                Argument arg = new Argument(v);
                exp.addArguments(arg);
            });
            return exp;
        }).toList();
        return exps.stream().map(Expression::calculate).map(BigDecimal::valueOf).collect(Collectors.toList());
    }
}
