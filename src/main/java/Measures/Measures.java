package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
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

        BinomialDistribution bd = new BinomialDistribution(n, p);

        return inputData.getAllDataAsDouble().stream().map(d -> bd.probability(d.intValue())).toList();
    }

    private static List<Double> ChiSquare() {
        logger.debug("Running " + Constants.chi);

        double dof = Double.parseDouble(Expressions.getArgument("d"));

        ChiSquaredDistribution csd = new ChiSquaredDistribution(dof);

        return inputData.getAllDataAsDouble().stream().map(d -> csd.density(d.intValue())).toList();
    }

    private static Double CoefficientOfVariance() {
        logger.debug("Running " + Constants.coefficient);
        Double stddiv = StandardDeviation();
        Double mean = Mean();
        return (stddiv / mean) * (100.0);
    }

    private static Object CorrelationCoefficient() {
        logger.debug("Running " + Constants.correlation);
        PearsonsCorrelation pc = new PearsonsCorrelation();

        Double[] xList = inputData.getSample(0).getDataAsDouble().toArray(Double[]::new);
        Double[] yList = inputData.getSample(1).getDataAsDouble().toArray(Double[]::new);

        return pc.correlation(ArrayUtils.toPrimitive(xList), ArrayUtils.toPrimitive(yList));
    }

    private static double[][] pair(double[] arr1, double[] arr2) {
        double[][] paired = new double[arr1.length][2];
        for (int i = 0; i < arr1.length; i++) {
            paired[i][0] = arr1[i];
            paired[i][1] = arr2[i];
        }
        return paired;
    }

    private static String LeastSquareLine() {
        logger.debug("Running " + Constants.least);

        SimpleRegression sr = new SimpleRegression(true);

        List<Double> x = inputData.getSample(0).getDataAsDouble();
        List<Double> y = inputData.getSample(1).getDataAsDouble();

        Double[] xArray = x.toArray(Double[]::new);
        Double[] yArray = y.toArray(Double[]::new);

        double[][] xyArray = pair(ArrayUtils.toPrimitive(xArray), ArrayUtils.toPrimitive(yArray));
        //logger.debug("Operating on: " + Arrays.deepToString(xyArray));
        sr.addData(xyArray);

        return "Y=" + sr.getIntercept() + "+" + sr.getSlope() + "X";
    }

    private static Double Mean() {
        logger.debug("Running " + Constants.mean);
        List<Double> data = inputData.getAllDataAsDouble();
        return data.stream()
                .mapToDouble(d -> d)
                .sum()
                / inputData.getSize();
    }

    private static Double Median() {
        logger.debug("Running " + Constants.median);
        if (inputData.getSize() > 0 && !inputData.getAllDataAsDouble().isEmpty()) {
            List<Double> inputCopy = inputData.getAllDataAsDouble().stream().sorted(Comparator.naturalOrder()).toList();

            return inputCopy.get(BigDecimal.valueOf(inputCopy.size() / 2).setScale(0, RoundingMode.HALF_UP).intValue());
        }
        return null;
    }

    private static List<Double> Mode() {
        logger.debug("Running " + Constants.mode);
        Double[] values = inputData.getAllDataAsDouble().toArray(Double[]::new);

        return Arrays.stream(StatUtils.mode(ArrayUtils.toPrimitive(values))).boxed().toList();

        /*HashMap<Double, Integer> map = new HashMap<>();
        double result = -9999.0, max = 1.0;
        for (Double arrayItem : inputData.getAllDataAsDouble()) {
            if (map.putIfAbsent(arrayItem, 1) != null) {
                int count = map.get(arrayItem) + 1;
                map.put(arrayItem, count);
                if (count > max) {
                    max = (double) count;
                    result = arrayItem;
                }
            }
        }
        return !Double.valueOf(-9999).equals(result) ? result : null;*/
    }

    private static Double Percentiles() {
        logger.debug("Running " + Constants.percentiles);

        Percentile p = new Percentile();
        p.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));



        /*List<Double> data = inputData.getAllDataAsDouble();
        Collections.sort(data);

        int n = data.size();*/
        double x = Double.parseDouble(Expressions.getArgument("x"));

        /*double px = (x*(n+1))/100;
        px = Math.round(px);

        return data.get((int)px-1);*/

        return p.evaluate(x);
    }

    private static Double ProbabilityDistribution() {
        logger.debug("Running " + Constants.probability);
        Double mean = Mean();
        Double std = StandardDeviation();
        NormalDistribution nd = new NormalDistribution(mean, std);

        double x = Double.parseDouble(Expressions.getArgument("x"));

        return nd.density(x);
    }

    private static Double RankSum() {
        logger.debug("Running " + Constants.rank);

        MannWhitneyUTest mwut = new MannWhitneyUTest();
        Double[] xList = inputData.getSample(0).getDataAsDouble().toArray(Double[]::new);
        Double[] yList = inputData.getSample(1).getDataAsDouble().toArray(Double[]::new);

        return mwut.mannWhitneyUTest(ArrayUtils.toPrimitive(xList), ArrayUtils.toPrimitive(yList));
    }

    private static String SignTest() {
        logger.debug("Running " + Constants.sign);

        StringBuilder result = new StringBuilder();
        List<Double> x;
        List<Double> y;
        if (inputData != null && inputData.getNumberOfSamples() >= 2) {
            try {
                x = inputData.getSample(0).getDataAsDouble();
                y = inputData.getSample(1).getDataAsDouble();

            } catch (IndexOutOfBoundsException e) {
                logger.debug("Out of Bounds Exception");
                return null;
            }

            int size = Math.min(x.size(), y.size());

            for (int i = 0; i < size; i++) {
                Double a = x.get(i);
                Double b = y.get(i);

                result.append(a).append(", ").append(b).append(", ");

                if (a.compareTo(b) < 0) {
                    result.append("-");
                } else if (a.compareTo(b) > 0) {
                    result.append("+");
                } else {
                    result.append("N/A");
                }

                result.append("\n");
            }
        }

        return result.toString();
    }

    private static Double SpearmanRank() {
        logger.debug("Running " + Constants.spearman);

        Double[] xList = inputData.getSample(0).getDataAsDouble().toArray(Double[]::new);
        Double[] yList = inputData.getSample(1).getDataAsDouble().toArray(Double[]::new);

        SpearmansCorrelation sc = new SpearmansCorrelation();
        return sc.correlation(ArrayUtils.toPrimitive(xList), ArrayUtils.toPrimitive(yList));
    }

    private static Double StandardDeviation() {
        logger.debug("Running " + Constants.std);

        //Double variance = Measures.Variance();

        StandardDeviation std = new StandardDeviation();
        std.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        return std.evaluate();

        /*assert variance != null;
        return Math.sqrt(variance);*/
    }

    private static Double Variance() {
        logger.debug("Running " + Constants.variance);

        Variance v = new Variance();

        v.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));

        return v.evaluate();
        
        /*Double mean = Measures.Mean();
        if(mean != null) {
            return inputData.getAllDataAsDouble()
                    .stream()
                    .map(d -> BigDecimal.valueOf(Math.pow(d - mean, 2)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(inputData.getSize() - 1), RoundingMode.HALF_UP).doubleValue();
        }
        return null;*/
    }

    public static boolean isValidFor(String measure) {
        if (inputData != null) {
            logger.debug("DataSet is not null and has " + inputData.getNumberOfSamples() + " data points");
            return switch (measure) {
                case Constants.binomial -> {
                    yield Measures.inputData.getNumberOfSamples() >= 1 && Expressions.ensureArgument("n") && Expressions.ensureArgument("p");
                }
                case Constants.variance, Constants.std, Constants.mode, Constants.median, Constants.mean, Constants.coefficient -> {
                    yield Measures.inputData.getNumberOfSamples() >= 1;
                }
                case Constants.percentiles -> {
                    yield Measures.inputData.getNumberOfSamples() >= 1 && Expressions.ensureArgument("x");
                }
                case Constants.chi -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2 && Expressions.ensureArgument("d");
                }
                case Constants.correlation -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                case Constants.least -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                case Constants.rank -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                case Constants.sign -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                case Constants.probability -> {
                    yield Measures.inputData.getNumberOfSamples() >= 1 && Expressions.ensureArgument("x");
                }
                case Constants.spearman -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                default -> {
                    logger.error("Invalid measure passed to isValidFor: " + measure);
                    yield false;
                }
            };
        }
        logger.error("DataSet not loaded");
        return false;
    }

    public static Object run(String measure) {
        if (isValidFor(measure)) {
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
                    logger.error("Invalid measure passed to run:" + measure);
                    yield null;
                }
            };
        }
        logger.error("Error running measure: " + measure);
        return null;
    }

    public static Class<?> getReturnType(String measure) {
        return switch (measure) {
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
