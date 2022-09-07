package BackEndUtilities;

import java.util.Objects;

public class Cell {
    private int row, column;
    private String data;
    private Boolean isSelected;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Cell(int row, int column, String data) {
        this.row = row;
        this.column = column;
        this.data = data;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void select() {
        this.isSelected = true;
    }

    public void deselect(){
        this.isSelected = false;
    }

    public void toggleSelected() {
        this.isSelected = !this.isSelected;
    }

    public void clear() {
        this.data = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell cell)) return false;
        return getRow() == cell.getRow() && getColumn() == cell.getColumn() && getData().equals(cell.getData()) && Objects.equals(isSelected, cell.isSelected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn(), getData(), isSelected);
    }
}
