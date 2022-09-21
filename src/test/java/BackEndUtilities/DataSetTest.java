package BackEndUtilities;

import org.junit.Before;
import org.junit.Test;

public class DataSetTest {

    private DataSet ds;
    private Sample numericSample;
    private Sample expressionSample;
    private Sample nullSample;
    @Before
    public void setup() {
        ds = new DataSet();
        numericSample = new Sample(1.0, 2.0, 3.0);
        expressionSample = new Sample("x-1", "x+0", "y+2");
        expressionSample.addVariables("x=2");
        expressionSample.addVariables("y=1");
        nullSample = null;
    }

    @Test
    public void addSample() {
        ds.addSample(numericSample);
        assert numericSample.equals(ds.getSample(0));
        ds.addSample(expressionSample);
        ds.getSample(1).evaluate();
        assert expressionSample.equals(ds.getSample(1));
    }

    @Test
    public void addSample2() {
        ds.addSample(numericSample);
        assert numericSample.equals(ds.getSample(0));
        ds.addSample(0, expressionSample);
        ds.getSample(0).evaluate();
        assert expressionSample.equals(ds.getSample(0));
        assert numericSample.equals(ds.getSample(1));
    }

    @Test
    public void getSamples() {

    }

    @Test
    public void setSamples() {
    }

    @Test
    public void testSetSamples() {
    }

    @Test
    public void getSample() {
    }

    @Test
    public void getDataAsDouble() {
    }

    @Test
    public void getSize() {
    }

    @Test
    public void testGetSize() {
    }

    @Test
    public void getNumberOfSamples() {
    }

    @Test
    public void toTable() {
    }

    @Test
    public void fromTable() {
    }

    @Test
    public void testClone() {
    }
}
