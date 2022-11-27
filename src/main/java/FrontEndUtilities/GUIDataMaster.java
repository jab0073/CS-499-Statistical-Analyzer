package FrontEndUtilities;

import BackEndUtilities.Sample;
import GUI.Card;
import GUI.CardTypes;
import GUI.Frame;
import Graphing.GraphManager;
import TableUtilities.DataTable;

import java.util.ArrayList;

public class GUIDataMaster {
    private static final ArrayList<GUIMeasure> measures = new ArrayList<>();
    private static final ArrayList<Object> results = new ArrayList<>();
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
                ErrorManager.sendErrorMessage(measures.get(i).getName(), "A critical error has occurred");
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

        if(card == CardTypes.BLANK)
            return;

        for(int i = 0; i < measure.getMinimumSamples(); i++){
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

    public static void addMeasure(GUIMeasure m){
        measures.add(m);
        results.add(null);
        frameReference.updateRightPanelForLoad();
    }

    public static ArrayList<GUIMeasure> getAllMeasures() {
        return measures;
    }

    public static void removeAllMeasures(){
        int limit = measures.size();
        for(int i = 0; i < limit; i++){
            removeGUIMeasure(0);
        }

        frameReference.updateRightPanelForLoad();
    }

    public static void newProject(){
        DataTable blank = new DataTable();

        for(int i = 0; i < 50; i++){
            blank.addRow(new Sample());
        }

        frameReference.getCellsTable().loadFromDT(blank);
        removeAllMeasures();

        frameReference.updateRightPanelForLoad();
    }
}
