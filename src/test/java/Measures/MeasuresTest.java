package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import BackEndUtilities.Sample;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MeasuresTest {
    private DataSet ds;
    private static final Logger logger = LogManager.getLogger(MeasuresTest.class.getName());

    @Before
    public void setup() {
        ds = new DataSet();
        ds.addSample(new Sample(1.0, 2.0, 3.0, 4.0, 5.0, 6.0));
        ds.addSample(new Sample(2.0, 3.0, 4.0, 5.0, 6.0, 7.0));
        Expressions.addArgument("n", "10");
        Expressions.addArgument("p", "0.6");
        Expressions.addArgument("x", "80");
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

    @Test
    public void testStandardDeviation() {
        DataSet otherDS = new DataSet();
        otherDS.addSample(new Sample(1.0, 2.0, 3.0, 4.0, 5.0));
        Measures.setInputData(otherDS);
        Object population = Measures.run(Constants.std);
        Double populationD = (Double) population;
        assert populationD != null;
        assert populationD.equals(1.4142135623730951);
        logger.debug("population " + Constants.std + ": " + population);

        Measures.setBiasCorrected(true);
        Object sample = Measures.run(Constants.std);
        Double sampleD = (Double) sample;
        assert sampleD != null;
        assert sampleD.equals(1.5811388300841898);
        logger.debug("sample " + Constants.std + ": " + sample);
    }
}
