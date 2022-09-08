package Interop;


import BackEndUtilities.DataSet;
import TableUtilities.DataTable;
import TableUtilities.Row;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class UIServices {

    static final Logger logger = LogManager.getLogger(UIServices.class.getName());

    public static DataTable fromCSV(String file) {
        DataTable dt = new DataTable();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> {
                DataSet ds = new DataSet();
                ds.addData(Arrays.asList(x));
                int index = r.indexOf(x);
                Row row = new Row(index, ds);
                dt.getRows().add(row.clone());
            });
        } catch (IOException | CsvException e) {
            logger.error("File could not be loaded.");
            return null;
        }
        return dt;
    }

    public static DataTable fromCSV(String tableName, String file) {
        DataTable dt = UIServices.fromCSV(file);
        if(dt != null) {
            dt.setTableName(tableName);
            return dt;
        }
        return null;
    }

    public static DataTable fromDelimitedFile(String file, char delimiter) {
        DataTable dt = new DataTable();
        CSVParser csvParser = new CSVParserBuilder().withSeparator(delimiter).build(); // custom separator
        try(CSVReader reader = new CSVReaderBuilder(
                new FileReader(file))
                .withCSVParser(csvParser)   // custom CSV parser
                .build()){
            List<String[]> r = reader.readAll();
            r.forEach(x -> {
                DataSet ds = new DataSet();
                ds.addData(Arrays.asList(x));
                int index = r.indexOf(x);
                Row row = new Row(index, ds);
                dt.getRows().add(row.clone());
            });
        } catch (IOException | CsvException e) {
            logger.error("File could not be loaded.");
            throw new RuntimeException(e);
        }
        return dt;
    }

    public static DataTable fromDelimitedFile(String tableName, String file, char delimiter) {
        DataTable dt = fromDelimitedFile(file, delimiter);
        dt.setTableName(tableName);
        return dt;
    }

    public static DataTable fromXLSX(String filePath, int sheetNumber) throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(sheetNumber);

        DataTable dt = new DataTable();
        for (org.apache.poi.ss.usermodel.Row cells : sheet) {
            DataSet ds = new DataSet();

            Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = cells.cellIterator();
            while (cellIterator.hasNext()) {
                org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();

                if(cell.getCellType() == CellType.STRING)
                    ds.addData(cell.getStringCellValue());
                else if (cell.getCellType() == CellType.NUMERIC) {
                    ds.addData(String.valueOf(cell.getNumericCellValue()));
                } else if (cell.getCellType() == CellType.FORMULA) {
                    switch (cell.getCachedFormulaResultType()) {
                        case BOOLEAN -> ds.addData(String.valueOf(cell.getBooleanCellValue()));
                        case NUMERIC -> ds.addData(String.valueOf(cell.getNumericCellValue()));
                        case STRING -> ds.addData(cell.getRichStringCellValue().getString());
                    }
                }
            }
            dt.addRow(ds.clone());
        }
        return dt;
    }

    public static DataTable fromXLSX(String tableName, String filePath, int sheetNumber) throws IOException {
        DataTable dt = UIServices.fromXLSX(filePath, sheetNumber);
        dt.setTableName(tableName);
        return dt;
    }


}
