package Settings;

public class UserSettings {

    private static String workingDirectory;

    public static String getWorkingDirectory() {
        return workingDirectory;
    }

    public static void setWorkingDirectory(String workingDirectory) {
        UserSettings.workingDirectory = workingDirectory;
    }
}
