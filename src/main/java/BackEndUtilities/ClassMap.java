package BackEndUtilities;

import Interfaces.IMeasure;
import Interfaces.IMeasureString;
import Measures.*;
import Interfaces.IMeasureBigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClassMap {
    private static final Logger logger = LogManager.getLogger(ClassMap.class.getName());
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
