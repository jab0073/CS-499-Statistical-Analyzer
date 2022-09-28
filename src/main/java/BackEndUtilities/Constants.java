package BackEndUtilities;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Constants {
    public static final String binomial = "binomial distribution";
    public static final String chi = "chi square";
    public static final String coefficient = "coefficient of variance";
    public static final String correlation = "correlation coefficient";
    public static final String least = "least square line";
    public static final String mean = "mean";
    public static final String median = "median";
    public static final String mode = "mode";
    public static final String percentiles = "percentiles";
    public static final String probability = "probability distribution";
    public static final String rank = "rank sum";
    public static final String sign = "sign test";
    public static final String spearman = "spearman rank";
    public static final String std = "standard deviation";
    public static final String variance = "variance";

    public static List<String> getAllConstants() {
        return Arrays.stream(Constants.class.getDeclaredFields()).map(Field::getName).map(f-> {
            try {
                return Constants.class.getDeclaredField(f).get(Expressions.class).toString();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
