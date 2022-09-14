package BackEndUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Sample implements Cloneable{
    private List<String> data;
    private List<String> variables;
    private static final Logger logger = LogManager.getLogger(Sample.class.getName());


    public Sample() {
        logger.debug("Creating empty Sample");
        this.data = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    public Sample(List<Double> data) {
        logger.debug("Creating Sample with size of " + data.size());
        this.data = data.stream().map(String::valueOf).toList();
        this.variables = new ArrayList<>();
    }

    public Sample(Double... data) {
        this.data = Arrays.stream(data).map(String::valueOf).toList();
        logger.debug("Creating Sample with size of " + this.data.size());
        this.variables = new ArrayList<>();
    }

    public Sample(List<Double> data, List<String> variables) {
        this.data = data.stream().map(String::valueOf).toList();
        logger.debug("Creating Sample with size of " + this.data.size());
        this.variables = variables;
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
     * @param evaluate If true, the expressions stored in the DataSet will be evaluated before returning the data.
     * @return A list of BigDecimal objects.
     */
    public List<BigDecimal> getDataAsDouble(Boolean evaluate) {
        if(evaluate) {
            return Expressions.eval(this);
        }
        else {
            return data.stream().map(s -> {
                try {
                    return new BigDecimal(s);
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
     * This function returns a list of strings that represent the variables to be used in the expressions
     *
     * @return A list of variables
     */
    public List<String> getVariables() {
        return variables;
    }

    /**
     * This function sets the variables of the class to the variables passed in.
     *
     * @param variables A list of variables.
     */
    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    /**
     * This function adds a variable to the list of variables.
     *
     * @param variable The variable to be added to the list of variables.
     */
    public void addVariables(String variable) {
        this.variables.add(variable);
    }

    /**
     * This function adds a list of variables to the list of variables.
     *
     * @param variables The list of variables to add to the existing variables.
     */
    public void addVariables(List<String> variables) {
        this.variables.addAll(variables);
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
     * It takes a list of doubles and a list of variables, and returns a DataSet object
     *
     * @param data a list of data.
     * @param variables A list of variables for the data set.
     * @return A DataSet object
     */
    public static Sample fromDoubleList(List<Double> data, List<String> variables) {
        Sample ds = new Sample();
        ds.data = data.stream().map(String::valueOf).collect(Collectors.toList());
        ds.variables = variables;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sample sample)) return false;
        return getData().equals(sample.getData()) && getVariables().equals(sample.getVariables());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getVariables());
    }

    @Override
    public String toString() {
        return "Sample{" +
                "data=" + this.data +
                ", variables=" + this.variables +
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
