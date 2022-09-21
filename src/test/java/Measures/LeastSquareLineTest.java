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

public class LeastSquareLineTest {
    DataSet ds;
    private static final Logger logger = LogManager.getLogger(IMeasureTest.class.getName());
    @Before
    public void setup() {
        ds = new DataSet();
    }

    @Test
    public void proper() {
        ds.addSample(new Sample(1.0, 2.0, 3.0));
        ds.addSample(new Sample(4.0, 5.0, 6.0));

        String value = (String) FunctionCaller.measureRunner("least square line", ds);
        logger.debug(value);

        assert value != null;
        assert value.equals("Y=3.0+1.0X");

        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }

    @Test
    public void singleSample() {
        ds.addSample(new Sample(1.0,2.0,3.0));

        String value = (String) FunctionCaller.measureRunner("least square line", ds);
        assert value == null;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }

    @Test
    public void emptySample() {
        ds.addSample(new Sample());

        BigDecimal value = (BigDecimal) FunctionCaller.measureRunner("least square line", ds);
        assert value == null;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }

    @Test
    public void emptyDataSet() {
        BigDecimal value = (BigDecimal) FunctionCaller.measureRunner("least square line", ds);
        assert value == null;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }

    @Test
    public void nullDataSet() {
        BigDecimal value = (BigDecimal) FunctionCaller.measureRunner("least square line", null);
        assert value == null;
        String functionName = new Throwable().getStackTrace()[0].getMethodName();
        logger.debug(functionName + " returned expected results.");
    }
}
