import BackEndUtilities.*;
import Graphing.GraphManager;
import Interfaces.IMeasure;
import Interop.UIServices;
import Measures.Measures;
import Measures.UserDefinedMeasure;
import Respository.RepositoryManager;
import Settings.UserSettings;
import TableUtilities.DataTable;
import TableUtilities.Row;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        logger.debug("Starting Main.");

        //UserSettings.setWorkingDirectory("/Users/justin/Desktop/SA/");

        //RepositoryManager.init();
        MeasureManager.init();
        GraphManager.init();

        /**
        System.out.println("MEASURES:");
        MeasureManager.getAllMeasureNames().forEach(System.out::println);

        DataSet ds = RepositoryManager.getDataSet("BLEH");
        if(ds == null) {
            logger.error("DataSet not found in repository");
            ds = new DataSet();
            ds.addSample(new Sample(1.0,2.0,100.0,35.0,7.0,12.5));
            ds.setName("BLEH");
            RepositoryManager.putDataSet(ds, ds.getName());
        }

        //UserDefinedMeasure udm = new UserDefinedMeasure("TEST", "((x^x)/x)*x", "data");

        String measureName = MeasureConstants.percentiles;

        IMeasure measure = MeasureManager.getMeasure(measureName);
        if(measure == null) {
            logger.error("Measure not found in repository");
            measure = new UserDefinedMeasure(measureName, "data*z", "data");
            RepositoryManager.putUserDefinedMeasure((UserDefinedMeasure) measure, measure.getName());
        }

        Expressions.disableEvaluation();

        Arrays.asList("n=20000", "p=.5", "x=100").forEach(a->Expressions.addArgument(a.split("=")[0], a.split("=")[1]));

        if(Expressions.ensureArgument("n")) {
            logger.debug("VARIABLE FOUND");
        }
        else {
            logger.error("VARIABLE NOT FOUND");
        }



        Measures.setInputData(ds);
        measure.setInputData(ds);

        Double output = (Double) measure.run();
         */


        GUI.Frame frame = new GUI.Frame();
        frame.frame();

        logger.debug("Leaving Main.");
    }
}
