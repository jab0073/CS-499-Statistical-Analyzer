package TableUtilities;

import java.util.Objects;

public class Cell {
    public enum CellStatus {NONE, WARNING, ERROR}
    public int row;
    public int column;
    public String data;
    public CellStatus status = CellStatus.NONE;
    public boolean selected = false;

    /**
     * This function returns the row of the current cell.
     *
     * @return The row of the cell.
     */
    public int getRow(){return row;}
    /**
     * Returns the column of the cell.
     *
     * @return The column of the cell.
     */
    public int getColumn(){return column;}
    public Cell(){}

    public Cell(int row, int col){
        this.row = row;
        this.column = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell cell)) return false;
        return getRow() == cell.getRow() && getColumn() == cell.getColumn() && selected == cell.selected && data.equals(cell.data) && status == cell.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn(), data, status, selected);
    }
}
