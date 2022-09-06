package Utilities;

import Measures.*;
import Interfaces.IMeasure;

public class ClassMap {

    public static Class<? extends IMeasure> getMeasureClass(String measure) {
        return switch (measure) {
            case "mean" -> Mean.class;
            case "median" -> Median.class;
            case "mode" -> Mode.class;
            case "percentiles" -> Percentiles.class;
            case "probability distribution" -> ProbabilityDistribution.class;
            case "standard deviation" -> StdDiv.class;
            case "binomial distribution" -> BinomialDistribution.class;
            case "coefficient of variance" -> CoefficientOfVariance.class;
            //TODO: add rest of measures as they're defined
            default -> null;
        };
    }
}
