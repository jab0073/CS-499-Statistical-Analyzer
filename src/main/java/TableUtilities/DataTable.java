package TableUtilities;

import BackEndUtilities.DataSet;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class DataTable {
    private List<Row> rows;

    private List<Column> columns;
    private List<Cell> selectedCells;
    private String tableName;
    private int size;

    public DataTable() {
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.tableName = "";
        this.size = 0;
    }

    public DataTable(String tableName) {
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.tableName = tableName;
        this.size = 0;
    }

    public Row getRow(int index) {
        return rows.get(index);
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
        fixSize();
    }

    private Row addRowObj() {
        Row row = new Row(rows.size());
        rows.add(row);
        return row;
    }

    public Row addRow(DataSet ds) {
        Row row = addRowObj();
        ds.getData().forEach(row::addCell);
        return row;
    }

    public Column addColumn(String header) {
        Column col = new Column(header);
        this.columns.add(col);
        return col;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getSize() {
        return size;
    }

    public void fixSize() {
        this.size = this.rows.size();
    }

    public void selectCell(int row, int col) {
        Cell cell = this.rows.get(row).get(col);
        cell.selected = true;
        if(!selectedCells.contains(cell))
            this.selectedCells.add(cell);
    }

    public void selectCell(Cell cell) {
        List<Cell> filtered = this.selectedCells.stream().filter(c -> (c.row == cell.row && c.column == cell.column)).toList();
        if (filtered.size() > 0) {
            filtered.forEach(c -> c.selected = false);
            this.selectedCells.removeAll(filtered);
        } else
            addToSelection(cell);
    }

    public void addToSelection(int row, int col) {
        if(row < 0 || row >= rows.size() || col < 0 || col >= columns.size()) {
            return;
        }
        else
            addToSelection(rows.get(row).get(col));
    }

    /**
     * This function won't work if called from the outside, use the public
     * version instead
     */
    private void addToSelection(Cell cell) {
        if (cell == null)
            return;
        cell.selected = true;
        this.selectedCells.add(cell);
    }

    public void clearSelected() {
        this.selectedCells.forEach(cell -> cell.selected = false);
        this.selectedCells.clear();
    }
    public List<Cell> getSelectedCells() {
        return this.selectedCells;
    }
    public Cell getCell(int rowIndex, int colIndex) {
        if (rowIndex < rows.size()) {
            Row row = rows.get(rowIndex);
            if ((colIndex) < row.size())
                return row.get(colIndex);
        }
        return null;
    }

    public void clearCellStatus() {
        rows.stream().map(Row::getCells).flatMap(Collection::stream).forEach(cell -> cell.status = Cell.CellStatus.NONE);
    }

    public DataSet selectionToDataSet() {
        DataSet ds = new DataSet();
        ds.addData(this.selectedCells.stream().map(c -> c.data).collect(Collectors.toList()));
        return ds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataTable dataTable)) return false;
        return getSize() == dataTable.getSize() && getRows().equals(dataTable.getRows()) && getTableName().equals(dataTable.getTableName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRows(), getTableName(), getSize());
    }

    @Override
    public String toString() {
        return this.tableName + "{" +
                "columns=" + rows +
                ", size=" + size +
                '}';
    }
}
