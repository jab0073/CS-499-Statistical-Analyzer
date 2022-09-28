package Measures;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import Interfaces.IMeasure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;

@Deprecated
public class SignTest implements IMeasure<String> {
    private static final Logger logger = LogManager.getLogger(IMeasure.class.getName());
    private final String name = Constants.sign;
    public final int minimumSamples = 2;

    @Override
    public String function(DataSet inputData) {
        logger.debug("Running " + name);

        StringBuilder result = new StringBuilder();
        List<BigDecimal> x;
        List<BigDecimal> y;
        if (inputData != null && inputData.getNumberOfSamples() >= 2) {
            try {
                x = inputData.getSample(0).getDataAsBigDecimal();
                y = inputData.getSample(1).getDataAsBigDecimal();

            } catch (IndexOutOfBoundsException e) {
                logger.debug("Out of Bounds Exception");
                return null;
            }

            int size = Math.min(x.size(), y.size());

            for(int i = 0; i < size; i++){
                BigDecimal a = x.get(i);
                BigDecimal b = y.get(i);

                result.append(a).append(", ").append(b).append(", ");

                if(a.compareTo(b) < 0){
                    result.append("-");
                }else if(a.compareTo(b) > 0){
                    result.append("+");
                }else{
                    result.append("N/A");
                }

                result.append("\n");
            }
        }

        return result.toString();

    }

}
