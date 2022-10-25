package FrontEndUtilities;

import BackEndUtilities.*;
import Interfaces.IMeasure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIMeasure {
    private ArrayList<String>[] measureData;
    private String name = "";
    private int minimumSamples = 0;
    private List<String> requiredVariables = new ArrayList<>();
    private List<Double> variableValues = new ArrayList<>();

    public GUIMeasure(String name){
        IMeasure m = MeasureManager.getMeasure(name);
        this.name = name;
        this.minimumSamples = m.getMinimumSamples();
        this.requiredVariables = m.getRequiredVariables();

        for(String s : requiredVariables){
            variableValues.add(0.0);
        }

        this.measureData = new ArrayList[minimumSamples];

        for(ArrayList a : measureData){
            a = new ArrayList<String>();
        }
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
            variableValues.set(i, Double.parseDouble(value));
        }

    }

    public Object execute(){
        DataSet ds = new DataSet();

        for(ArrayList<String> arr : measureData){
            Sample s = new Sample();
            s.setData(arr);
            ds.addSample(s);
        }

        MeasureManager.getMeasure(name).setInputData(ds);

        for(int i = 0; i < requiredVariables.size(); i++){
            Expressions.addArgument(requiredVariables.get(i), variableValues.get(i).toString());
        }

        return MeasureManager.getMeasure(name).run();

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

        data.deleteCharAt(data.length()-1);

        return data.toString();
    }
}
