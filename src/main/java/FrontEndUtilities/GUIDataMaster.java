package FrontEndUtilities;

import GUI.Card;
import GUI.CardTypes;
import GUI.Frame;
import Graphing.GraphManager;

import javax.swing.*;
import java.util.ArrayList;

public class GUIDataMaster {
    private static ArrayList<GUIMeasure> measures = new ArrayList<>();
    private static ArrayList<Object> results = new ArrayList<>();
    private static Frame frameReference;

    public static GUIMeasure getGUIMeasure(int index){
        return measures.get(index);
    }

    public static void newGUIMeasure(String name){
        measures.add(new GUIMeasure(name));
        results.add(null);
    }

    public static void removeGUIMeasure(int index){
        if(measures.size() > 0){
            measures.remove(index);
            results.remove(index);
        }

    }

    /**
     * Retrieves the names of all measures currently registered
     * @return Array of names
     */
    public static String[] getMeasureNames(){
        String[] names = new String[measures.size()];

        for(int i = 0; i < measures.size(); i++){
            names[i] = measures.get(i).getName();
        }

        return names;
    }

    /**
     * Executes all measures
     * @return Whether the measures executed without issues. A return of false indicates that a measure encountered an error or was unable to perform the calculation
     */
    public static boolean executeMeasures(){
        boolean success = true;

        for(int i = 0; i < measures.size(); i++){
            try{
                results.set(i, measures.get(i).execute());
            }catch (Exception e) {
                results.set(i, null);
                ErrorManager.sendErrorMessage(measures.get(i).getName(), "A critical error has occured");
                success = false;
            }
        }

        for(Object o : results){
            if (o == null) {
                success = false;
                break;
            }
        }

        return success;
    }

    public static ArrayList<Object> getResults(){
        return results;
    }

    public static void flush(){
        for(Object s : results){
            s = null;
        }

        GraphManager.clearGraphs();
        OutputManager.clearOutput();
        ErrorManager.clearErrors();
    }

    public static void swapMiddleCard(CardTypes card, GUIMeasure measure){
        Card c = Frame.swapCard(card);

        for(int i = 0; i < measure.getMinimumSamples(); i++){
            System.out.println("Setting data " + measure.getDataAsString(i) + i + " on card " + card.name());
            c.setDataArea(i, measure.getDataAsString(i));
        }

        for(int j = 0; j < measure.getNumVariables(); j++){
            c.setVariableArea(j, measure.getVariableValue(j));
            c.setVariableLabel(j, measure.getVariableName(j));
        }
    }

    public static void setFrameReference(Frame frame){
        frameReference = frame;
    }

    public static Frame getFrameReference(){return frameReference; }
}
