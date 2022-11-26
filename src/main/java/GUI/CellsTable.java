package GUI;

import Interop.UIServices;
import TableUtilities.DataTable;
import TableUtilities.Row;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class CellsTable extends JPanel {
    private static JTable table;

    public CellsTable(){
        this.add(scrollPane());
    }

    /**Method which creates a scroll pane to contain the JTable.
     *@return The scroll pane containg the JTable*/
    private JScrollPane scrollPane() {
        JScrollPane scrollPane = new JScrollPane(table(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(900, 750));
        return (scrollPane);
    }

    /**Method which creates the JTable.
     *@return The JTable*/
    private JTable table() {
        int numRows = 50;
        DefaultTableModel tableModel = new DefaultTableModel(numRows, 12);

        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setShowGrid(true);

        table.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        table.setGridColor(Color.GRAY);

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
                    String spaceLess = value.toString().replace(" ", "");

                    if(spaceLess.length() == 0){
                        continue;
                    }

                    data.append(spaceLess).append(",");
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

        int numRows = Math.max(50, in.getIndexOfLastRow()+20);
        int numCols = Math.max(12, in.getLongestRowSize());

        table.setModel(new DefaultTableModel(numRows, numCols));

        for(int i = 0; i < in.getRows().size(); i++){
            TableUtilities.Row r = in.getRow(i);
            for(int j = 0; j < r.size(); j++){
                table.getModel().setValueAt(r.get(j).data, i, j);
            }
        }
    }

    public static void loadXLSXFile(String file) throws IOException {
        DataTable in = UIServices.fromXLSX(file, 0);

        int numRows = Math.max(50, in.getIndexOfLastRow()+20);
        int numCols = Math.max(12, in.getLongestRowSize());

        table.setModel(new DefaultTableModel(numRows, numCols));

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

    public void setGridColor(java.awt.Color color){
        table.setShowGrid(true);
        table.setGridColor(color);
    }

    public DataTable getTableAsDT(){
        DataTable out = new DataTable();
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();

        for(int i = 0; i < numRows; i++){
            TableUtilities.Row row = new Row(i);
            for(int j = 0; j < numCols; j++){
                String data = (String) table.getModel().getValueAt(i,j);

                row.addCell(data);
            }

            row.removeTrailingEmptyCells();

            if(row.size() > 0){
                out.addRow(row);
            }

        }

        return out;
    }

    public void loadFromDT(DataTable in){
        if(in == null){
            return;
        }

        int numRows = Math.max(50, in.getIndexOfLastRow()+20);
        int numCols = Math.max(12, in.getLongestRowSize());

        table.setModel(new DefaultTableModel(numRows, numCols));

        for(int i = 0; i < in.getRows().size(); i++){
            TableUtilities.Row r = in.getRow(i);

            for(int j = 0; j < r.size(); j++){
                table.getModel().setValueAt(r.get(j).data, r.getIndex(), j);
            }
        }
    }
}
