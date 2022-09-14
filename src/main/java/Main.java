import BackEndUtilities.*;
import Interop.UIServices;
import TableUtilities.DataTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Starting Main.");
        DataTable dt = null;
        try {
            dt = UIServices.fromXLSX("Sample Table", "C:\\Users\\jusbus6p\\Desktop\\testingcsv.xlsx", 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Sample ds = dt.getRow(0).toSample();
        //DataSet ds = new DataSet();

        ds.addVariables(Arrays.asList("n=20000", "p=.5"));


        BigDecimal output = (BigDecimal) FunctionCaller.measureRunner("binomial distribution", ds);

        //List<BigDecimal> value = Expressions.eval(ds);
        //value.sort(Comparator.naturalOrder());
        //value.forEach(System.out::println);
        System.out.println("bd: " + output);
        logger.debug("Leaving Main.");
    }
}
