package FrontEndUtilities;

import BackEndUtilities.Sample;
import GUI.Cards.Card;
import Enums.CardTypes;
import GUI.Frame;
import Managers.ErrorManager;
import Managers.GraphManager;
import Managers.OutputManager;
import TableUtilities.DataTable;

import java.util.ArrayList;

/**
 * Manages data collected from elements on the GUI
 * Acts as a sort of "Controller" in an MVC type system
 */
public class GUIDataMaster {
    private static final ArrayList<GUIMeasure> measures = new ArrayList<>();
    private static final ArrayList<Object> results = new ArrayList<>();
    private static Frame frameReference;
    private static boolean biasCorrection = false;

    public static GUIMeasure getGUIMeasure(int index){
        return measures.get(index);
    }

    /**
     * Adds new GUIMeasure
     *
     * @param name The name of the measure. This will be displayed in the GUI.
     */
    public static void newGUIMeasure(String name){
        measures.add(new GUIMeasure(name));
        results.add(null);
    }

    /**
     * This function removes a measure from the list of measures
     *
     * @param index The index of the measure you want to remove.
     */
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
            measures.get(i).setBiasCorrection(biasCorrection);
            try{
                results.set(i, measures.get(i).execute());
            }catch (Exception e) {
                e.printStackTrace();
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

    /**
     * Clears the results, graphs, output, and errors
     */
    public static void flush(){
        for(Object s : results){
            s = null;
        }

        GraphManager.clearGraphs();
        OutputManager.clearOutput();
        ErrorManager.clearErrors();
    }

    /**
     * This function takes a card type and a measure, and swaps the middle card with the card type
     *
     * @param card The type of card to swap to.
     * @param measure The GUIMeasure object that contains the data to be displayed on the card.
     */
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

    /**
     * This function sets the frame reference to the frame that is passed in.
     *
     * @param frame The frame that the GUI is running in.
     */
    public static void setFrameReference(Frame frame){
        frameReference = frame;
    }

    /**
     * This function returns a reference to the frame object.
     *
     * @return The frameReference variable is being returned.
     */
    public static Frame getFrameReference(){return frameReference; }

    /**
     * This function adds a measure to the list of measures that the user has selected
     *
     * @param m The measure to be added to the list of measures.
     */
    public static void addMeasure(GUIMeasure m){
        measures.add(m);
        results.add(null);
        frameReference.updateRightPanelForLoad();
    }

    public static ArrayList<GUIMeasure> getAllMeasures() {
        return measures;
    }

    /**
     * It removes all the measures from the GUI and the array list
     */
    public static void removeAllMeasures(){
        int limit = measures.size();
        for(int i = 0; i < limit; i++){
            removeGUIMeasure(0);
        }
        frameReference.updateRightPanelForLoad();
    }

    /**
     * Create a blank data table, load it into the table, and update the right panel.
     */
    public static void newProject(){
        DataTable blank = new DataTable();

        for(int i = 0; i < 50; i++){
            blank.addRow(new Sample());
        }

        frameReference.getCellsTable().loadFromDT(blank);
        removeAllMeasures();

        frameReference.updateRightPanelForLoad();
    }

    public static void setBiasCorrection(boolean biasCorrection) {
        GUIDataMaster.biasCorrection = biasCorrection;
    }

    public static boolean isBiasCorrection() {
        return biasCorrection;
    }

    /**
     * Updates the list of available measures in the onscreen dropdown with any new measures that have been loaded
     */
    public static void updateMeasureListForDynamicLoad(){
        frameReference.updateMeasureDropdown();
    }
}
