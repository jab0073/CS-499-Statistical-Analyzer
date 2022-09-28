package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import Interfaces.IMeasure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Measure to calculate Least Square Line
 */
@Deprecated
public class LeastSquareLine implements IMeasure<String> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    public final int minimumSamples = 2;

    public double[][] pair(double[] arr1, double[] arr2) {
        double[][] paired = new double[arr1.length][2];
        for(int i = 0 ; i < arr1.length ; i++) {
            paired[i][0] = arr1[i];
            paired[i][1] = arr2[i];
        }

        return paired;
    }
    @Override
    public String function(DataSet inputData) {
        String name = Constants.least;
        logger.debug("Running " + name);

        SimpleRegression sr = new SimpleRegression(true);

        List<Double> x;
        List<Double> y;
        if(inputData != null && inputData.getNumberOfSamples() >= 2) {
            try {
                 x = inputData.getSample(0).getDataAsDouble();
                 y = inputData.getSample(1).getDataAsDouble();

            }
            catch(IndexOutOfBoundsException e) {
                logger.debug("Out of Bounds Exception");
                return null;
            }
            Double[] xArray = x.toArray(Double[]::new);
            Double[] yArray = y.toArray(Double[]::new);

            double[][] xyArray = pair(ArrayUtils.toPrimitive(xArray), ArrayUtils.toPrimitive(yArray));
            logger.debug("Operating on: " + Arrays.deepToString(xyArray));
            sr.addData(xyArray);

            return "Y=" + sr.getIntercept() + "+" + sr.getSlope() + "X";
        }
        return null;
    }
}
