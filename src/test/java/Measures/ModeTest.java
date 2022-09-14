package Measures;

import BackEndUtilities.DataSet;
import BackEndUtilities.FunctionCaller;
import BackEndUtilities.Sample;
import Interfaces.IMeasureTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ModeTest {
    DataSet ds;
    private static final Logger logger = LogManager.getLogger(IMeasureTest.class.getName());
    @Before
    public void setup() {
        ds = new DataSet();
    }

    @Test
    public void proper() {
        ds.addSample(new Sample(1.0, 2.0, 3.0, 2.0));

        BigDecimal value = (BigDecimal) FunctionCaller.measureRunner("mode", ds);
        assert value != null;
        assert value.doubleValue() == 2.0;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }

    @Test
    public void emptySample() {
        ds.addSample(new Sample());

        BigDecimal value = (BigDecimal) FunctionCaller.measureRunner("mode", ds);
        assert value == null;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }

    @Test
    public void emptyDataSet() {
        BigDecimal value = (BigDecimal) FunctionCaller.measureRunner("mode", ds);
        assert value == null;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }

    @Test
    public void noMode() {
        ds.addSample(new Sample(1.0, 2.0, 3.0));

        BigDecimal value = (BigDecimal) FunctionCaller.measureRunner("mode", ds);
        assert value == null;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }

    @Test
    public void nullDataSet() {
        DataSet ds2 = null;
        BigDecimal value = (BigDecimal) FunctionCaller.measureRunner("mode", null);
        assert value == null;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }
}
