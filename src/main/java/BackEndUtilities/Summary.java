package BackEndUtilities;

import java.util.Map;
import java.util.stream.Collectors;

public class Summary {

    private static String latestSummary = null;

    public static String summarize(DataSet dataset, Map<String, String> measuresRan) {
        Summary.latestSummary = dataset.toString();
        Summary.latestSummary += measuresRan.keySet().stream()
                .map(key -> key + ": " + measuresRan.get(key))
                .collect(Collectors.joining("\n"));
        return Summary.latestSummary;
    }

    public String getLastSummary() {
        return Summary.latestSummary;
    }

}
