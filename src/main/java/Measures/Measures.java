package Measures;

import BackEndUtilities.DataSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Measures {
    private static DataSet inputData = new DataSet();
    private static final Logger logger = LogManager.getLogger(Measures.class.getName());

    public static DataSet getInputData() {
        return inputData;
    }

    public static void setInputData(DataSet ds) {
        inputData = ds;
    }
    public static Logger getLogger() {
        return logger;
    }
}
