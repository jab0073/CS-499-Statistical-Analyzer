import BackEndUtilities.*;
import GUI.Frame;
import Graphing.GraphManager;
import javax.swing.*;

import com.formdev.flatlaf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static JFrame frame;

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        logger.debug("Starting Main.");

        UIManager.setLookAndFeel(new FlatIntelliJLaf());

        //UserSettings.setWorkingDirectory("/Users/justin/Desktop/SA/");

        //RepositoryManager.init();
        MeasureManager.init();
        GraphManager.init();


        frame = new Frame();

        logger.debug("Leaving Main.");
    }

    public JFrame returnFrame(){return(frame);}
}
