package Measures;
import BackEndUtilities.Constants;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Measure to calculate Mean
 */
@Deprecated
public class Mean implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());

    public int minimumSamples = 1;

    @Override
    public BigDecimal function(DataSet inputData) {
        String name = Constants.mean;
        logger.debug("Running " + name);
        if(inputData.getSize() >= minimumSamples) {
            List<BigDecimal> data = inputData.getAllDataAsDouble();
            if (inputData.getSize() != 0 && !data.isEmpty()) {
                return data.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal
                                .valueOf(inputData.getSize()), RoundingMode.HALF_UP);
            }
        }
        return null;
    }
}
