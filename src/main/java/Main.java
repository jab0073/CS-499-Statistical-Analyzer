import BackEndUtilities.*;
import TableUtilities.DataTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Starting Main.");
        DataTable dt = new DataTable();
        dt.addRow(new Sample(1.0,2.0,3.0));
        /*try {
            dt = UIServices.fromXLSX("Sample Table", "C:\\Users\\jusbus6p\\Desktop\\testingcsv.xlsx", 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        Sample sample = dt.getRow(0).toSample();
        DataSet ds = new DataSet();

        sample.addVariables(Arrays.asList("n=20000", "p=.5"));

        ds.addSample(sample);

        BigDecimal output = (BigDecimal) FunctionCaller.measureRunner("standard deviation", ds);

        //List<BigDecimal> value = Expressions.eval(ds);
        //value.sort(Comparator.naturalOrder());
        //value.forEach(System.out::println);
        System.out.println("std dev: " + output);
        logger.debug("Leaving Main.");
    }
}
