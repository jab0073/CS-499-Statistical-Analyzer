import BackEndUtilities.*;
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

        DataTable dt;

        UserSettings.setWorkingDirectory("/Users/justin/Desktop/SA/");

        RepositoryManager.init();
        MeasureManager.init();
        System.out.println("MEASURES:");
        MeasureManager.getAllMeasureNames().forEach(System.out::println);

        DataSet ds = RepositoryManager.getDataSet("TEST_DS");
        if(ds == null) {
            logger.debug("DataSet not found in repository");
            ds = new DataSet();
            ds.addSample(new Sample(1.0,2.0,100.0,35.0,7.0,12.5));
            ds.setName("TEST_DS");
            RepositoryManager.putDataSet(ds, ds.getName());
        }

        //UserDefinedMeasure udm = new UserDefinedMeasure("TEST", "((x^x)/x)*x", "data");

        String measureName = "TEST_IT";

        IMeasure measure = MeasureManager.getMeasure(measureName);
        if(measure == null) {
            measure = new UserDefinedMeasure(measureName, "data", "data");
            RepositoryManager.putUserDefinedMeasure((UserDefinedMeasure) measure, measure.getName());
        }

        ((UserDefinedMeasure) measure).setAggregate(UserDefinedMeasure.aggregateMode.SUM);
        ((UserDefinedMeasure) measure).setExpression("data");
        Expressions.disableEvaluation();

        Arrays.asList("n=20000", "p=.5").forEach(a->Expressions.addArgument(a.split("=")[0], a.split("=")[1]));

        Measures.setInputData(ds);

        Double output = (Double) measure.run();



        System.out.println(measureName + ": " + output + " based on DataSet " + ds.getName());
        logger.debug("Leaving Main.");
    }
}
