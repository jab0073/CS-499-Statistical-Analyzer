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
        dt.addRow(new Sample(1.2, 3.2, 5.4, 7.9, 101.2));
        /*try {
            dt = UIServices.fromXLSX("Sample Table", "C:\\Users\\jusbus6p\\Desktop\\testingcsv.xlsx", 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        Sample sample = dt.getRow(0).toSample();
        DataSet ds = new DataSet();

        sample.addVariables(Arrays.asList("n=20000", "p=.5"));

        ds.addSample(sample);

        BigDecimal output = (BigDecimal) FunctionCaller.measureRunner("mean", ds);

        //List<BigDecimal> value = Expressions.eval(ds);
        //value.sort(Comparator.naturalOrder());
        //value.forEach(System.out::println);
        System.out.println("mean: " + output);
        logger.debug("Leaving Main.");
    }
}
