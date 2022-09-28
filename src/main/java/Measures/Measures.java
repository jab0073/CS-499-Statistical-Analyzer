package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class Measures {
    private static DataSet inputData;
    private static final Logger logger = LogManager.getLogger(Measures.class.getName());

    public static void setInputData(DataSet inputData) {
        Measures.inputData = inputData;
    }

    public static DataSet getInputData() {
        return Measures.inputData;
    }

    private static List<Double> Binomial() {
        logger.debug("Running " + Constants.binomial);

        int n = Integer.parseInt(Expressions.getArgument("n"));

        double p = Double.parseDouble(Expressions.getArgument("p"));

        // (n!/((n-x)!*x!))*p^x*q^(n-x)

        BinomialDistribution bd = new BinomialDistribution(n, p);

        return inputData.getAllDataAsDouble().stream().map(d -> bd.probability(d.intValue())).toList();
    }
    
    private static Object ChiSquare() {
        logger.debug("Running " + Constants.chi);
        return null;
    }
    
    private static BigDecimal CoefficientOfVariance() {
        logger.debug("Running " + Constants.coefficient);
        BigDecimal stddiv = StandardDeviation();
        BigDecimal mn = Mean();

        return (stddiv.divide(mn, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100.0));
    }
    
    private static Object CorrelationCoefficient() {
        logger.debug("Running " + Constants.correlation);
        return null;
    }

    private static double[][] pair(double[] arr1, double[] arr2) {
        double[][] paired = new double[arr1.length][2];
        for(int i = 0 ; i < arr1.length ; i++) {
            paired[i][0] = arr1[i];
            paired[i][1] = arr2[i];
        }

        return paired;
    }
    
    private static String LeastSquareLine() {
        logger.debug("Running " + Constants.least);

        SimpleRegression sr = new SimpleRegression(true);

        List<BigDecimal> x;
        List<BigDecimal> y;
        if(inputData != null && inputData.getNumberOfSamples() >= 2) {
            try {
                x = inputData.getSample(0).getDataAsBigDecimal();
                y = inputData.getSample(1).getDataAsBigDecimal();

            }
            catch(IndexOutOfBoundsException e) {
                logger.debug("Out of Bounds Exception");
                return null;
            }
            Double[] xArray = x.stream().map(BigDecimal::doubleValue).toArray(Double[]::new);
            Double[] yArray = y.stream().map(BigDecimal::doubleValue).toArray(Double[]::new);

            double[][] xyArray = pair(ArrayUtils.toPrimitive(xArray), ArrayUtils.toPrimitive(yArray));
            logger.debug("Operating on: " + Arrays.deepToString(xyArray));
            sr.addData(xyArray);

            return "Y=" + sr.getIntercept() + "+" + sr.getSlope() + "X";
        }
        return null;
    }
    
    private static BigDecimal Mean() {
        logger.debug("Running " + Constants.mean);
        if(inputData.getSize() >= 1) {
            List<BigDecimal> data = inputData.getAllDataAsDouble();
            if (inputData.getSize() != 0 && !data.isEmpty()) {
                return data.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal
                                .valueOf(inputData.getSize()), RoundingMode.HALF_UP);
            }
        }
        return null;
    }
    
    private static BigDecimal Median() {
        logger.debug("Running " + Constants.median);
        if(inputData.getSize() > 0 && !inputData.getAllDataAsDouble().isEmpty()) {
            List<BigDecimal> inputCopy = inputData.getAllDataAsDouble().stream().sorted(Comparator.naturalOrder()).toList();

            return inputCopy.get(BigDecimal.valueOf(inputCopy.size() / 2).setScale(0, RoundingMode.HALF_UP).intValue());
        }
        return null;
    }
    
    private static BigDecimal Mode() {
        logger.debug("Running " + Constants.mode);
        HashMap<BigDecimal, Integer> map = new HashMap<>();
        BigDecimal result = BigDecimal.valueOf(-9999), max = BigDecimal.ONE;
        for (BigDecimal arrayItem : inputData.getAllDataAsDouble()) {
            if (map.putIfAbsent(arrayItem, 1) != null) {
                int count = map.get(arrayItem) + 1;
                map.put(arrayItem, count);
                if (count > max.doubleValue()) {
                    max = BigDecimal.valueOf(count);
                    result = arrayItem;
                }
            }
        }
        return !BigDecimal.valueOf(-9999).equals(result) ? result : null;
    }
    
    private static BigDecimal Percentiles() {
        logger.debug("Running " + Constants.percentiles);

        List<BigDecimal> data = inputData.getAllDataAsDouble();
        Collections.sort(data);

        int n = data.size();
        double x = Double.parseDouble(Expressions.getArgument("x"));

        double px = (x*(n+1))/100;
        px = Math.round(px);
        return data.get((int)px-1);
    }
    
    private static Object ProbabilityDistribution() {
        logger.debug("Running " + Constants.probability);
        return null;
    }
    
    private static Object RankSum() {
        logger.debug("Running " + Constants.rank);
        return null;
    }
    
    private static String SignTest() {
        logger.debug("Running " + Constants.sign);

        StringBuilder result = new StringBuilder();
        List<BigDecimal> x;
        List<BigDecimal> y;
        if (inputData != null && inputData.getNumberOfSamples() >= 2) {
            try {
                x = inputData.getSample(0).getDataAsBigDecimal();
                y = inputData.getSample(1).getDataAsBigDecimal();

            } catch (IndexOutOfBoundsException e) {
                logger.debug("Out of Bounds Exception");
                return null;
            }

            int size = Math.min(x.size(), y.size());

            for(int i = 0; i < size; i++){
                BigDecimal a = x.get(i);
                BigDecimal b = y.get(i);

                result.append(a).append(", ").append(b).append(", ");

                if(a.compareTo(b) < 0){
                    result.append("-");
                }else if(a.compareTo(b) > 0){
                    result.append("+");
                }else{
                    result.append("N/A");
                }

                result.append("\n");
            }
        }

        return result.toString();
    }
    
    private static Object SpearmanRank() {
        logger.debug("Running " + Constants.spearman);
        return null;
    }
    
    private static BigDecimal StandardDeviation() {
        logger.debug("Running " + Constants.std);
        Variance vnc = new Variance();
        BigDecimal variance = Measures.Variance();
        assert variance != null;
        return variance.sqrt(new MathContext(10, RoundingMode.HALF_UP));
    }
    
    private static BigDecimal Variance() {
        logger.debug("Running " + Constants.variance);
        
        BigDecimal mean = Measures.Mean();
        if(mean != null) {
            return inputData.getAllDataAsDouble()
                    .stream()
                    .map(d -> BigDecimal.valueOf(Math.pow(d.subtract(mean).doubleValue(), 2)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(inputData.getSize() - 1), RoundingMode.HALF_UP);
        }
        return null;
    }

    public static boolean isValidFor(String measure) {
            return switch (measure) {
                case Constants.binomial, Constants.chi, Constants.coefficient, Constants.mean, Constants.median, Constants.mode, Constants.percentiles, Constants.std, Constants.variance -> {
                    yield Measures.inputData.getSize() >= 1;
                }
                case Constants.correlation -> {
                    yield Measures.inputData.getSize() >= 0;
                }
                case Constants.least, Constants.rank, Constants.sign -> {
                    yield Measures.inputData.getSize() >= 2;
                }
                case Constants.probability -> {
                    yield Measures.inputData.getSize() >= -1;
                }
                case Constants.spearman -> {
                    yield Measures.inputData.getSize() >= -2;
                }
                default -> {
                    logger.error("Requested measure unsupported");
                    yield false;
                }
            };
    }
    
    public static Object run(String measure) {
        if(isValidFor(measure)) {
            return switch (measure) {
                case Constants.binomial -> Binomial();
                case Constants.chi -> ChiSquare();
                case Constants.coefficient -> CoefficientOfVariance();
                case Constants.correlation -> CorrelationCoefficient();
                case Constants.least -> LeastSquareLine();
                case Constants.mean -> Mean();
                case Constants.median -> Median();
                case Constants.mode -> Mode();
                case Constants.percentiles -> Percentiles();
                case Constants.probability -> ProbabilityDistribution();
                case Constants.rank -> RankSum();
                case Constants.sign -> SignTest();
                case Constants.spearman -> SpearmanRank();
                case Constants.std -> StandardDeviation();
                case Constants.variance -> Variance();
                default -> {
                    logger.error("Requested measure unsupported");
                    yield null;
                }
            };
        }
        logger.error("Invalid dataset for requested measure");
        return null;
    }

    public static Class<?> getReturnType(String measure) {
        return switch(measure) {
            case Constants.binomial -> {
                try {
                    yield Measures.class.getDeclaredMethod("Binomial").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.chi -> {
                try {
                    yield Measures.class.getDeclaredMethod("ChiSquare").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.coefficient -> {
                try {
                    yield Measures.class.getDeclaredMethod("CoefficientOfVariance").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.correlation -> {
                try {
                    yield Measures.class.getDeclaredMethod("CorrelationCoefficient").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.least -> {
                try {
                    yield Measures.class.getDeclaredMethod("LeastSquareLine").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.mean -> {
                try {
                    yield Measures.class.getDeclaredMethod("Mean").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.median -> {
                try {
                    yield Measures.class.getDeclaredMethod("Median").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.mode -> {
                try {
                    yield Measures.class.getDeclaredMethod("Mode").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.percentiles -> {
                try {
                    yield Measures.class.getDeclaredMethod("Percentiles").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.probability -> {
                try {
                    yield Measures.class.getDeclaredMethod("ProbabilityDistribution").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.rank -> {
                try {
                    yield Measures.class.getDeclaredMethod("RankSum").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.sign -> {
                try {
                    yield Measures.class.getDeclaredMethod("SignTest").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.spearman -> {
                try {
                    yield Measures.class.getDeclaredMethod("SpearmanRank").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.std -> {
                try {
                    yield Measures.class.getDeclaredMethod("StandardDeviation").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case Constants.variance -> {
                try {
                    yield Measures.class.getDeclaredMethod("Variance").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            default -> {
                logger.error("Requested measure unsupported");
                yield null;
            }
        };
    }
}
