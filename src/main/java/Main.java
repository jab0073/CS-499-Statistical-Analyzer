import BackEndUtilities.*;
import Graphing.GraphManager;
import Respository.RepositoryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
        logger.debug("Starting Main.");


        RepositoryManager.init();
        MeasureManager.init();
        DynamicJavaClassLoader.init();
        GraphManager.init();

        GUI.Frame frame = new GUI.Frame();
        frame.frame();

        logger.debug("Leaving Main.");
    }
}

