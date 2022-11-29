package BackEndUtilities;

import Managers.ErrorManager;
import Interfaces.IValidator;
import Validators.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Sample implements Cloneable{
    private List<String> data;
    private static final Logger logger = LogManager.getLogger(Sample.class.getName());
    public IValidator.ValidationStatus status;


    public Sample() {
        logger.debug("Creating empty Sample");
        this.data = new ArrayList<>();
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    public Sample(List<Double> data) {
        logger.debug("Creating Sample with size of " + data.size());
        this.data = data.stream().map(String::valueOf).toList();
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    public Sample(Double... data) {
        this.data = Arrays.stream(data).map(String::valueOf).toList();
        logger.debug("Creating Sample with size of " + this.data.size());
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    public Sample(String... data) {
        this.data = Arrays.asList(data);
        logger.debug("Creating Sample with size of " + this.data.size());
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    public Sample(List<Double> data, List<String> variables) {
        this.data = data.stream().map(String::valueOf).toList();
        logger.debug("Creating Sample with size of " + this.data.size());
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    /**
     * This function returns the data associated with the DataSet.
     *
     * @return A list of strings
     */
    public List<String> getData() {
        return data;
    }

    /**
     * If evaluate is true, return the result of evaluating the expression, otherwise return the data as a list of
     * BigDecimal
     *
     * @return A list of BigDecimal objects.
     */
    public List<Double> getDataAsDouble() {
        if(Expressions.isEvaluationOn()) {
            return Expressions.eval(this);
        }
        else {
            return data.stream().map(s -> {
                try {
                    return Double.parseDouble(s);
                } catch (NumberFormatException e) {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }

    /**
     * Evaluate the expression and store the result in the data field. This method is destructive to the original data.
     */
    public void evaluate() {
        this.data = Expressions.eval(this).stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * This function sets the data and size variables to the data and size of the data passed in.
     *
     * @param data The data to be displayed in the list.
     */
    public void setData(List<String> data) {
        if(data == null){
            ErrorManager.sendErrorMessage("Sample", "Data to be added to sample is null");
            return;
        }
        this.data = data;
    }

    /**
     * This function adds a string to the data array and increments the size variable.
     *
     * @param data The data to be added to the DataSet
     */
    public void addData(String data) {
        this.data.add(data);
    }

    /**
     * Add all the elements of the data list to the DataSet
     * @param data The data to be added to the DataSet.
     */
    public void addData(List<String> data) {
        this.data.addAll(data);
    }


    /**
     * Convert a list of doubles to a DataSet.
     *
     * @param data The data to be used for the DataSet.
     * @return A DataSet object
     */
    public static Sample fromDoubleList(List<Double> data) {
        Sample ds = new Sample();
        ds.data = data.stream().map(String::valueOf).collect(Collectors.toList());
        return ds;
    }

    /**
     * This function returns the size of the DataSet.
     *
     * @return The size of the array.
     */
    public int getSize() {
        return this.data.size();
    }

    public boolean validate() {
        return DataValidator.validate(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sample sample)) return false;
        return getData().equals(sample.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData());
    }

    @Override
    public String toString() {
        return "Sample{" +
                "data=" + this.data +
                ", size=" + this.getSize() +
                '}';
    }

    @Override
    public Sample clone() {
        try {
            return (Sample) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
