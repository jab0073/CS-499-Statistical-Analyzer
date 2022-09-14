package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.Sample;
import Interfaces.IMeasureString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Measure to calculate Least Square Line
 */
public class LeastSquareLine implements IMeasureString {
    private String name = "least square line";

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String function(DataSet inputData) {
        List<BigDecimal> x = inputData.getSample(0).getDataAsDouble(true);
        List<BigDecimal> y = inputData.getSample(1).getDataAsDouble(true);
        BigDecimal xSum = x.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ySum = y.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        int n = inputData.getSize();
        BigDecimal sxsy = BigDecimal.ZERO;
        // sum of square of x
        BigDecimal sx2 = BigDecimal.ZERO;
        for (int i = 0; i < n ; i++) {
            sxsy = sxsy.add(x.get(i).multiply(y.get(i)));
            sx2 = sx2.add(x.get(i).multiply(x.get(i)));
        }
        BigDecimal b = (sxsy.multiply(BigDecimal.valueOf(n)).subtract(xSum.multiply(ySum))).divide(sx2.multiply(BigDecimal.valueOf(n)).subtract(xSum.multiply(ySum)), RoundingMode.HALF_UP);

        BigDecimal xMean = xSum.divide(BigDecimal.valueOf(n), RoundingMode.HALF_UP);
        BigDecimal yMean = ySum.divide(BigDecimal.valueOf(n), RoundingMode.HALF_UP);

        BigDecimal a = yMean.subtract(b.multiply(xMean));

        return "Y=" + a.doubleValue() + "+" + b.doubleValue() + "X";
    }
}
