package TableUtilities;

import BackEndUtilities.DataSet;
import BackEndUtilities.Sample;
import Interfaces.IValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;


public class DataTable {
    private static final Logger logger = LogManager.getLogger(DataTable.class.getName());
    public IValidator.ValidationStatus status;
    private List<Row> rows;

    private List<Column> columns;
    private List<Cell> selectedCells;
    private String tableName;
    private int size;

    public DataTable() {
        logger.debug("Creating empty DataTable");
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.tableName = "";
        this.size = 0;
        this.selectedCells = new ArrayList<>();
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    public DataTable(String tableName) {
        logger.debug("Creating new DataTable: " + tableName);
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.tableName = tableName;
        this.size = 0;
        this.selectedCells = new ArrayList<>();
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    /**
     * This function returns the row at the given index.
     *
     * @param index The index of the row to get.
     * @return The row at the specified index.
     */
    public Row getRow(int index) {
        return rows.get(index);
    }

    /**
     * This function returns a list of rows.
     *
     * @return A list of rows.
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * This function sets the rows of the table to the given rows, and then fixes the size of the table.
     *
     * @param rows The list of rows in the table.
     */
    public void setRows(List<Row> rows) {
        this.rows = rows;
        fixSize();
    }

    /**
     * Add a new row to the table and return it.
     *
     * @return A new row object is being returned.
     */
    private Row addRowObj() {
        Row row = new Row(rows.size());
        rows.add(row);
        return row;
    }

    /**
     * "Add a row to the table, and add a cell to the row for each data item in the data set."
     *
     * The first line of the function creates a new row object. The second line iterates
     * over the data set's data items, and for each data item, it calls the row's addCell() function
     *
     * @param ds The DataSet object that contains the data to be added to the row.
     * @return A Row object
     */
    public Row addRow(Sample ds) {
        Row row = addRowObj();
        ds.getData().forEach(row::addCell);
        return row;
    }

    /**
     * Add a column to the table and return it.
     *
     * @param header The header of the column.
     * @return A Column object
     */
    public Column addColumn(String header) {
        Column col = new Column(header);
        this.columns.add(col);
        return col;
    }

    /**
     * This function returns the table name
     *
     * @return The table name.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * This function sets the table name
     *
     * @param tableName The name of the table to be created.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * This function returns the size of the list.
     *
     * @return The size of the array.
     */
    public int getSize() {
        return size;
    }

    /**
     * This function sets the size of the table to the number of rows in the table.
     */
    public void fixSize() {
        this.size = this.rows.size();
    }

    /**
     * Selects the cell at the given row and column.
     *
     * @param row The row of the cell you want to select.
     * @param col The column of the cell to select
     */
    public void selectCell(int row, int col) {
        Cell cell = this.rows.get(row).get(col);
        cell.selected = true;
        if(!selectedCells.contains(cell))
            this.selectedCells.add(cell);
    }

    /**
     * If the cell is already selected, deselect it. Otherwise, select it
     *
     * @param cell The cell to be selected
     */
    public void selectCell(Cell cell) {
        List<Cell> filtered = this.selectedCells.stream().filter(c -> (c.row == cell.row && c.column == cell.column)).toList();
        if (filtered.size() > 0) {
            filtered.forEach(c -> c.selected = false);
            this.selectedCells.removeAll(filtered);
        } else
            addToSelection(cell);
    }

    /**
     * If the row and column are valid, then add the cell to the selection
     *
     * @param row the row of the cell to be added to the selection
     * @param col The column index of the cell to be added to the selection.
     */
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

    /**
     * For each cell in the selectedCells list, set the cell's selected property to false, then clear the selectedCells
     * list.
     */
    public void clearSelected() {
        this.selectedCells.forEach(cell -> cell.selected = false);
        this.selectedCells.clear();
    }
    /**
     * Returns a list of all the cells that are currently selected.
     *
     * @return A list of cells that are selected.
     */
    public List<Cell> getSelectedCells() {
        return this.selectedCells;
    }
    /**
     * If the row index is less than the number of rows, and the column index is less than the number of columns in the
     * row, return the cell at the specified row and column index.
     *
     * @param rowIndex The row index of the cell you want to get.
     * @param colIndex The column index of the cell you want to get.
     * @return A Cell object
     */
    public Cell getCell(int rowIndex, int colIndex) {
        if (rowIndex < rows.size()) {
            Row row = rows.get(rowIndex);
            if ((colIndex) < row.size())
                return row.get(colIndex);
        }
        return null;
    }

    /**
     * For each cell in the table, set its status to NONE.
     */
    public void clearCellStatus() {
        rows.stream().map(Row::getCells).flatMap(Collection::stream).forEach(cell -> cell.status = Cell.CellStatus.NONE);
    }


    /**
     * This function takes the selected cells and returns a DataSet containing the data from those cells.
     *
     * @return A DataSet object containing the data from the selected cells.
     */
    public Sample selectionToSample() {
        Sample ds = new Sample();
        ds.addData(this.selectedCells.stream().map(c -> c.data).collect(Collectors.toList()));
        return ds;
    }

    public DataSet toDataSet() {
        DataSet ds = new DataSet();
        this.rows.forEach(r-> {
            logger.debug("Converting new row to sample and adding to dataset");
            ds.addSample(r.toSample());
        });
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
