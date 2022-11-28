package Settings;

import WaspAnalyzer.Main;
import BackEndUtilities.Constants;
import BackEndUtilities.Expressions;
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
        boolean userEval = Boolean.parseBoolean(prefs.get("userEval", "false"));
        boolean userBias = Boolean.parseBoolean(prefs.get("userBias", "false"));

        try {
            UIManager.setLookAndFeel(Themes.getTheme(userTheme));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        int fontSize = Math.round(12F * (userZoom/100.0F));

        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Segoe UI", Font.PLAIN, fontSize));

        if(userEval){
            Expressions.enableEvaluation();
        }else{
            Expressions.disableEvaluation();
        }

        GUIDataMaster.setBiasCorrection(userBias);

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
