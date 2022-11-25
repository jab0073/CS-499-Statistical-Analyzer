package GUI;

import FrontEndUtilities.ErrorManager;
import FrontEndUtilities.GUIDataMaster;
import FrontEndUtilities.OutputManager;
import FrontEndUtilities.SaveManager;
import Graphing.GraphManager;
import Settings.UserSettings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AltMenuBar {
    private JMenuBar menuBar;

    public AltMenuBar() {
        // Create the menu bar
        menuBar = new JMenuBar();

        /*
            Start File Menu
         */

        // Build the File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.getAccessibleContext().setAccessibleDescription("File related options.");
        menuBar.add(fileMenu);

        // File -> New... menu
        JMenu fileNewMenu = new JMenu("New...");
        fileNewMenu.getAccessibleContext().setAccessibleDescription("New File related options.");
        fileMenu.add(fileNewMenu);

        // File -> New... -> Project menu item
        JMenuItem newProjectMenuItem = new JMenuItem("Project");
        newProjectMenuItem.getAccessibleContext().setAccessibleDescription("Create a new Project.");
        newProjectMenuItem.addActionListener(l -> {
            //Display dialog asking if the user would like to save first
            JDialog saveBefore = new JDialog();
            saveBefore.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            JPanel centerPane = new JPanel();
            JPanel buttonPane = new JPanel(new BorderLayout());
            JPanel content = new JPanel(new BorderLayout());

            JLabel message = new JLabel("Would you like to save your project?");

            JButton btnYes = new JButton("Yes");
            JButton btnNo = new JButton("No");
            JButton btnCancel = new JButton("Cancel");

            btnYes.addActionListener(y -> {
                SaveManager.saveProgramState(false);
                GUIDataMaster.newProject();
                saveBefore.dispose();
            });

            btnNo.addActionListener(n -> {
                GUIDataMaster.newProject();
                saveBefore.dispose();
            });

            btnCancel.addActionListener(c -> {
                saveBefore.dispose();
            });

            centerPane.add(message);
            buttonPane.add(btnYes, BorderLayout.WEST);
            buttonPane.add(btnNo, BorderLayout.CENTER);
            buttonPane.add(btnCancel, BorderLayout.EAST);

            content.add(centerPane, BorderLayout.CENTER);
            content.add(buttonPane, BorderLayout.SOUTH);

            saveBefore.setContentPane(content);

            saveBefore.pack();

            saveBefore.setLocationRelativeTo(GUIDataMaster.getFrameReference());

            saveBefore.setVisible(true);


        });
        fileNewMenu.add(newProjectMenuItem);

        // File -> New... -> Custom Measure menu item
        JMenuItem newCustomMeasureMenuItem = new JMenuItem("Custom Measure");
        newCustomMeasureMenuItem.getAccessibleContext().setAccessibleDescription("Create a new Custom Measure.");
        newCustomMeasureMenuItem.addActionListener(l -> {
            // TODO: create functionality to make new user defined measure
        });
        fileNewMenu.add(newCustomMeasureMenuItem);

        // File -> Open... menu
        JMenu fileOpenMenu = new JMenu("Open...");
        fileNewMenu.getAccessibleContext().setAccessibleDescription("Open File related options.");
        fileMenu.add(fileOpenMenu);

        // File -> Open... -> DataSet menu item
        JMenuItem openDataSetMenuItem = new JMenuItem("Project");
        openDataSetMenuItem.getAccessibleContext().setAccessibleDescription("Open a Project.");
        openDataSetMenuItem.addActionListener(l -> {
            // TODO: create functionality to open dataset
            SaveManager.openSaveFile();
        });
        fileOpenMenu.add(openDataSetMenuItem);

        // File -> Open... -> Custom Measure menu item
        JMenuItem openCustomMeasureMenuItem = new JMenuItem("Custom Measure");
        openCustomMeasureMenuItem.getAccessibleContext().setAccessibleDescription("Open a Custom Measure.");
        openCustomMeasureMenuItem.addActionListener(l -> {
            // TODO: create functionality to open user defined measure
        });
        fileOpenMenu.add(openCustomMeasureMenuItem);

        // File -> Import... menu
        JMenu fileImportMenu = new JMenu("Import...");
        fileImportMenu.getAccessibleContext().setAccessibleDescription("Import Data");
        fileMenu.add(fileImportMenu);

        // File -> Import... -> CSV Data menu
        JMenuItem fileImportCSVMenuItem = new JMenuItem("CSV");
        fileImportCSVMenuItem.getAccessibleContext().setAccessibleDescription("Import CSV data.");
        fileImportCSVMenuItem.addActionListener(l -> {

            // From MenuBar File button action listener

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(UserSettings.getWorkingDirectory()));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
            fileChooser.setFileFilter(filter);

            JDialog dialog = new JDialog();

            int result = fileChooser.showOpenDialog(dialog);

            dialog.setVisible(true);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                CellsTable.loadFile(selectedFile.getAbsolutePath());
            }
            dialog.dispose();

        });
        fileImportMenu.add(fileImportCSVMenuItem);

        // File -> Import... -> XLSX Data menu
        JMenuItem fileImportXLSXMenuItem = new JMenuItem("XLSX");
        fileImportXLSXMenuItem.getAccessibleContext().setAccessibleDescription("Import XLSX data.");
        fileImportXLSXMenuItem.addActionListener(l -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(UserSettings.getWorkingDirectory()));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XSLX Files", "xlsx");
            fileChooser.setFileFilter(filter);

            JDialog dialog = new JDialog();

            int result = fileChooser.showOpenDialog(dialog);

            dialog.setVisible(true);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    CellsTable.loadXLSXFile(selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    return;
                }
            }
            dialog.dispose();
        });
        fileImportMenu.add(fileImportXLSXMenuItem);

        // File -> Export Data menu
        JMenuItem fileSaveMenuItem = new JMenuItem("Save Project");
        fileSaveMenuItem.getAccessibleContext().setAccessibleDescription("Save Project");
        fileSaveMenuItem.addActionListener(l -> {
            SaveManager.saveProgramState(false);
        });
        fileMenu.add(fileSaveMenuItem);

        // File -> Export Data menu
        JMenuItem fileSaveAsMenuItem = new JMenuItem("Save Project As");
        fileSaveAsMenuItem.getAccessibleContext().setAccessibleDescription("Save Project As");
        fileSaveAsMenuItem.addActionListener(l -> {
            SaveManager.saveProgramState(true);
        });
        fileMenu.add(fileSaveAsMenuItem);

        // File -> Export Data menu
        JMenuItem fileExitMenuItem = new JMenuItem("Exit");
        fileExitMenuItem.getAccessibleContext().setAccessibleDescription("Exit program.");
        fileExitMenuItem.addActionListener(l -> {
            //Display dialog asking if the user would like to save first
            JDialog saveBefore = new JDialog();
            saveBefore.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            JPanel centerPane = new JPanel();
            JPanel buttonPane = new JPanel(new BorderLayout());
            JPanel content = new JPanel(new BorderLayout());

            JLabel message = new JLabel("Would you like to save your project?");

            JButton btnYes = new JButton("Yes");
            JButton btnNo = new JButton("No");
            JButton btnCancel = new JButton("Cancel");

            btnYes.addActionListener(y -> {
                SaveManager.saveProgramState(false);
                Frame.closeDialogs();
                Arrays.stream(Frame.getFrames()).forEach(Window::dispose);
            });

            btnNo.addActionListener(n -> {
                Frame.closeDialogs();
                Arrays.stream(Frame.getFrames()).forEach(Window::dispose);
            });

            btnCancel.addActionListener(c -> {
                saveBefore.dispose();
            });

            centerPane.add(message);
            buttonPane.add(btnYes, BorderLayout.WEST);
            buttonPane.add(btnNo, BorderLayout.CENTER);
            buttonPane.add(btnCancel, BorderLayout.EAST);

            content.add(centerPane, BorderLayout.CENTER);
            content.add(buttonPane, BorderLayout.SOUTH);

            saveBefore.setContentPane(content);

            saveBefore.pack();

            saveBefore.setLocationRelativeTo(GUIDataMaster.getFrameReference());

            saveBefore.setVisible(true);

        });
        fileMenu.add(fileExitMenuItem);

        /*
            Start Edit Menu
         */

        // Build the Edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.getAccessibleContext().setAccessibleDescription("Edit related options.");
        menuBar.add(editMenu);

        JMenuItem editCopyMenuItem = new JMenuItem("Copy");
        editCopyMenuItem.getAccessibleContext().setAccessibleDescription("Copy Selected Data.");
        editCopyMenuItem.addActionListener(l -> {
            String data = CellsTable.getSelectedData();
        });
        fileMenu.add(editCopyMenuItem);

        /*
            Start Settings Menu
         */

        // Build the Settings menu
        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.getAccessibleContext().setAccessibleDescription("Settings related options.");
        settingsMenu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SettingWindow();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        menuBar.add(settingsMenu);

        // TODO: Add Menu Items for Settings

        /*
            Start Run Menu
         */

        // Build the Run menu
        JMenu runMenu = new JMenu("Run");
        runMenu.getAccessibleContext().setAccessibleDescription("Run related options.");
        runMenu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GUIDataMaster.flush();
                boolean success = GUIDataMaster.executeMeasures();

                if(!success){
                    ErrorManager.displayErrors();
                    return;
                }

                ArrayList<Object> r = GUIDataMaster.getResults();
                for(Object o : r){
                    OutputManager.addOutput(GUIDataMaster.getGUIMeasure(r.indexOf(o)).getName() ,(o==null) ? null : o.toString());
                }

                GraphManager.displayGraphs();
                OutputManager.displayOutputs();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        menuBar.add(runMenu);

        // TODO: Add Menu Items for Run

        // Build the Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.getAccessibleContext().setAccessibleDescription("Help related options.");
        menuBar.add(helpMenu);

        // TODO: Add Menu Items for Help

        menuBar.setVisible(true);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
