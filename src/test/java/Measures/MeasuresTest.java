package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.Sample;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mariuszgromada.math.mxparser.Constant;

import java.util.List;

public class MeasuresTest {
    private DataSet ds;
    private static final Logger logger = LogManager.getLogger(MeasuresTest.class.getName());
    @Before
    public void setup() {
        ds = new DataSet();
        ds.addSample(new Sample(1.0, 2.0, 3.0, 5.0, 100.0, 3000.0));
        ds.addSample(new Sample(12.0, 54.0, 100.0, 7.0, 13.0, 99.9));
        Expressions.addArgument("n", "10");
        Expressions.addArgument("p", ".6");
        Expressions.addArgument("x", "5");
        Expressions.addArgument("d", "6");

        Measures.setInputData(ds);
    }

    @Test
    public void testAllMeasure() {
        List<String> measures = Constants.getAllConstants();

        measures.forEach(m -> {
            Object result = Measures.run(m);
            assert result != null;
            logger.debug(m + ": " + result);
        });
    }
}
