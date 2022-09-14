package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

/**
 * Measure to calculate Percentiles
 */
public class Percentiles implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final String name = "percentiles";

    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running " + name);
        //TODO: define function for percentile
        return BigDecimal.ZERO;
    }
}
