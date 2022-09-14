package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Measure to calculate Mode
 */
public class Mode implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final String name = "mode";


    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running " + name);
        HashMap<BigDecimal, Integer> map = new HashMap<>();
        BigDecimal result = BigDecimal.valueOf(-999), max = BigDecimal.ONE;
        for (BigDecimal arrayItem : inputData.getDataAsDouble(true)) {
            if (map.putIfAbsent(arrayItem, 1) != null) {
                int count = map.get(arrayItem) + 1;
                map.put(arrayItem, count);
                if (count > max.doubleValue()) {
                    max = BigDecimal.valueOf(count);
                    result = arrayItem;
                }
            }
        }
        return result;
    }
}
