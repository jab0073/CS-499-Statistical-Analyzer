package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CellsTable {
    /**Method for returning the panel containing a scroll pane which contains the JTable.
     *@return The panel containing the cells table.*/
    public JPanel cellsPanel() {
        JPanel panel = new JPanel();
        panel.add(scrollPane());
        return (panel);
    }

    /**Method which creates a scroll pane to contain the JTable.
     *@return The scroll pane containg the JTable*/
    private JScrollPane scrollPane() {
        JScrollPane scrollPane = new JScrollPane(table());
        scrollPane.setPreferredSize(new Dimension(900, 750));
        return (scrollPane);
    }

    /**Method which creates the JTable.
     *@return The JTable*/
    private JTable table() {
        String[] headings = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
        int numRows = 50;
        DefaultTableModel tableModel = new DefaultTableModel(numRows, headings.length);
        JTable table = new JTable(tableModel);
        return (table);
    }
}
