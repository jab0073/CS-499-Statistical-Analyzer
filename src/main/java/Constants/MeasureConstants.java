package Constants;

import BackEndUtilities.Expressions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MeasureConstants {
    public static final String binomial = "Binomial Distribution";
    public static final String chi = "Chi Square";
    public static final String coefficient = "Coefficient of Variance";
    public static final String correlation = "Correlation Coefficient";
    public static final String least = "Least Square Line";
    public static final String mean = "Mean";
    public static final String median = "Median";
    public static final String mode = "Mode";
    public static final String percentiles = "Percentiles";
    public static final String probability = "Probability distribution";
    public static final String rank = "Rank Sum";
    public static final String sign = "Sign Test";
    public static final String spearman = "Spearman Rank";
    public static final String std = "Standard Deviation";
    public static final String variance = "Variance";

    public static List<String> getAllConstants() {
        return Arrays.stream(MeasureConstants.class.getDeclaredFields()).map(Field::getName).map(f-> {
            try {
                return MeasureConstants.class.getDeclaredField(f).get(Expressions.class).toString();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
