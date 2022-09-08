package Interfaces;

import BackEndUtilities.DataSet;

import java.math.BigDecimal;

public interface IMeasureBigDecimal extends IMeasure {
    String name = "";

    void setName(String name);

    String getName();

    BigDecimal function(DataSet inputData);

}
