package Measures;
import BackEndUtilities.Constants;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * Measure to calculate Median
 */
public class Median implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final int minimumSamples = 1;
    public final String name = Constants.median;
    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running " + name);
        if(inputData.getSize() > 0 && !inputData.getAllDataAsDouble().isEmpty()) {
            List<BigDecimal> inputCopy = inputData.getAllDataAsDouble().stream().sorted(Comparator.naturalOrder()).toList();

            return inputCopy.get(BigDecimal.valueOf(inputCopy.size() / 2).setScale(0, RoundingMode.HALF_UP).intValue());
        }
        return null;
    }
}
