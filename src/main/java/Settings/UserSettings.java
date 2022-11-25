package Settings;

import ApplicationMain.Main;
import BackEndUtilities.Constants;
import FrontEndUtilities.GUIDataMaster;
import GUI.AltMenuBar;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class UserSettings {

    private static String workingDirectory;

    /**
     * On Windows the directory is "C:\Users\[user]\Documents\Statistical-Analysis\"
     * On macOS the directory is "/Users/[user]/Documents/Statistical-Analysis/"
     */
    public static void init() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);

        String userTheme = prefs.get("userTheme", "Light");
        float userZoom = Float.parseFloat(prefs.get("userZoom", "100"));

        try {
            UIManager.setLookAndFeel(Themes.getTheme(userTheme));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        int fontSize = (int) (12 * ((double)userZoom/100.0));

        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Segoe UI", Font.PLAIN, fontSize));

        if(System.getProperty("os.name").toLowerCase().startsWith("win")) {
            UserSettings.workingDirectory =  Constants.WindowsBeginningDefaultDir + System.getProperty("user.name") + Constants.WindowsEndingDefaultDir;
            return;
        }
        else if(System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            UserSettings.workingDirectory =  Constants.MacBeginningDir + System.getProperty("user.name") + Constants.MacDefaultDir;
            // If running on macOS, this next line puts the JMenuBar in the system menu bar
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            AltMenuBar.isMacOS = true;
            return;
        }
        UserSettings.workingDirectory = null;
    }

    public static String getWorkingDirectory() {
        return UserSettings.workingDirectory;
    }


}
