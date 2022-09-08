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
        logger.trace("Starting Main.");
        DataTable dt = null;
        try {
            dt = UIServices.fromXLSX("Sample Table", "C:\\Users\\jusbus6p\\Desktop\\testingcsv.xlsx", 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DataSet ds = dt.getRow(0).toDataSet();
        // ds.addData(Arrays.asList("x*12", "y/3", "y*12", "x/3", "12", "19", "72"));
        ds.addVariables(Arrays.asList("x=3", "y=12"));
        BigDecimal output = (BigDecimal) FunctionCaller.measureRunner("standard deviation", ds);

        List<BigDecimal> value = Expressions.eval(ds);
        value.sort(Comparator.naturalOrder());
        value.forEach(System.out::println);
        System.out.println("standard deviation: " + output);
        logger.trace("Leaving Main.");
    }
}
