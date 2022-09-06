package Interop;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.io.Source;
import tech.tablesaw.io.xlsx.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UIToBE {

    public static Table loadCSVFromFile(String tableName, String filePath) {
        if(Files.exists(Path.of(filePath))) {
            Table newTable = Table.read().csv(filePath);
            newTable.setName(tableName);
            return newTable;
        }
        return null;
    }
    public static Table loadXLSXFromFile(String tableName, String filePath) {
        if(Files.exists(Path.of(filePath))) {
            XlsxReader xlsxr = new XlsxReader();
            Table newTable = xlsxr.read(Source.fromString(filePath));
            newTable.setName(tableName);
            return newTable;
        }
        return null;
    }

    public static Table loadDataFromUI(String tableName, List<List<Double>> data) {
        Table newTable = Table.create();
        for(List<Double> column : data) {
            DoubleColumn dc = DoubleColumn.create("", column);
            newTable.addColumns(dc);
        }
        newTable.setName(tableName);
        return newTable;
    }
}
