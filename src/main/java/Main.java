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
        /*try {
            dt = UIServices.fromXLSX("Sample Table", "C:\\Users\\jusbus6p\\Desktop\\testingcsv.xlsx", 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        Sample sample = new Sample("1+n", "6000*p", "5+p*100000/n");
        DataSet ds = new DataSet();

        Expressions.enableEvaluation();

        Arrays.asList("n=20000", "p=.5").forEach(a->Expressions.addArguement(a.split("=")[0], a.split("=")[1]));

        ds.addSample(sample);

        String testMeasure = Constants.mean;

        BigDecimal output = (BigDecimal) FunctionCaller.measureRunner(testMeasure, ds);

        //List<BigDecimal> value = Expressions.eval(ds);
        //value.sort(Comparator.naturalOrder());
        //value.forEach(System.out::println);
        System.out.println(testMeasure + ": " + output);
        logger.debug("Leaving Main.");
    }
}
