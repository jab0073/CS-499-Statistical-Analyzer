package Interfaces;

import BackEndUtilities.DataSet;

public interface IMeasure {
    String name = "";

    void setName(String name);

    String getName();

    double function(DataSet inputData);

}
