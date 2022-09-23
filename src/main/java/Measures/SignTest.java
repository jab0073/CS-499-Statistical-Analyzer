package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import Interfaces.IMeasure;
import org.apache.commons.math3.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SignTest implements IMeasure<Pair<Integer, Integer>> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());

    @Override
    public Pair<Integer, Integer> function(DataSet inputData) {
        String name = Constants.sign;
        logger.debug("Running " + name);

        //TODO: finish
        return null;
    }
}
