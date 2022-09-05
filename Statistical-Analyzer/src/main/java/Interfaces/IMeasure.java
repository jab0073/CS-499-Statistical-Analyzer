package Interfaces;

import tech.tablesaw.api.DoubleColumn;

public interface IMeasure {
    public String name = "";

    public void setName(String name);

    public String getName();

    public double function(DoubleColumn inputData);

}
