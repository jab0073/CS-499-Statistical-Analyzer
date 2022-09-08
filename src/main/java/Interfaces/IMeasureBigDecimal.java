package Interfaces;

import BackEndUtilities.DataSet;

import java.math.BigDecimal;

/**
 * Interface for BigDecimal returning measures.
 */
public interface IMeasureBigDecimal extends IMeasure {
    String name = "";

    void setName(String name);

    String getName();

    BigDecimal function(DataSet inputData);

}
