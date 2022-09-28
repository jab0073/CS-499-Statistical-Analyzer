package Measures;
import BackEndUtilities.Constants;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Measure to calculate Coefficient of Variance
 */
@Deprecated
public class CoefficientOfVariance implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    public final int minimumSamples = 1;
    @Override
    public BigDecimal function(DataSet inputData) {
        String name = Constants.coefficient;
        logger.debug("Running " + name);
        StdDiv stddiv = new StdDiv();
        Mean mn = new Mean();

        return (stddiv.function(inputData).divide(mn.function(inputData), RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100.0));
    }
}
