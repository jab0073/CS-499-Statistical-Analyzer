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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        logger.debug("Starting Main.");
        DataTable dt;

        UserSettings.setWorkingDirectory("C:\\Users\\jusbus6p\\Desktop\\SA\\");

        RepositoryManager.init();
        MeasureManager.init();
        RepositoryManager.getAllUserDefinedMeasures().forEach(m->System.out.println(m.getName()));


        // String inputTestCSV = "/Users/justin/Desktop/test.csv";

        // dt = UIServices.fromCSV("Sample Table", inputTestCSV);

        // Sample sample = new Sample("1+n", "6000*p", "5+p*100000/n");
        // assert dt != null;
        // List<Sample> ls = dt.getRows().stream().map(Row::toSample).toList();//new DataSet();

        DataSet ds = new DataSet();
        //ls.forEach(ds::addSample);
        ds.addSample(new Sample(1.0,2.0,100.0,35.0,7.0,12.5));

        UserDefinedMeasure udm1 = new UserDefinedMeasure("TEST", "((x^x)/x)*x", "x");

        RepositoryManager.putUserDefinedMeasure(udm1, udm1.getName());

        Expressions.disableEvaluation();

        Arrays.asList("n=20000", "p=.5").forEach(a->Expressions.addArgument(a.split("=")[0], a.split("=")[1]));

//        Measures.setInputData(ds);
//
//
//        UserDefinedMeasure udm = RepositoryManager.getUserDefinedMeasure("TEST");
//
//        Double output = (Double) udm.run();
//
//        System.out.println("TEST: " + output);
        logger.debug("Leaving Main.");
    }
}
