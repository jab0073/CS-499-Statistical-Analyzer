package Interfaces;

import BackEndUtilities.DataSet;

import java.math.BigDecimal;

/**
 * Top Level interface that all measure interfaces extend.
 * This is to ensure compatibility with the ClassMap functions.
 * If a measure needs to return a data type that is not currently
 * implemented, simply create a new interface named "IMeasure[DataType]"
 * then in the ClassMap add a new function for measures that will implement
 * that interface, then in the top level function add a new check for if
 * the appropriate function returned null or not.
 */
public interface IMeasure {
    String name = "";

    void setName(String name);

    String getName();

    Object function(DataSet inputData);
}
