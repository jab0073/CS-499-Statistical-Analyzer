package BackEndUtilities;

import Interfaces.IMeasure;
import Measures.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Utility Class that is used to get the Class object of the required measure.
 */
public class ClassMap {
    private static final Logger logger = LogManager.getLogger(ClassMap.class.getName());

    /**
     * It takes a string as input and returns a class that implements the IMeasure interface
     *
     * @param measure the name of the measure to be returned
     * @return The class of the measure.
     */
    public static Class<? extends IMeasure<?>> getMeasureClass(String measure) {
        logger.debug("Getting class for " + measure);
        return switch (measure) {
            case "mean" -> Mean.class;
            case "median" -> Median.class;
            case "mode" -> Mode.class;
            case "percentiles" -> Percentiles.class;
            case "probability distribution" -> ProbabilityDistribution.class;
            case "variance" -> Variance.class;
            case "standard deviation" -> StdDiv.class;
            case "binomial distribution" -> BinomialDist.class;
            case "coefficient of variance" -> CoefficientOfVariance.class;
            case "least square line" -> LeastSquareLine.class;
            //TODO: add rest of measures as they're defined
            default -> {
                logger.error("Class not found");
                yield null;
            }
        };
    }
}
