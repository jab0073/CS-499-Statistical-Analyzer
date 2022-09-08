package Interfaces;

import BackEndUtilities.DataSet;

import java.math.BigDecimal;

/**
 * Interface for string returning measures.
 */
public interface IMeasureString extends IMeasure{
    String name = "";

    void setName(String name);

    String getName();

    String function(DataSet inputData);

}
