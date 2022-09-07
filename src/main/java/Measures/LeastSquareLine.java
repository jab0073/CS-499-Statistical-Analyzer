package Measures;

import BackEndUtilities.DataSet;
import Interfaces.IMeasure;

import java.util.List;

public class LeastSquareLine implements IMeasure {
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
    public double function(DataSet inputData) {
        List<Double> x = inputData.getDataAsDouble(true);
        List<Double> y = inputData.getAdditionalDataAsDouble(true);
        double xSum = x.stream().mapToDouble(d->d).sum();
        double ySum = y.stream().mapToDouble(d->d).sum();
        int n = inputData.getData().size();
        int sxsy = 0;
        // sum of square of x
        int sx2 = 0;
        for (int i = 0; i < n; i++) {
            sxsy += x.get(i) * y.get(i);
            sx2 += x.get(i) * x.get(i);
        }
        double b = (n * sxsy - xSum * ySum) / (n * sx2 - xSum * ySum);

        double xMean = xSum / n;
        double yMean = ySum / n;

        double a = yMean - b * xMean;

        return 0.0;
    }
}
