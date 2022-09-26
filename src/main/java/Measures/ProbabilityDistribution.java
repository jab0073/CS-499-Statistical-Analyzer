package Measures;
import BackEndUtilities.Constants;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

/**
 * Measure to calculate Probability Distribution
 */
public class ProbabilityDistribution implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final String name = Constants.probability;
    public final int minimumSamples = 2;

    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running " + name);
        //TODO: define function for probability distribution
        return BigDecimal.ZERO;
    }
}
