package BackEndUtilities;

import Interfaces.IMeasure;
import Measures.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;


/**
 * Utility Class that is used to get the Class object of the required measure.
 */
@Deprecated
public class ClassMap {
    private static final Logger logger = LogManager.getLogger(ClassMap.class.getName());

    /**
     * Takes a string as input and returns a class that implements the IMeasure interface
     *
     * @param measure the name of the measure to be returned
     * @return The class of the measure.
     */
    public static Class<? extends IMeasure<?>> getMeasureClass(String measure) {
        logger.debug("Getting class for " + measure);
        return switch (measure) {
            case Constants.mean -> Mean.class;
            case Constants.median -> Median.class;
            case Constants.mode -> Mode.class;
            case Constants.percentiles -> Percentiles.class;
            case Constants.probability -> ProbabilityDistribution.class;
            case Constants.variance -> Variance.class;
            case Constants.std -> StdDiv.class;
            case Constants.binomial -> BinomialDist.class;
            case Constants.coefficient -> CoefficientOfVariance.class;
            case Constants.least -> LeastSquareLine.class;
            case Constants.chi -> ChiSquare.class;
            case Constants.correlation -> CorrelationCoefficient.class;
            case Constants.sign -> SignTest.class;
            case Constants.spearman -> SpearmanRank.class;
            case Constants.rank -> RankSum.class;
            default -> {
                logger.error("Class not found");
                yield null;
            }
        };
    }
}
