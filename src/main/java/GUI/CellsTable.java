package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CellsTable {
    private static JTable table;
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

        table = new JTable(tableModel);
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        return (table);
    }

    public static String getSelectedData(){
        int[] r = table.getSelectedRows();
        int[] c = table.getSelectedColumns();

        StringBuilder data = new StringBuilder();

        for(int rS : r){
            for(int cS : c){
                Object value = table.getValueAt(rS, cS);

                if(value == null)
                    continue;

                data.append(value).append(",");
            }
        }

        data.deleteCharAt(data.length()-1);

        return data.toString();
    }
}
