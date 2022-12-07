package WaspAnalyzer;

import BackEndUtilities.*;
import Managers.GraphManager;
import Managers.MeasureManager;
import Managers.RepositoryManager;

import Settings.Themes;
import Settings.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        logger.debug("Starting ApplicationMain.Main.");

        if(Arrays.asList(args).contains("--template") || Arrays.asList(args).contains("-t")) {
            UserSettings.init();
            RepositoryManager.init();
            MeasureManager.init();
            DynamicJavaClassLoader.init();
            return;
        }
        else {
            //Initialize all managers and systems
            Themes.init();
            UserSettings.init();
            RepositoryManager.init();
            MeasureManager.init();
            DynamicJavaClassLoader.init();
            GraphManager.init();

            //Create GUI and give reference to the GUIDataMaster
            GUI.Frame frame = new GUI.Frame();
            FrontEndUtilities.GUIDataMaster.setFrameReference(frame);
        }

        logger.debug("Leaving ApplicationMain.Main.");
    }
}

