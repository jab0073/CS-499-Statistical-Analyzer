package Interfaces;

import BackEndUtilities.DataSet;
import BackEndUtilities.Expressions;
import GUI.CardTypes;
import Graphing.DataFormat;
import Graphing.GraphTypes;
import Measures.Measures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public interface IMeasure {

    Logger logger = LogManager.getLogger(IMeasure.class.getName());

    String getName();

    int getMinimumSamples();

    List<String> getRequiredVariables();

    void setInputData(DataSet inputData);

    boolean validate();

    DataSet getInputData();

    Object run();

    boolean isGraphable();

    List<GraphTypes> getValidGraphs();

    default DataFormat getOutputFormat(){ return DataFormat.SINGLE_DOUBLE; };

    default CardTypes getCardType(){ return CardTypes.ONE_DATA_NO_VARIABLE; };

}
