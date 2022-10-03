import BackEndUtilities.*;
import Interop.UIServices;
import Measures.Measures;
import Measures.UserDefinedMeasure;
import Respository.RepositoryManager;
import Settings.UserSettings;
import TableUtilities.DataTable;
import TableUtilities.Row;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Starting Main.");
        DataTable dt;

        UserSettings.setWorkingDirectory("/Users/justin/Desktop/SA/");

        RepositoryManager.init();
        RepositoryManager.getAllUserDefinedMeasures().forEach(m->System.out.println(m.getName()));

        // String inputTestCSV = "/Users/justin/Desktop/test.csv";

        // dt = UIServices.fromCSV("Sample Table", inputTestCSV);

        // Sample sample = new Sample("1+n", "6000*p", "5+p*100000/n");
        // assert dt != null;
        // List<Sample> ls = dt.getRows().stream().map(Row::toSample).toList();//new DataSet();

        DataSet ds = new DataSet();
        //ls.forEach(ds::addSample);
        ds.addSample(new Sample(1.0,2.0,100.0,35.0,7.0,12.5));

        ds.save("/Users/justin/Desktop/SA/Analysis/testDS.json");

        Expressions.disableEvaluation();

        Arrays.asList("n=20000", "p=.5").forEach(a->Expressions.addArgument(a.split("=")[0], a.split("=")[1]));

        Measures.setInputData(ds);


        UserDefinedMeasure udm = RepositoryManager.getUserDefinedMeasure("TEST3");

        Double output = (Double) Measures.run("TEST3");

        System.out.println("TEST3: " + output);
        logger.debug("Leaving Main.");
    }
}
