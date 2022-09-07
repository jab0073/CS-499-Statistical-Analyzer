package BackEndUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Column {
    private List<Cell> cells;
    private int nextRowIndex;

    public Column() {
        this.cells = new ArrayList<>();
        this.nextRowIndex = 0;
    }

    public Column(List<Cell> cells) {
        this.cells = cells;
        this.nextRowIndex = cells.size();
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setData(List<Cell> cells) {
        this.cells = cells;
        this.nextRowIndex = cells.size();
    }

    public int getNextRowIndex(){
        return this.nextRowIndex;
    }

    public void append(Cell cell) {
        cell.setRow(this.nextRowIndex);
        cells.add(cell);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Column column)) return false;
        return getNextRowIndex() == column.getNextRowIndex() && getCells().equals(column.getCells());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCells(), getNextRowIndex());
    }
}
