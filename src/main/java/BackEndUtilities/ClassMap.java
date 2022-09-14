package BackEndUtilities;

import Interfaces.IMeasure;
import Interfaces.IMeasureString;
import Measures.*;
import Interfaces.IMeasureBigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility Class that is used to get the Class object of the required measure.
 */
public class ClassMap {
    private static final Logger logger = LogManager.getLogger(ClassMap.class.getName());
    /**
     * If the measure is to return a BigDecimal, return the class for that measure. If it's not to return a BigDecimal,
     * return the class for the String returning measure. If it's not to return a String, return null
     *
     * @param measure The name of the measure you want to retrieve the class for.
     * @return The class of the measure.
     */
    public static Class<? extends IMeasure> getMeasureClass(String measure) {
        logger.debug("Retrieving class for: " + measure);
        Class<? extends IMeasure> clazz = getBigDecimalMeasureClass(measure);
        if(clazz == null) {
            Class<? extends IMeasure> strClazz = getStringMeasureClass(measure);
            if(strClazz != null) {
                logger.debug("Class retrieved: " + strClazz.getName());
                return strClazz;
            }
            logger.error("Measure class was not able to be retrieved.");
            return null;
        }
        else {
            logger.debug("Class retrieved: " + clazz.getName());
            return clazz;
        }
    }

    /**
     * It takes a string as input and returns a class that implements the IMeasureBigDecimal interface
     *
     * @param measure the name of the measure to be returned
     * @return The class of the measure.
     */
    private static Class<? extends IMeasureBigDecimal> getBigDecimalMeasureClass(String measure) {

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
            //TODO: add rest of measures as they're defined
            default -> null;
        };
    }

    /**
     * It returns a class that implements the IMeasureString interface, given a string that represents the name of the
     * measure
     *
     * @param measure the name of the measure to be used.
     * @return A class object that implements IMeasureString.
     */
    private static Class<? extends IMeasureString> getStringMeasureClass(String measure) {
        return switch (measure) {
            case "least square line" -> LeastSquareLine.class;
            //TODO: add rest of measures as they're defined
            default -> null;
        };
    }
}
