package Measures;
import BackEndUtilities.Constants;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Measure to calculate Mode
 */
@Deprecated
public class Mode implements IMeasure<Double> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final String name = Constants.mode;
    public final int minimumSamples = 1;

    @Override
    public Double function(DataSet inputData) {
        logger.debug("Running " + name);
        HashMap<Double, Integer> map = new HashMap<>();
        double result = -9999.0, max = 1.0;
        for (Double arrayItem : inputData.getAllDataAsDouble()) {
            if (map.putIfAbsent(arrayItem, 1) != null) {
                int count = map.get(arrayItem) + 1;
                map.put(arrayItem, count);
                if (count > max) {
                    max = count;
                    result = arrayItem;
                }
            }
        }
        return !Double.valueOf(-9999).equals(result) ? result : null;
    }
}
