package BackEndUtilities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataSet implements Cloneable{

    private List<String> data;

    private List<String> additionalData;
    private List<String> variables;
    private int size;

    public DataSet() {
        this.data = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.size = 0;
    }

    public DataSet(List<Double> data) {
        this.data = data.stream().map(String::valueOf).toList();
        this.variables = new ArrayList<>();
        this.size = data.size();
    }

    public DataSet(List<Double> data, List<String> variables) {
        this.data = data.stream().map(String::valueOf).toList();
        this.variables = variables;
        this.size = data.size();
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
        this.size = data.size();
    }

    /**
     * This function adds a string to the data array and increments the size variable.
     *
     * @param data The data to be added to the DataSet
     */
    public void addData(String data) {
        this.data.add(data);
        this.size += 1;
    }

    /**
     * Add all the elements of the data list to the DataSet
     * @param data The data to be added to the DataSet.
     */
    public void addData(List<String> data) {
        this.data.addAll(data);
        this.size += data.size();
    }

    /**
     * Switch the data and additionalData fields.
     */
    public void switchToAlternate() {
        List<String> tmp = this.data;
        this.data = this.additionalData;
        this.additionalData = tmp;
    }

    /**
     * If evaluate is true, then evaluate the expression, otherwise return the data as a list of BigDecimal
     *
     * @param evaluate If true, the additional data will be evaluated using the variables. If false, the additional data
     * will be returned as is with any null values removed.
     * @return A list of BigDecimal objects.
     */
    public List<BigDecimal> getAdditionalDataAsDouble(Boolean evaluate) {
        if(evaluate) {
            return Expressions.eval(this.additionalData, this.variables);
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
    public static DataSet fromDoubleList(List<Double> data) {
        DataSet ds = new DataSet();
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
    public static DataSet fromDoubleList(List<Double> data, List<String> variables) {
        DataSet ds = new DataSet();
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
        return this.size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataSet dataSet)) return false;
        return getData().equals(dataSet.getData()) && getVariables().equals(dataSet.getVariables());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getVariables());
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "data=" + data +
                ", variables=" + variables +
                ", size=" + size +
                '}';
    }

    @Override
    public DataSet clone() {
        try {
            return (DataSet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
