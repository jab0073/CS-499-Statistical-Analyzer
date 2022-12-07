package Settings;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;

import javax.swing.*;
import java.util.*;

public class Themes {
    private static final Map<String, LookAndFeel> themes = new HashMap<>();

    public static void init(){
        themes.put("Light", new FlatIntelliJLaf());
        themes.put("Dark", new FlatDarkLaf());
        themes.put("Light - Orange", new FlatArcOrangeIJTheme());
        themes.put("Dark - Contrast", new FlatArcDarkContrastIJTheme());
        themes.put("One Dark", new FlatAtomOneDarkIJTheme());
        themes.put("Dark - Orange", new FlatArcDarkOrangeIJTheme());
    }

    /**
     * Gets a LookAndFeel based off of it's name
     * @param theme The name of the LookAndFeel
     * @return The lookAndFeel with the matching name
     */
    public static LookAndFeel getTheme(String theme){
        return themes.get(theme);
    }

    /**
     * Gets the name of all currently available themes
     * @return List of available theme names
     */
    public static ArrayList<String> getThemeNames(){
        ArrayList<String> names = new ArrayList<>(themes.keySet());
        Collections.sort(names);

        return names;
    }

    /**
     * Get the name of the currently loaded theme
     * @return Name of the currently loaded theme
     */
    public static String getCurrentThemeName() {
        String current = UIManager.getLookAndFeel().getName();

        for (Map.Entry<String, LookAndFeel> entry : themes.entrySet()) {
            if (entry.getValue().getName().equals(current)) {
                return entry.getKey();
            }
        }

        return null;
    }
}
