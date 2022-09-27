package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import Interfaces.IMeasure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class SpearmanRank implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final int minimumSamples = 2;
    @Override
    public BigDecimal function(DataSet inputData) {
        String name = Constants.spearman;
        logger.debug("Running " + name);

        //TODO: finish
        return null;
    }
}