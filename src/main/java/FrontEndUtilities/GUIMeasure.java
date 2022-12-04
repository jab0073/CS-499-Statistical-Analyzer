package FrontEndUtilities;

import BackEndUtilities.*;
import Enums.CardTypes;
import Enums.DataFormat;
import Managers.ErrorManager;
import Managers.GraphManager;
import Enums.GraphTypes;
import Interfaces.BiasCorrectable;
import Interfaces.IMeasure;
import Managers.MeasureManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstraction of IMeasure interface intended to be used in GUI code in place of IMeasure.
 * Contains the same data as the reference IMeasure, however data collection does not convert data to the DataSet format required by
 * IMeasure until it is time to execute the measure, hopefully improving performance
 */
public class GUIMeasure {
    private final ArrayList<String>[] measureData;
    private final String name;
    private final int minimumSamples;
    private final List<String> requiredVariables;
    private final List<String> variableValues = new ArrayList<>();
    private final boolean isGraphable;
    private final List<GraphTypes> validGraphs = new ArrayList<>();
    private GraphTypes selectedGraph = null;
    private final DataFormat outputFormat;
    private final CardTypes cardType;
    private boolean biasCorrection = false;

    /**
     * GUIMeasure Constructor
     * @param name the name of the measure
     */
    public GUIMeasure(String name){
        //Get reference of IMeasure with the same name to copy data from
        IMeasure m = MeasureManager.getMeasure(name);

        //Copy data from m
        this.name = name;
        this.minimumSamples = m.getMinimumSamples();
        this.requiredVariables = m.getRequiredVariables();

        for(String s : requiredVariables){
            variableValues.add("");
        }

        this.measureData = new ArrayList[minimumSamples];

        for(ArrayList a : measureData){
            a = new ArrayList<String>();
        }

        this.isGraphable = m.isGraphable();
        if(isGraphable){
            this.validGraphs.addAll(m.getValidGraphs());
        }

        this.outputFormat = m.getOutputFormat();

        this.cardType = m.getCardType();
    }

    /**
     * Add data to the Measure
     * @param append Whether to append the data to the measure's data, or to replace the data entirely. True = Append, False = Replace
     * @param index The index of the sample to add to
     * @param data The data to add
     */
    public void addData(boolean append, int index, String... data){
        if(index > measureData.length-1){
            return;
        }

        if(append){
            measureData[index].addAll(Arrays.asList(data));
        }else{
            measureData[index] = new ArrayList<String>();
            measureData[index].addAll(Arrays.asList(data));
        }

        int i = 0;
    }

    /**
     * Sets the value of a variable used by the Measure
     * @param variableName The name of the variable
     * @param value the value to set the variable to
     */
    public void setVariable(String variableName, String value){
        if(!requiredVariables.contains(variableName)){
            return;
        }

        if(isNumeric(value)) {
            int i = requiredVariables.indexOf(variableName);
            variableValues.set(i, value);
        }

    }

    /**
     * Retrieves a list of data, a list of variables, and a list of variable values, and then runs the measure
     * with the given data and variables
     *
     * @return The result of the measure.
     */
    public Object execute(){
        DataSet ds = new DataSet();

        //Convert String data into a DataSet for the measure
        for(ArrayList<String> arr : measureData){
            Sample s = new Sample();
            s.setData(arr);
            ds.addSample(s);
        }

        //Reference the measure to execute
        IMeasure m = MeasureManager.getMeasure(name);

        //Check if the measure to be executed actually exists
        if(m == null){
            ErrorManager.sendErrorMessage(name, "The program does not have this Custom Measure installed");
            return null;
        }

        //Set data required for the measure
        m.setInputData(ds);

        for(int i = 0; i < requiredVariables.size(); i++){
            Expressions.setArgument(requiredVariables.get(i), variableValues.get(i));
        }

        if(m instanceof BiasCorrectable){
            if(biasCorrection){
                ((BiasCorrectable) m).biasCorrected();
            }else{
                ((BiasCorrectable) m).nonBiasCorrected();
            }
        }

        //Execute the measure
        Object r = m.run();

        //Graph the measure with the selected graph, if applicable
        if(isGraphable && selectedGraph != GraphTypes.NONE){
            GraphManager.graphOutput(selectedGraph, r, this);
        }

        return r;

    }

    /**
     * Checks if a string is numeric
     * @param str the string to analyze
     * @return whether the string is numeric
     */
    private boolean isNumeric(String str){
        if(str == null){
            return false;
        }
        try{
            double d = Double.parseDouble(str);
        }catch(NumberFormatException e){
            return false;
        }

        return true;
    }

    public String getName(){
        return name;
    }

    /**
     * Retrieves the measure's data as a CSV style string
     * @return the data as a string
     */
    public String getDataAsString(){
        StringBuilder data = new StringBuilder();

        for(ArrayList<String> sA : measureData){
            if(sA == null){
                data.append(" ");
                continue;
            }
            for(String s : sA){
                data.append(s).append(",");
            }
        }

        if(data.length() > 0){
            data.deleteCharAt(data.length()-1);
        }

        return data.toString();
    }

    /**
     * Retrieves the data of a specific set of data as a CSV style string
     * @param index the index of the set of data to retrieve
     * @return the data as a string
     */
    public String getDataAsString(int index){
        StringBuilder data = new StringBuilder();

        ArrayList<String> sample = measureData[index];
        if(sample == null){
            data.append(" ");
            return "";
        }

        for(String s : sample){
            data.append(s).append(",");
        }


        if(data.length() > 0){
            data.deleteCharAt(data.length()-1);
        }

        return data.toString();
    }

    /**
     * Get the currently set value of a variable
     * @param index the index of the variable to retrieve
     * @return String representation of the variable value
     */
    public String getVariableValue(int index){
        String var = variableValues.get(index);

        if(var == null){
            return "";
        }

        return var;
    }

    public ArrayList<String>[] getData(){
        return this.measureData;
    }

    public void setSelectedGraph(GraphTypes graph){
        this.selectedGraph = graph;
    }

    public GraphTypes getSelectedGraph(){
        return this.selectedGraph;
    }

    public List<GraphTypes> getValidGraphs(){return this.validGraphs; }

    public DataFormat getOutputFormat(){ return outputFormat; }

    public CardTypes getCardType(){ return cardType; }

    public int getMinimumSamples(){ return minimumSamples; }

    public int getNumVariables(){ return requiredVariables.size(); }

    public String getVariableName(int i){ return requiredVariables.get(i) ; }

    public void setBiasCorrection(boolean biasCorrection) {
        this.biasCorrection = biasCorrection;
    }
}
