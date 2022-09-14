package Measures;
import BackEndUtilities.Expressions;
import Interfaces.IMeasureBigDecimal;
import BackEndUtilities.DataSet;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.BinomialDistribution;

/**
 * Measure to calculate Binomial Distribution
 */
public class BinomialDist implements IMeasureBigDecimal {
    private String name = "binomial distribution";

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public BigDecimal function(DataSet inputData) {
        int n = Integer.parseInt(inputData.getVariables().stream().filter(s -> {
            return s.startsWith("n");
        }).map(s->{
            return s.substring(2);
        }).findFirst().get());

        double p = Double.parseDouble(inputData.getVariables().stream().filter(s -> {
            return s.startsWith("p");
        }).map(s->{
            return s.substring(2);
        }).findFirst().get());

        String expression = "(n!/((n-x)!*x!))*p^x*q^(n-x)";

        BinomialDistribution bd = new BinomialDistribution(n, p);

        double probabilityMean = inputData.getDataAsDouble(true).stream().map(d -> {
            return bd.cumulativeProbability(d.intValue());
        }).toList().stream().mapToDouble(d->d).sum() / inputData.getSize();

        return BigDecimal.valueOf(probabilityMean);
    }
}
