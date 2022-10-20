package FrontEndUtilities;

import java.util.ArrayList;

public class GUIDataMaster {
    private static ArrayList<GUIMeasure> measures = new ArrayList<>();
    private static ArrayList<Object> results = new ArrayList<>();

    public static GUIMeasure getGUIMeasure(int index){
        return measures.get(index);
    }

    public static void newGUIMeasure(String name){
        measures.add(new GUIMeasure(name));
        results.add(null);
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
}
