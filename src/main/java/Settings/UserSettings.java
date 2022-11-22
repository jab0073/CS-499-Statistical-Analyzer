package Settings;

import BackEndUtilities.Constants;

public class UserSettings {

    private static String workingDirectory;

    /**
     * On Windows the directory is "C:\Users\[user]\Documents\Statistical-Analysis\"
     * On macOS the directory is "/Users/[user]/Documents/Statistical-Analysis/"
     */
    public static void init() {
        if(System.getProperty("os.name").toLowerCase().startsWith("win")) {
            UserSettings.workingDirectory =  Constants.WindowsBeginningDefaultDir + System.getProperty("user.name") + Constants.WindowsEndingDefaultDir;
            return;
        }
        else if(System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            UserSettings.workingDirectory =  Constants.MacBeginningDir + System.getProperty("user.name") + Constants.MacDefaultDir;
            // If running on macOS, this next line puts the JMenuBar in the system menu bar
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Statistical Analyzer");
            return;
        }
        UserSettings.workingDirectory = null;
    }

    public static void setWorkingDirectory(String workingDirectory) {
        UserSettings.workingDirectory = workingDirectory;
    }

    public static String getWorkingDirectory() {
        return UserSettings.workingDirectory;
    }


}
