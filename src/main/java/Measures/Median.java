package Measures;
import Interfaces.IMeasure;
import BackEndUtilities.DataSet;
import tech.tablesaw.api.DoubleColumn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

public class Median implements IMeasure {
    private String name = "median";

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double function(DataSet inputData) {
        List<Double> toSort = inputData.getDataAsDouble(true);
        toSort.sort(Comparator.naturalOrder());
        int size = toSort.size();
        BigDecimal median = BigDecimal.valueOf(size / 2).setScale(0, RoundingMode.HALF_UP);
        int middle = median.intValue();

        return toSort.get(middle);
    }
}
