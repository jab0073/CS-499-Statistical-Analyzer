package Measures;

import BackEndUtilities.Constants;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Measure to calculate Standard Deviation
 */
public class StdDiv implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final String name = Constants.std;

    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running " + name);
        Variance vnc = new Variance();
        return vnc.function(inputData).sqrt(new MathContext(10, RoundingMode.HALF_UP));
    }
}
