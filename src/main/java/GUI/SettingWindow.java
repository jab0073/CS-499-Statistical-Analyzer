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
import java.io.File;
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

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        String currentTheme = Themes.getCurrentThemeName();

        for(String s : Themes.getThemeNames()){
            themeSelector.addItem(s);
        }

        themeSelector.setSelectedItem(currentTheme);

        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int zoom = (int) Math.round((zoomSlider.getValue()*1.75) + 25);
                zoomReadout.setText(zoom + "%");
            }
        });

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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.pack();

        this.setLocationRelativeTo(GUIDataMaster.getFrameReference());

        this.setVisible(true);
    }

    private void onOK() {
        String themeName = (String) themeSelector.getSelectedItem();
        try{
            Frame frame = GUIDataMaster.getFrameReference();

            float zoom =  ((float) zoomSlider.getValue()*1.75F) + 25F;

            frame.setLookAndFeel(themeName, zoom);

            Preferences prefs = Preferences.userNodeForPackage(Main.class);

            if(EvalCheckBox.isSelected()){
                Expressions.enableEvaluation();
            }else{
                Expressions.disableEvaluation();
            }

            GUIDataMaster.setBiasCorrection(biasCheckBox.isSelected());

            UserSettings.setWorkingDirectory(this.workingDirectoryTextField.getText());

            prefs.put("userTheme", Themes.getCurrentThemeName());
            prefs.put("userZoom", String.valueOf(zoom));
            prefs.put("userEval", String.valueOf(EvalCheckBox.isSelected()));
            prefs.put("userBias", String.valueOf(biasCheckBox.isSelected()));
            prefs.put("userWD", UserSettings.getWorkingDirectory());

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
