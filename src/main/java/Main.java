import BackEndUtilities.*;
import Interop.UIServices;
import TableUtilities.DataTable;
import TableUtilities.Row;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Starting Main.");
        DataTable dt = new DataTable();
        dt = UIServices.fromCSV("Sample Table", "/Users/justin/Desktop/test.csv");

        //Sample sample = new Sample("1+n", "6000*p", "5+p*100000/n");
        assert dt != null;
        List<Sample> ls = dt.getRows().stream().map(Row::toSample).toList();//new DataSet();

        DataSet ds = new DataSet();
        ls.forEach(ds::addSample);

        Expressions.enableEvaluation();

        Arrays.asList("n=20000", "p=.5").forEach(a->Expressions.addArgument(a.split("=")[0], a.split("=")[1]));

        //ds.addSample(sample);

        String testMeasure = Constants.mean;

        BigDecimal output = (BigDecimal) FunctionCaller.measureRunner(testMeasure, ds);

        //List<BigDecimal> value = Expressions.eval(ds);
        //value.sort(Comparator.naturalOrder());
        //value.forEach(System.out::println);
        System.out.println(testMeasure + ": " + output);
        logger.debug("Leaving Main.");
    }
}
