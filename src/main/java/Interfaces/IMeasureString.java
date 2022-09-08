package Interfaces;

import BackEndUtilities.DataSet;

import java.math.BigDecimal;

public interface IMeasureString extends IMeasure{
    String name = "";

    void setName(String name);

    String getName();

    String function(DataSet inputData);

}
