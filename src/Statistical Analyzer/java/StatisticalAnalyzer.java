import BackEndUtilities.*;
import Graphing.GraphManager;
import Respository.RepositoryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class StatisticalAnalyzer {
    private static final Logger logger = LogManager.getLogger(StatisticalAnalyzer.class);

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        logger.debug("Starting Main.");

        RepositoryManager.init();
        MeasureManager.init();
        GraphManager.init();

        GUI.Frame frame = new GUI.Frame();
        frame.frame();

        logger.debug("Leaving Main.");
    }
}
