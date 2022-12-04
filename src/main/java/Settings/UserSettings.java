package Settings;

import Managers.RepositoryManager;
import WaspAnalyzer.Main;
import Constants.Constants;
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

        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            // If running on macOS, this next line puts the JMenuBar in the system menu bar
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            AltMenuBar.isMacOS = true;
        }

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

        UserSettings.workingDirectory = prefs.get("userWD", "N/A");
        if(UserSettings.workingDirectory.equals("N/A")) {
            if (!AltMenuBar.isMacOS) {
                UserSettings.workingDirectory = Constants.WindowsBeginningDefaultDir + System.getProperty("user.name") + Constants.WindowsEndingDefaultDir;
            } else {
                UserSettings.workingDirectory = Constants.MacBeginningDir + System.getProperty("user.name") + Constants.MacDefaultDir;
            }
        }
    }

    public static String getWorkingDirectory() {
        return UserSettings.workingDirectory;
    }

    public static void setWorkingDirectory(String workingDirectory) {
        UserSettings.workingDirectory = workingDirectory;
        RepositoryManager.buildWD();
        GUIDataMaster.getFrameReference().updateWD();
    }

}
