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
    private static boolean isMacOS = false;

    /**
     * Loads the Users settings from the OS Registry
     */
    public static void init() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);

        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            isMacOS = true;
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

        //Convert the font size percentage saved into a usable font size
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
            if (!isMacOS) {
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

    /**
     * Save the specified settings to the OS Registry
     * @param userTheme the user selected theme
     * @param userZoom the user zoom percentage
     * @param userEval whether expression evaluation is enabled
     * @param userBias whether bias correction is enabled
     * @param userWD the users working directory
     */
    public static void saveUserSettings(String userTheme, String userZoom, String userEval, String userBias, String userWD){
        Preferences prefs = Preferences.userNodeForPackage(Main.class);

        prefs.put("userTheme", userTheme);
        prefs.put("userZoom", userZoom);
        prefs.put("userEval", userEval);
        prefs.put("userBias", userBias);
        prefs.put("userWD", userWD);
    }

}
