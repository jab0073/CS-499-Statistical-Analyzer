package Measures;

import BackEndUtilities.MeasureConstants;
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
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Measures {
    private static DataSet inputData;
    private static final Logger logger = LogManager.getLogger(Measures.class.getName());

    private static boolean isBiasCorrected = false;

    public static void setBiasCorrected(boolean b) {
        isBiasCorrected = b;
    }

    public static void setInputData(DataSet inputData) {
        Measures.inputData = inputData;
    }

    public static DataSet getInputData() {
        return Measures.inputData;
    }

    private static List<Double> Binomial() {
        logger.debug("Running " + MeasureConstants.binomial);

        int n = Integer.parseInt(Expressions.getArgument("n"));

        double p = Double.parseDouble(Expressions.getArgument("p"));

        BinomialDistribution bd = new BinomialDistribution(n, p);

        return inputData.getAllDataAsDouble().stream().map(d -> bd.probability(d.intValue())).toList();
    }

    private static List<Double> ChiSquare() {
        logger.debug("Running " + MeasureConstants.chi);

        double dof = Double.parseDouble(Expressions.getArgument("d"));

        ChiSquaredDistribution csd = new ChiSquaredDistribution(dof);

        return inputData.getAllDataAsDouble().stream().map(d -> csd.density(d.intValue())).toList();
    }

    private static Double CoefficientOfVariance() {
        logger.debug("Running " + MeasureConstants.coefficient);
        Double stddiv = StandardDeviation();
        Double mean = Mean();
        return (stddiv / mean) * (100.0);
    }

    private static Object CorrelationCoefficient() {
        logger.debug("Running " + MeasureConstants.correlation);
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
        logger.debug("Running " + MeasureConstants.least);

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
        logger.debug("Running " + MeasureConstants.mean);
        Double[] values = inputData.getAllDataAsDouble().toArray(Double[]::new);
        return StatUtils.mean(ArrayUtils.toPrimitive(values));
    }

    private static Double Median() {
        logger.debug("Running " + MeasureConstants.median);
        Median median = new Median();
        median.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        return median.evaluate();
    }

    private static List<Double> Mode() {
        logger.debug("Running " + MeasureConstants.mode);
        Double[] values = inputData.getAllDataAsDouble().toArray(Double[]::new);

        return Arrays.stream(StatUtils.mode(ArrayUtils.toPrimitive(values))).boxed().toList();
    }

    private static Double Percentiles() {
        logger.debug("Running " + MeasureConstants.percentiles);

        Percentile p = new Percentile();
        p.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        double x = Double.parseDouble(Expressions.getArgument("x"));

        return p.evaluate(x);
    }

    private static Double ProbabilityDistribution() {
        logger.debug("Running " + MeasureConstants.probability);
        Double mean = Mean();
        Double std = StandardDeviation();
        NormalDistribution nd = new NormalDistribution(mean, std);

        double x = Double.parseDouble(Expressions.getArgument("x"));

        return nd.density(x);
    }

    private static Double RankSum() {
        logger.debug("Running " + MeasureConstants.rank);

        MannWhitneyUTest mwut = new MannWhitneyUTest();
        Double[] xList = inputData.getSample(0).getDataAsDouble().toArray(Double[]::new);
        Double[] yList = inputData.getSample(1).getDataAsDouble().toArray(Double[]::new);

        return mwut.mannWhitneyUTest(ArrayUtils.toPrimitive(xList), ArrayUtils.toPrimitive(yList));
    }

    private static String SignTest() {
        logger.debug("Running " + MeasureConstants.sign);

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
        logger.debug("Running " + MeasureConstants.spearman);

        Double[] xList = inputData.getSample(0).getDataAsDouble().toArray(Double[]::new);
        Double[] yList = inputData.getSample(1).getDataAsDouble().toArray(Double[]::new);

        SpearmansCorrelation sc = new SpearmansCorrelation();
        return sc.correlation(ArrayUtils.toPrimitive(xList), ArrayUtils.toPrimitive(yList));
    }

    private static Double StandardDeviation() {
        logger.debug("Running " + MeasureConstants.std);

        StandardDeviation std = new StandardDeviation();
        std.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));
        std.setBiasCorrected(isBiasCorrected);
        return std.evaluate();
    }

    private static Double Variance() {
        logger.debug("Running " + MeasureConstants.variance);

        Variance v = new Variance();

        v.setBiasCorrected(isBiasCorrected);

        v.setData(ArrayUtils.toPrimitive(inputData.getAllDataAsDouble().toArray(Double[]::new)));

        return v.evaluate();

    }

    public static boolean isValidFor(String measure) {
        if (inputData != null) {
            logger.debug("DataSet is not null and has " + inputData.getNumberOfSamples() + " data points");
            return switch (measure) {
                case MeasureConstants.binomial -> {
                    yield Measures.inputData.getNumberOfSamples() >= 1 && Expressions.ensureArgument("n") && Expressions.ensureArgument("p");
                }
                case MeasureConstants.variance, MeasureConstants.std, MeasureConstants.mode, MeasureConstants.median, MeasureConstants.mean, MeasureConstants.coefficient -> {
                    yield Measures.inputData.getNumberOfSamples() >= 1;
                }
                case MeasureConstants.percentiles -> {
                    yield Measures.inputData.getNumberOfSamples() >= 1 && Expressions.ensureArgument("x");
                }
                case MeasureConstants.chi -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2 && Expressions.ensureArgument("d");
                }
                case MeasureConstants.correlation -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                case MeasureConstants.least -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                case MeasureConstants.rank -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                case MeasureConstants.sign -> {
                    yield Measures.inputData.getNumberOfSamples() >= 2;
                }
                case MeasureConstants.probability -> {
                    yield Measures.inputData.getNumberOfSamples() >= 1 && Expressions.ensureArgument("x");
                }
                case MeasureConstants.spearman -> {
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
                case MeasureConstants.binomial -> Binomial();
                case MeasureConstants.chi -> ChiSquare();
                case MeasureConstants.coefficient -> CoefficientOfVariance();
                case MeasureConstants.correlation -> CorrelationCoefficient();
                case MeasureConstants.least -> LeastSquareLine();
                case MeasureConstants.mean -> Mean();
                case MeasureConstants.median -> Median();
                case MeasureConstants.mode -> Mode();
                case MeasureConstants.percentiles -> Percentiles();
                case MeasureConstants.probability -> ProbabilityDistribution();
                case MeasureConstants.rank -> RankSum();
                case MeasureConstants.sign -> SignTest();
                case MeasureConstants.spearman -> SpearmanRank();
                case MeasureConstants.std -> StandardDeviation();
                case MeasureConstants.variance -> Variance();
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
            case MeasureConstants.binomial -> {
                try {
                    yield Measures.class.getDeclaredMethod("Binomial").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.chi -> {
                try {
                    yield Measures.class.getDeclaredMethod("ChiSquare").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.coefficient -> {
                try {
                    yield Measures.class.getDeclaredMethod("CoefficientOfVariance").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.correlation -> {
                try {
                    yield Measures.class.getDeclaredMethod("CorrelationCoefficient").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.least -> {
                try {
                    yield Measures.class.getDeclaredMethod("LeastSquareLine").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.mean -> {
                try {
                    yield Measures.class.getDeclaredMethod("Mean").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.median -> {
                try {
                    yield Measures.class.getDeclaredMethod("Median").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.mode -> {
                try {
                    yield Measures.class.getDeclaredMethod("Mode").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.percentiles -> {
                try {
                    yield Measures.class.getDeclaredMethod("Percentiles").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.probability -> {
                try {
                    yield Measures.class.getDeclaredMethod("ProbabilityDistribution").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.rank -> {
                try {
                    yield Measures.class.getDeclaredMethod("RankSum").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.sign -> {
                try {
                    yield Measures.class.getDeclaredMethod("SignTest").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.spearman -> {
                try {
                    yield Measures.class.getDeclaredMethod("SpearmanRank").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.std -> {
                try {
                    yield Measures.class.getDeclaredMethod("StandardDeviation").getReturnType();
                } catch (NoSuchMethodException e) {
                    yield null;
                }
            }
            case MeasureConstants.variance -> {
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

    public static Logger getLogger() {
        return logger;
    }
}
