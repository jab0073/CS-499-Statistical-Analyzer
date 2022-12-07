package GUI;

import Settings.UserSettings;
import WaspAnalyzer.Main;
import BackEndUtilities.Expressions;
import FrontEndUtilities.GUIDataMaster;
import Settings.Themes;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class SettingWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel themeLabel;
    private JComboBox<String> themeSelector;
    private JSlider zoomSlider;
    private JLabel zoomReadout;
    private JCheckBox EvalCheckBox;
    private JLabel WarningLabel;
    private JCheckBox biasCheckBox;
    private JTextField workingDirectoryTextField;
    private JLabel wdLabel;
    private JButton browseButton;

    public SettingWindow() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.setTitle("Settings");

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        String currentTheme = Themes.getCurrentThemeName();

        for(String s : Themes.getThemeNames()){
            themeSelector.addItem(s);
        }

        themeSelector.setSelectedItem(currentTheme);

        zoomSlider.addChangeListener(e -> {
            //Convert slider 0-100 range to 25-200 range
            int zoom = (int) Math.round((zoomSlider.getValue()*1.75) + 25);
            zoomReadout.setText(zoom + "%");
        });

        //Get the current font and set the value of the zoom slider based on the current font size
        Font currentFont = (Font) UIManager.get("defaultFont");
        double currentZoomSlide = (((double) currentFont.getSize()-3.0)/21.0) * 100;

        zoomSlider.setValue((int) Math.round(currentZoomSlide));

        WarningLabel.setVisible(false);

        EvalCheckBox.addChangeListener(e -> WarningLabel.setVisible(EvalCheckBox.isSelected()));

        EvalCheckBox.setSelected(Expressions.isEvaluationOn());

        biasCheckBox.setSelected(GUIDataMaster.isBiasCorrection());

        this.workingDirectoryTextField.setText(UserSettings.getWorkingDirectory());

        this.browseButton.addActionListener(a -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(GUIDataMaster.getFrameReference());
            if(option == JFileChooser.APPROVE_OPTION){
                this.workingDirectoryTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }else{
                this.workingDirectoryTextField.setText(UserSettings.getWorkingDirectory());
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.pack();

        //Set the position of the settings window to the center of the rest of the program
        this.setLocationRelativeTo(GUIDataMaster.getFrameReference());

        this.setVisible(true);
    }

    private void onOK() {
        String themeName = (String) themeSelector.getSelectedItem();
        try{
            Frame frame = GUIDataMaster.getFrameReference();

            //Convert 0-100 range to 25-200 range
            float zoom =  ((float) zoomSlider.getValue()*1.75F) + 25F;

            //Set the zoom value and theme to those selected by the user
            frame.setLookAndFeel(themeName, zoom);

            if(EvalCheckBox.isSelected()){
                Expressions.enableEvaluation();
            }else{
                Expressions.disableEvaluation();
            }

            GUIDataMaster.setBiasCorrection(biasCheckBox.isSelected());

            UserSettings.setWorkingDirectory(this.workingDirectoryTextField.getText());

            //Save users chosen settings
            UserSettings.saveUserSettings(Themes.getCurrentThemeName()
                    ,String.valueOf(zoom)
                    ,String.valueOf(EvalCheckBox.isSelected())
                    ,String.valueOf(biasCheckBox.isSelected())
                    ,UserSettings.getWorkingDirectory());


        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
