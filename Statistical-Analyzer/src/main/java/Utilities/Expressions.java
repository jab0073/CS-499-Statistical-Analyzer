package Utilities;

import com.google.common.base.Function;
import org.checkerframework.checker.units.qual.A;
import org.mariuszgromada.math.mxparser.*;

import java.util.ArrayList;
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

        List<Argument> args = dataset.getVariables().stream().map(Argument::new).toList();

        List<Expression> exps = dataset.getData().stream().map(d -> {
            Expression exp = new Expression(d);
            //noinspection ConstantConditions
            exp.addArguments((Argument[]) args.toArray());
            return exp;
        }).toList();

        return exps.stream().map(Expression::calculate).collect(Collectors.toList());
    }
}
