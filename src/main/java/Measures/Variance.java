package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import Interfaces.IMeasure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Measure to calculate Variance
 */
@Deprecated
public class Variance implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    String name = Constants.variance;
    private final int minimumSamples = 1;

    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running" + name);
        Mean mn = new Mean();
        BigDecimal mean = mn.function(inputData);
        if(mean != null) {
            return inputData.getAllDataAsDouble()
                    .stream()
                    .map(d -> BigDecimal.valueOf(Math.pow(d.subtract(mean).doubleValue(), 2)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(inputData.getSize() - 1), RoundingMode.HALF_UP);
        }
        return null;
    }
}
