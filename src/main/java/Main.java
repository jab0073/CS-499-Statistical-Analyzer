import BackEndUtilities.*;
import GUI.Frame;
import Graphing.GraphManager;
import javax.swing.*;
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
    private static JFrame frame;

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        logger.debug("Starting Main.");

        //UserSettings.setWorkingDirectory("/Users/justin/Desktop/SA/");

        //RepositoryManager.init();
        MeasureManager.init();
        GraphManager.init();


        frame = new Frame();

        logger.debug("Leaving Main.");
    }

    public JFrame returnFrame(){return(frame);}
}
