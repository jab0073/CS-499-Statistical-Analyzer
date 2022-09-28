package Interfaces;

import BackEndUtilities.DataSet;


/**
 * Interface that all measures implement.
 * This is to ensure compatibility with the ClassMap functions.
 */
@Deprecated
public interface IMeasure<R> {
    String name = "";

    R function(DataSet inputData);
}
