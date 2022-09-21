package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
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
    private final String name = "percentiles";

    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running " + name);

        List<BigDecimal> data = inputData.getDataAsDouble(true);
        Collections.sort(data);

        int n = data.size();
        double x = Double.parseDouble(inputData.getSample(0).getVariables().stream().filter(s -> s.startsWith("x")).map(s-> s.substring(2)).findFirst().get());

        double px = (x*(n+1))/100;
        px = Math.round(px);
        return data.get((int)px-1);
    }
}
