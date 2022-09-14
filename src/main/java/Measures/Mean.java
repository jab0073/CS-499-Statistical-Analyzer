package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Measure to calculate Mean
 */
public class Mean implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());

    @Override
    public BigDecimal function(DataSet inputData) {
        String name = "mean";
        logger.debug("Running " + name);
        return inputData.getDataAsDouble(true).stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(inputData.getSize()), RoundingMode.HALF_UP);
    }
}
