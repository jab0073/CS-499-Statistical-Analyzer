package Measures;
import BackEndUtilities.Constants;
import BackEndUtilities.Expressions;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Measure to calculate Binomial Distribution
 */
public class BinomialDist implements IMeasure<BigDecimal> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final String name = Constants.binomial;
    public final int minimumSamples = 2;

    @Override
    public BigDecimal function(DataSet inputData) {
        logger.debug("Running " + name);
        int n = Integer.parseInt(Expressions.getArgument("n"));

        double p = Double.parseDouble(Expressions.getArgument("p"));

        // (n!/((n-x)!*x!))*p^x*q^(n-x)

        BinomialDistribution bd = new BinomialDistribution(n, p);

        double probabilityMean = inputData.getAllDataAsDouble().stream().map(d -> bd.cumulativeProbability(d.intValue())).toList().stream().mapToDouble(d->d).sum() / inputData.getSize();

        return BigDecimal.valueOf(probabilityMean);
    }
}
