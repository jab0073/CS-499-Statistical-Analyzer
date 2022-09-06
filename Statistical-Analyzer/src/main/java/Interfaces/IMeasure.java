package Interfaces;

import Utilities.DataSet;
import tech.tablesaw.api.DoubleColumn;

public interface IMeasure {
    String name = "";

    void setName(String name);

    String getName();

    double function(DataSet inputData);

}
