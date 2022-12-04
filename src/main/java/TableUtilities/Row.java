package TableUtilities;

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

    public void removeTrailingEmptyCells(){
        int i = 0;
        int startOfTrail = 0;

        //Find the start of any trailing empty cells
        for(Cell c : cells){
            if(c.data != null && !c.data.replace(" ", "").isEmpty()){
                startOfTrail = i+1;
            }

            i++;
        }

        //Make sure to not delete all the data from a filled row
        if(startOfTrail == 0 && (cells.get(0).data != null && !cells.get(0).data.replace(" ", "").isEmpty())){
            return;
        }

        if (cells.size() > startOfTrail) {
            cells.subList(startOfTrail, cells.size()).clear();
        }
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
