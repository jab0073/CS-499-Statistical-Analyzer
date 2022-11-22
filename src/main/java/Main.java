import BackEndUtilities.*;
import Graphing.GraphManager;
import Respository.RepositoryManager;
import javax.swing.*;
import com.formdev.flatlaf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException, UnsupportedLookAndFeelException {
        logger.debug("Starting Main.");

        UIManager.setLookAndFeel(new FlatIntelliJLaf());

        RepositoryManager.init();
        MeasureManager.init();
        DynamicJavaClassLoader.init();
        GraphManager.init();

        new GUI.Frame();

        logger.debug("Leaving Main.");
    }
}

