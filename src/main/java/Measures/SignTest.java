package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import Interfaces.IMeasure;
import org.apache.commons.math3.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SignTest implements IMeasure<Pair<Integer, Integer>> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    public final int minimumSamples = 2;

    @Override
    public Pair<Integer, Integer> function(DataSet inputData) {
        String name = Constants.sign;
        logger.debug("Running " + name);

        List<Double> subtracted = new ArrayList<>();

        for(int i = 0 ; i < inputData.getSample(0).getSize() ; i++) {
            double value1 = Double.parseDouble(inputData.getSample(0).getData().get(i));
            double value2 = Double.parseDouble(inputData.getSample(1).getData().get(i));
            subtracted.add(value2 - value1);
        }

        int positive = subtracted.stream().filter(d-> {
            return d >= 0;
        }).toList().size();

        int negative = subtracted.size() - positive;

        return new Pair<>(positive, negative);
    }
}
