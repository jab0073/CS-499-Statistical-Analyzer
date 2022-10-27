package GUI;

import Interop.UIServices;
import TableUtilities.DataTable;

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

    /**
     * Retrieves the data contained with cells on the table that have been selected by the user
     * @return String representation of the data held within the cells. Data is ordered in a Row-Then-Column order
     */
    public static String getSelectedData(){
        int[] r = table.getSelectedRows();
        int[] c = table.getSelectedColumns();

        StringBuilder data = new StringBuilder();

        for(int rS : r){
            for(int cS : c){
                Object value = table.getValueAt(rS, cS);

                if(value == null)
                    continue;
                if(value.toString().contains(" ")){

                }
                else{
                    data.append(value).append(",");
                }
            }
        }

        if(data.length() == 0){
            return null;
        }

        data.deleteCharAt(data.length()-1);

        return data.toString();
    }

    /**
     * Loads a CSV file into the onscreen chart
     * @param file String representation of the file path to the target file
     */
    public static void loadFile(String file){
        DataTable in = UIServices.fromCSV(file);

        if(in == null){
            return;
        }

        table.setModel(new DefaultTableModel(in.getRows().size()+20, table.getColumnCount()));

        for(int i = 0; i < in.getRows().size(); i++){
            TableUtilities.Row r = in.getRow(i);
            for(int j = 0; j < r.size(); j++){
                table.getModel().setValueAt(r.get(j).data, i, j);
            }
        }
    }

    public static void setColumnSelection(boolean value){
        System.out.println("Column: " + value);
        table.setRowSelectionAllowed(value);
    }

    public static void setRowSelection(boolean value){
        table.setColumnSelectionAllowed(value);
    }
}
