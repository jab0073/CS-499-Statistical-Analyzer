package GUI;

import FrontEndUtilities.ErrorManager;
import FrontEndUtilities.GUIDataMaster;
import FrontEndUtilities.OutputManager;
import Graphing.GraphManager;
import Settings.UserSettings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
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

        // File -> New... -> DataSet menu item
        JMenuItem newDataSetMenuItem = new JMenuItem("DataSet");
        newDataSetMenuItem.getAccessibleContext().setAccessibleDescription("Create a new DataSet.");
        newDataSetMenuItem.addActionListener(l -> {
            // TODO: create functionality to make new dataset
        });
        fileNewMenu.add(newDataSetMenuItem);

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
        JMenuItem openDataSetMenuItem = new JMenuItem("DataSet");
        openDataSetMenuItem.getAccessibleContext().setAccessibleDescription("Open a DataSet.");
        openDataSetMenuItem.addActionListener(l -> {
            // TODO: create functionality to open dataset
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
        JMenuItem fileExportMenuItem = new JMenuItem("Export Data");
        fileExportMenuItem.getAccessibleContext().setAccessibleDescription("Export Data");
        fileExportMenuItem.addActionListener(l -> {
            // TODO: create functionality to export data from table
        });
        fileMenu.add(fileExportMenuItem);

        // File -> Export Data menu
        JMenuItem fileExitMenuItem = new JMenuItem("Exit");
        fileExitMenuItem.getAccessibleContext().setAccessibleDescription("Exit program.");
        fileExitMenuItem.addActionListener(l -> {
            // TODO: Add dialog box to prompt if user wants to save or export before closing

            // TODO: Save Table contents as DataSet or export to preferred file format

            Frame.closeDialogs();
            Arrays.stream(Frame.getFrames()).forEach(Window::dispose);

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
        menuBar.add(settingsMenu);

        // TODO: Add Menu Items for Settings

        /*
            Start Run Menu
         */

        // Build the Run menu
        JMenu runMenu = new JMenu("Run");
        runMenu.getAccessibleContext().setAccessibleDescription("Run related options.");
        runMenu.addActionListener(a -> {
                GUIDataMaster.flush();
                boolean success = GUIDataMaster.executeMeasures();

                if(!success){
                    ErrorManager.displayErrors();
                    return;
                }

                ArrayList<Object> r = GUIDataMaster.getResults();
                for(Object o : r){
                    OutputManager.addOutput((o==null) ? null : o.toString());
                }

                GraphManager.displayGraphs();
                OutputManager.displayOutputs();
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
