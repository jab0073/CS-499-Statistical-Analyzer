package Measures;

import BackEndUtilities.*;
import Interfaces.IMeasure;
import Respository.RepositoryManager;
import Settings.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasuresTest {

    private static DataSet normalSet;
    static Map<String, Object> expectedNormal;
    private static DataSet smallSet;
    static Map<String, Object> expectedSmall;
    private static DataSet nullSet;
    static Map<String, Object> expectedNull;
    private static DataSet emptySet;
    private static DataSet zeroSet;
    static Map<String, Object> expectedZero;
    private static DataSet singleSet;
    static Map<String, Object> expectedSingle;
    private static DataSet oddSet;
    static Map<String, Object> expectedOdd;
    private static DataSet evenSet;
    static Map<String, Object> expectedEven;

    private static List<String> measureNames;
    private static final Logger logger = LogManager.getLogger(MeasuresTest.class.getName());

    @BeforeClass
    public static void setup(){
        try {
            RepositoryManager.init();
            MeasureManager.init();
            measureNames = MeasureManager.getAllMeasureNames();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        normalSet = new DataSet();
        normalSet.addSample(new Sample(51.2826,38.7237,17.0567,43.1165,78.3745,93.4345,3.0452,45.9097,16.3817,89.4836,83.2092,26.4851,94.1474,15.6924,83.8544,15.9907,84.792,7.2432,60.3425,61.094));
        normalSet.addSample(new Sample(50.1419,88.3835,72.1587,49.8547,8.4181,48.2371,61.902,39.2482,63.5381,82.8941,78.6294,55.8977,56.7649,99.3522,38.4706,60.3425,43.9826,12.7247,32.0294,68.2918));

        smallSet = new DataSet();
        smallSet.addSample(new Sample(41.6497,55.216,30.591,64.1283,40.972));
        smallSet.addSample(new Sample(33.3913,43.5733,44.4229,89.1558,21.9684));

        nullSet = null;

        emptySet = new DataSet();

        zeroSet = new DataSet();
        zeroSet.addSample(new Sample(0.0, 0.0, 0.0, 0.0, 0.0));
        zeroSet.addSample(new Sample(0.0, 0.0, 0.0, 0.0, 0.0));

        singleSet = new DataSet();
        singleSet.addSample(new Sample(5.0));
        singleSet.addSample(new Sample(10.0));

        oddSet = new DataSet();
        oddSet.addSample(new Sample(78.5262,54.6458,88.9788,51.9339,7.6655,48.4281,42.072,29.5013,58.2589));
        oddSet.addSample(new Sample(48.3825,27.7619,53.737,79.6884,51.9339,93.8262,26.9869,74.3134,76.7971));


        Expressions.addArgument("n", "100");
        Expressions.addArgument("p", "0.6");
        Expressions.addArgument("x", "80");
        Expressions.addArgument("d", "6");

        setupExpected();
    }

    @Test
    public void normalSetTest(){
        logger.info("Testing Average Set:");

        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);

        for(String m : measureNames){
            Boolean eval = false;

            logger.info("  Testing " + m + ":");
            IMeasure measure = MeasureManager.getMeasure(m);
            measure.setInputData(normalSet);

            Long startTime = System.nanoTime();
            Object result = measure.run();
            double execTime = (System.nanoTime() - startTime) / (float) 1_000_000_000;

            if(m == MeasureConstants.binomial || m == MeasureConstants.chi){
                logger.info("    Manual Review: " + result);
                logger.info("    Exec Time: " + df.format(execTime) + " Seconds");
                continue;
            }
            eval = checkResult(result, expectedNormal, m);

            if( result.getClass() == String.class){
                result = "No Display";
            }

            logger.info("    Result: " + (eval ? "Pass": "Fail"));
            logger.info("    Exec Time: " + df.format(execTime) + " Seconds");

            //assert eval;

        }
    }

    @Test
    public void smallSetTest(){
        logger.info("Testing Small Set:");

        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);

        for(String m : measureNames){
            Boolean eval = false;

            logger.info("  Testing " + m + ":");
            IMeasure measure = MeasureManager.getMeasure(m);
            measure.setInputData(smallSet);

            Long startTime = System.nanoTime();
            Object result = measure.run();
            double execTime = (System.nanoTime() - startTime) / (float) 1_000_000_000;

            if(m == MeasureConstants.binomial || m == MeasureConstants.chi){
                logger.info("    Manual Review: " + result);
                logger.info("    Exec Time: " + df.format(execTime) + " Seconds");
                continue;
            }
            eval = checkResult(result, expectedSmall, m);

            if( result.getClass() == String.class){
                result = "No Display";
            }

            logger.info("    Result: " + (eval ? "Pass": "Fail"));
            logger.info("    Exec Time: " + df.format(execTime) + " Seconds");

            //assert eval;

        }
    }

    @Test
    public void nullSetTest(){
        logger.info("Testing Null Set:");

        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);

        for(String m : measureNames){
            Boolean eval = false;

            logger.info("  Testing " + m + ":");
            IMeasure measure = MeasureManager.getMeasure(m);
            measure.setInputData(nullSet);

            Long startTime = System.nanoTime();
            Object result = measure.run();
            double execTime = (System.nanoTime() - startTime) / (float) 1_000_000_000;

            eval = result == null;

            logger.info("    Result: " + (eval ? "Pass": "Fail"));
            logger.info("    Exec Time: " + df.format(execTime) + " Seconds");

            //assert eval;

        }
    }

    @Test
    public void emptySetTest(){
        logger.info("Testing Empty Set:");

        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);

        for(String m : measureNames){
            Boolean eval = false;

            logger.info("  Testing " + m + ":");
            IMeasure measure = MeasureManager.getMeasure(m);
            measure.setInputData(emptySet);

            Long startTime = System.nanoTime();
            Object result = measure.run();
            double execTime = (System.nanoTime() - startTime) / (float) 1_000_000_000;

            eval = result == null;

            logger.info("    Result: " + (eval ? "Pass": "Fail"));
            logger.info("    Exec Time: " + df.format(execTime) + " Seconds");

            //assert eval;

        }
    }

    @Test
    public void zeroSetTest(){
        logger.info("Testing Zero Set:");

        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);

        for(String m : measureNames){
            Boolean eval = false;

            logger.info("  Testing " + m + ":");
            IMeasure measure = MeasureManager.getMeasure(m);
            measure.setInputData(zeroSet);

            if(m == MeasureConstants.probability){
                logger.info("    Skipped");
                continue;
            }

            Long startTime = System.nanoTime();
            Object result = measure.run();
            double execTime = (System.nanoTime() - startTime) / (float) 1_000_000_000;

            if(m == MeasureConstants.binomial || m == MeasureConstants.chi){
                logger.info("    Manual Review: " + result);
                logger.info("    Exec Time: " + df.format(execTime) + " Seconds");
                continue;
            }
            eval = checkResult(result, expectedZero, m);

            if( result.getClass() == String.class){
                result = "No Display";
            }

            logger.info("    Result: " + (eval ? "Pass": "Fail"));
            logger.info("    Exec Time: " + df.format(execTime) + " Seconds");

            //assert eval;

        }
    }

    @Test
    public void singleSetTest(){
        logger.info("Testing Single Set:");

        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);

        for(String m : measureNames){
            Boolean eval = false;

            logger.info("  Testing " + m + ":");
            IMeasure measure = MeasureManager.getMeasure(m);
            measure.setInputData(singleSet);

            if(m == MeasureConstants.correlation || m == MeasureConstants.spearman){
                logger.info("    Skipped");
                continue;
            }

            Long startTime = System.nanoTime();
            Object result = measure.run();
            double execTime = (System.nanoTime() - startTime) / (float) 1_000_000_000;

            if(m == MeasureConstants.binomial || m == MeasureConstants.chi){
                logger.info("    Manual Review: " + result);
                logger.info("    Exec Time: " + df.format(execTime) + " Seconds");
                continue;
            }
            eval = checkResult(result, expectedSingle, m);

            if( result.getClass() == String.class){
                result = "No Display";
            }

            logger.info("    Result: " + (eval ? "Pass": "Fail"));
            logger.info("    Exec Time: " + df.format(execTime) + " Seconds");

            //assert eval;

        }
    }

    @Test
    public void oddSetTest(){
        logger.info("Testing Odd Set:");

        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);

        for(String m : measureNames){
            Boolean eval = false;

            logger.info("  Testing " + m + ":");
            IMeasure measure = MeasureManager.getMeasure(m);
            measure.setInputData(oddSet);

            Long startTime = System.nanoTime();
            Object result = measure.run();
            double execTime = (System.nanoTime() - startTime) / (float) 1_000_000_000;

            if(m == MeasureConstants.binomial || m == MeasureConstants.chi){
                logger.info("    Manual Review: " + result);
                logger.info("    Exec Time: " + df.format(execTime) + " Seconds");
                continue;
            }
            eval = checkResult(result, expectedOdd, m);

            if( result.getClass() == String.class){
                result = "No Display";
            }

            logger.info("    Result: " + (eval ? "Pass": "Fail"));
            logger.info("    Exec Time: " + df.format(execTime) + " Seconds");

            //assert eval;

        }
    }

    private Boolean checkResult(Object result, Map<String, Object> expected, String m) {
        Object expectedResult = expected.get(m);
        Class a = result.getClass();
        Class b = expectedResult.getClass();
        if(result.getClass().equals(expectedResult.getClass())){
            if(result instanceof Double){
                return withinPointOnePercent((double) result, (double) expectedResult);
            }
            if(result instanceof String){
                if(m.equals(MeasureConstants.least)){
                    return testLeastSquare(result.toString(), expectedResult.toString());
                }
            }

        }

        if(result instanceof List){
            return result.equals(expectedResult);
        }


        return false;

    }

    private Boolean testLeastSquare(String result, String expected) {
        double m, b, tm, tb;
        boolean rm, rb;

        String[] t = expected.split(",");
        String[] r = result.split(",");

        m = Double.parseDouble(r[1]);
        b = Double.parseDouble(r[0]);
        tm = Double.parseDouble(t[0]);
        tb = Double.parseDouble(t[1]);

        rm = withinPointOnePercent(m, tm);
        rb = withinPointOnePercent(b, tb);

        return rm && rb;
    }

    private Boolean withinPointOnePercent(double result, double expectedResult) {
        double lowerBound = expectedResult - Math.abs(expectedResult * 0.001);
        double upperBound = expectedResult + Math.abs(expectedResult * 0.001);

        return lowerBound <= result && result <= upperBound;
    }


    private static void setupExpected(){
        expectedNormal = new HashMap<String, Object>();
            expectedNormal.put(MeasureConstants.binomial, 0.0);
            expectedNormal.put(MeasureConstants.chi, 0.0);
            expectedNormal.put(MeasureConstants.coefficient, 51.4165952356);
            expectedNormal.put(MeasureConstants.correlation, -0.1471);
            expectedNormal.put(MeasureConstants.least, "-0.10748,60.98879");
            expectedNormal.put(MeasureConstants.mean, 53.02179);
            expectedNormal.put(MeasureConstants.median, 53.59015);
            expectedNormal.put(MeasureConstants.mode, List.of(60.3425));
            expectedNormal.put(MeasureConstants.percentiles, 83.14618);
            expectedNormal.put(MeasureConstants.probability, 0.0);
            expectedNormal.put(MeasureConstants.rank, 0.0);
            expectedNormal.put(MeasureConstants.sign, 0.0);
            expectedNormal.put(MeasureConstants.spearman, -0.19248);
            expectedNormal.put(MeasureConstants.std, 27.262334971797);
            expectedNormal.put(MeasureConstants.variance, 762.29221);

        expectedSmall = new HashMap<String, Object>();
            expectedSmall.put(MeasureConstants.binomial, 0.0);
            expectedSmall.put(MeasureConstants.chi, 0.0);
            expectedSmall.put(MeasureConstants.coefficient, 39.06033);
            expectedSmall.put(MeasureConstants.correlation, 0.7129);
            expectedSmall.put(MeasureConstants.least, "1.38157,-17.75624");
            expectedSmall.put(MeasureConstants.mean, 46.50687);
            expectedSmall.put(MeasureConstants.median, 42.6115);
            expectedSmall.put(MeasureConstants.mode, List.of(21.9684, 30.591, 33.3913, 40.972, 41.6497, 43.5733, 44.4229, 55.216, 64.1283, 89.1558));
            expectedSmall.put(MeasureConstants.percentiles, 62.34584);
            expectedSmall.put(MeasureConstants.probability, 0.0);
            expectedSmall.put(MeasureConstants.rank, 0.0);
            expectedSmall.put(MeasureConstants.sign, 0.0);
            expectedSmall.put(MeasureConstants.spearman, 0.4);
            expectedSmall.put(MeasureConstants.std, 18.16573759912);
            expectedSmall.put(MeasureConstants.variance, 366.66003);

        expectedNull = new HashMap<String, Object>();
            expectedNull.put(MeasureConstants.binomial, null);
            expectedNull.put(MeasureConstants.chi, null);
            expectedNull.put(MeasureConstants.coefficient, null);
            expectedNull.put(MeasureConstants.correlation, null);
            expectedNull.put(MeasureConstants.least, null);
            expectedNull.put(MeasureConstants.mean, null);
            expectedNull.put(MeasureConstants.median, null);
            expectedNull.put(MeasureConstants.mode, null);
            expectedNull.put(MeasureConstants.percentiles, null);
            expectedNull.put(MeasureConstants.probability, null);
            expectedNull.put(MeasureConstants.rank, null);
            expectedNull.put(MeasureConstants.sign, null);
            expectedNull.put(MeasureConstants.spearman, null);
            expectedNull.put(MeasureConstants.std, null);
            expectedNull.put(MeasureConstants.variance, null);

        expectedZero = new HashMap<String, Object>();
            expectedZero.put(MeasureConstants.binomial, 0.0);
            expectedZero.put(MeasureConstants.chi, 0.0);
            expectedZero.put(MeasureConstants.coefficient, 2.0%0);
            expectedZero.put(MeasureConstants.correlation, 0.0);
            expectedZero.put(MeasureConstants.least, 0.0);
            expectedZero.put(MeasureConstants.mean, 0.0);
            expectedZero.put(MeasureConstants.median, 0.0);
            expectedZero.put(MeasureConstants.mode, 0.0);
            expectedZero.put(MeasureConstants.percentiles, 0.0);
            expectedZero.put(MeasureConstants.probability, 0.0);
            expectedZero.put(MeasureConstants.rank, 0.0);
            expectedZero.put(MeasureConstants.sign, 0.0);
            expectedZero.put(MeasureConstants.spearman, 0.0);
            expectedZero.put(MeasureConstants.std, 0.0);
            expectedZero.put(MeasureConstants.variance, 0.0);

        expectedSingle = new HashMap<String, Object>();
            expectedSingle.put(MeasureConstants.binomial, 0.0);
            expectedSingle.put(MeasureConstants.chi, 0.0);
            expectedSingle.put(MeasureConstants.coefficient, 33.3);
            expectedSingle.put(MeasureConstants.correlation, 0.0);
            expectedSingle.put(MeasureConstants.least, 0.0);
            expectedSingle.put(MeasureConstants.mean, 7.5);
            expectedSingle.put(MeasureConstants.median, 7.5);
            expectedSingle.put(MeasureConstants.mode, List.of(5.0,10.0));
            expectedSingle.put(MeasureConstants.percentiles, 10);
            expectedSingle.put(MeasureConstants.probability, 0.0);
            expectedSingle.put(MeasureConstants.rank, 0.0);
            expectedSingle.put(MeasureConstants.sign, 0.0);
            expectedSingle.put(MeasureConstants.spearman, 0.0);
            expectedSingle.put(MeasureConstants.std, 2.5);
            expectedSingle.put(MeasureConstants.variance, 12.5);

        expectedOdd= new HashMap<String, Object>();
        expectedOdd.put(MeasureConstants.binomial, 0.0);
        expectedOdd.put(MeasureConstants.chi, 0.0);
        expectedOdd.put(MeasureConstants.coefficient, 41.35085);
        expectedOdd.put(MeasureConstants.correlation, -0.0627);
        expectedOdd.put(MeasureConstants.least, "-0.06063,62.36878");
        expectedOdd.put(MeasureConstants.mean, 55.190988888889);
        expectedOdd.put(MeasureConstants.median, 52.83545);
        expectedOdd.put(MeasureConstants.mode, List.of(51.9339));
        expectedOdd.put(MeasureConstants.percentiles, 78.75864);
        expectedOdd.put(MeasureConstants.probability, 0.0);
        expectedOdd.put(MeasureConstants.rank, 0.0);
        expectedOdd.put(MeasureConstants.sign, 0.0);
        expectedOdd.put(MeasureConstants.spearman, 0.0);
        expectedOdd.put(MeasureConstants.std, 22.821943695266);
        expectedOdd.put(MeasureConstants.variance, 551.47883);
    }
}