package BackEndUtilities;

import Interfaces.IMeasure;
import Interfaces.IMeasureString;
import Measures.*;
import Interfaces.IMeasureBigDecimal;

public class ClassMap {

    public static Class<? extends IMeasure> getMeasureClass(String measure) {
        Class<? extends IMeasure> clazz = getBigDecimalMeasureClass(measure);
        if(clazz == null) {
            return getStringMeasureClass(measure);
        }
        else {
            return clazz;
        }
    }

    private static Class<? extends IMeasureBigDecimal> getBigDecimalMeasureClass(String measure) {
        return switch (measure) {
            case "mean" -> Mean.class;
            case "median" -> Median.class;
            case "mode" -> Mode.class;
            case "percentiles" -> Percentiles.class;
            case "probability distribution" -> ProbabilityDistribution.class;
            case "variance" -> Variance.class;
            case "standard deviation" -> StdDiv.class;
            case "binomial distribution" -> BinomialDistribution.class;
            case "coefficient of variance" -> CoefficientOfVariance.class;
            //TODO: add rest of measures as they're defined
            default -> null;
        };
    }

    private static Class<? extends IMeasureString> getStringMeasureClass(String measure) {
        return switch (measure) {
            case "least square line" -> LeastSquareLine.class;
            //TODO: add rest of measures as they're defined
            default -> null;
        };
    }
}
