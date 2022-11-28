package WaspAnalyzer;

import BackEndUtilities.*;
import Graphing.GraphManager;
import Respository.RepositoryManager;

import Settings.Themes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        logger.debug("Starting ApplicationMain.Main.");

        Themes.init();

        RepositoryManager.init();
        MeasureManager.init();
        DynamicJavaClassLoader.init();
        GraphManager.init();

        GUI.Frame frame = new GUI.Frame();
        FrontEndUtilities.GUIDataMaster.setFrameReference(frame);

        logger.debug("Leaving ApplicationMain.Main.");
    }
}

