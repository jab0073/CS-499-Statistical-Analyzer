package Interfaces;

import BackEndUtilities.DataSet;

import java.math.BigDecimal;

public interface IMeasure {
    String name = "";

    void setName(String name);

    String getName();

    Object function(DataSet inputData);
}
