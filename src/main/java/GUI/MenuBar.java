package GUI;

import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class MenuBar {
    /**Method which returns the menu bar.
     *@return The panel containing the menu bar.*/
    public JPanel topPanel(){
        /*Create a JPanel, give it a background color and a border.*/
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(new Color((float)0.760, (float)0.760, (float)0.760));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        panel.setBorder(border);

        /*Add all the buttons that make up the menu bar to the panel.*/
        panel.add(fileButton());
        panel.add(editButton());
        panel.add(settingsButton());
        panel.add(downloadButton());
        panel.add(runButton());
        panel.add(helpButton());
        return (panel);
    }

    /**Method which creates the file button.
     *@return The file button.*/
    private JButton fileButton() {
        JButton file = new JButton("File");
        file.setOpaque(false);
        file.setContentAreaFilled(false);
        file.setBorderPainted(false);

        file.addActionListener(new ActionListener() {
            @Override
            /**
             * Opens a file browser for the user to select a file to import to the chart
             * File types are restricted to CSV, but can be expanded
             */
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "CSV Files", "csv");
                fileChooser.setFileFilter(filter);

                JDialog dialog = new JDialog();

                int result = fileChooser.showOpenDialog(dialog);

                dialog.setVisible(true);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    CellsTable.loadFile(selectedFile.getAbsolutePath());
                }

                dialog.dispose();
            }
        });

        return (file);
    }

    /**Method which creates the edit button.
     *@return The edit button.*/
    private JButton editButton() {
        JButton edit = new JButton("Edit");
        edit.setOpaque(false);
        edit.setContentAreaFilled(false);
        edit.setBorderPainted(false);
        return (edit);
    }

    /**Method which creates the settings button.
     *@return The settings button.*/
    private JButton settingsButton() {
        JButton settings = new JButton("Settings");
        settings.setOpaque(false);
        settings.setContentAreaFilled(false);
        settings.setBorderPainted(false);
        return (settings);
    }

    /**Method which creates the help button.
     *@return The help button.*/
    private JButton helpButton() {
        JButton help = new JButton("Help");
        help.setOpaque(false);
        help.setContentAreaFilled(false);
        help.setBorderPainted(false);
        return (help);
    }

    /**Method which creates the run button.
     *@return The run button.*/
    private JButton runButton() {
        JButton run = new JButton("Run");
        run.setOpaque(false);
        run.setContentAreaFilled(false);
        run.setBorderPainted(false);

        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIDataMaster.executeMeasures();
                ArrayList<Object> r = GUIDataMaster.getResults();
                for(Object o : r){
                    System.out.println(o);
                }
            }
        });

        return (run);
    }

    /**Method which creates the download button.
     *@return The download button.*/
    private JButton downloadButton() {
        JButton download = new JButton("Download");
        download.setOpaque(false);
        download.setContentAreaFilled(false);
        download.setBorderPainted(false);
        return (download);
    }
}
