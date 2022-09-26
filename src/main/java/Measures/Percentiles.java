package Measures;
import BackEndUtilities.Expressions;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import BackEndUtilities.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Measure to calculate Percentiles
 * Formula: Px = [x(n+1)]/100
 */
public class Percentiles implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final String name = Constants.percentiles;
    public final int minimumSamples = 1;
    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running " + name);

        List<BigDecimal> data = inputData.getAllDataAsDouble();
        Collections.sort(data);

        int n = data.size();
        double x = Double.parseDouble(Expressions.getArgument("x"));

        double px = (x*(n+1))/100;
        px = Math.round(px);
        return data.get((int)px-1);
    }
}
