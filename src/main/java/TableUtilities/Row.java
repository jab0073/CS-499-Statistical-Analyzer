package TableUtilities;

import BackEndUtilities.DataSet;
import BackEndUtilities.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Row implements Cloneable{
    private final int index;
    private List<Cell> cells;

    public Row(int index) {
        this.index = index;
        this.cells = new ArrayList<>();
    }

    public Row(int index, Sample data) {
        this.index = index;
        this.cells = new ArrayList<>();
        this.cells = data.getData().stream().map(d -> {
           Cell cell = new Cell(this.index, this.cells.size());
           cell.data = d;
           return cell;
        }).collect(Collectors.toList());
    }

    public int getIndex() {
        return index;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void addCell(String data){
        Cell cell= new Cell();
        cell.row = index;
        cell.column = cells.size();
        cell.data = data;
        cells.add(cell);
    }

    public Sample toSample() {
        Sample ds = new Sample();
        for(Cell cell : this.cells) {
            ds.addData(cell.data);
        }
        return ds;
    }

    public Cell get(int index) {
        return cells.get(index);
    }

    public int size() {
        return cells.size();
    }

    @Override
    public Row clone() {
        try {
            return (Row) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
