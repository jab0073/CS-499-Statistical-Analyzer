package BackEndUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataTable {
    private List<DataSet> columns;
    private String tableName;
    private int size;

    public DataTable() {
        this.columns = new ArrayList<>();
        this.tableName = "";
        this.size = 0;
    }

    public DataTable(String tableName) {
        this.columns = new ArrayList<>();
        this.tableName = tableName;
        this.size = 0;
    }

    public DataSet getColumn(int index) {
        return columns.get(index);
    }

    public List<DataSet> getColumns() {
        return columns;
    }

    public void setColumns(List<DataSet> columns) {
        this.columns = columns;
        fixSize();
    }

    public void addColumn(DataSet ds) {
        this.columns.add(ds);
        fixSize();
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
        this.size = this.columns.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataTable dataTable)) return false;
        return getSize() == dataTable.getSize() && getColumns().equals(dataTable.getColumns()) && getTableName().equals(dataTable.getTableName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColumns(), getTableName(), getSize());
    }

    @Override
    public String toString() {
        return this.tableName + "{" +
                "columns=" + columns +
                ", size=" + size +
                '}';
    }
}
