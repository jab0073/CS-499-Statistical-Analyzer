package BackEndUtilities;

import org.mariuszgromada.math.mxparser.*;
import java.util.List;
import java.util.stream.Collectors;

public class Expressions {

    public static double eval(String expression, String... arguments) {
        Expression exp = new Expression(expression);
        for (String argues : arguments) {
            Argument arg = new Argument(argues);
            exp.addArguments(arg);
        }
        return exp.calculate();
    }

    public static double eval(String expression) {
        Expression exp = new Expression(expression);
        return exp.calculate();
    }

    public static List<Double> eval(DataSet dataset) {
        List<Expression> exps = dataset.getData().stream().map(d -> {
            Expression exp = new Expression(d);
            for(String var: dataset.getVariables()) {
                Argument arg = new Argument(var);
                exp.addArguments(arg);
            }
            return exp;
        }).toList();

        return exps.stream().map(Expression::calculate).collect(Collectors.toList());
    }

    public static List<Double> eval(List<String> expressions, List<String> variables) {
        List<Expression> exps = expressions.stream().map(d -> {
            Expression exp = new Expression(d);
            variables.forEach(v -> {
                Argument arg = new Argument(v);
                exp.addArguments(arg);
            });
            return exp;
        }).toList();
        return exps.stream().map(Expression::calculate).collect(Collectors.toList());
    }
}
