package BackEndUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataSet {

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

    public List<String> getData() {
        return data;
    }

    public List<Double> getDataAsDouble(Boolean evaluate) {
        if(evaluate) {
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

    /*
    This method is destructive to the original data.
     */
    public void evaluate() {
        this.data = Expressions.eval(this).stream().map(String::valueOf).collect(Collectors.toList());
    }

    public void setData(List<String> data) {
        this.data = data;
        this.size = data.size();
    }

    public void addData(String data) {
        this.data.add(data);
        this.size += 1;
    }

    public void addData(List<String> data) {
        this.data.addAll(data);
        this.size += data.size();
    }

    public void switchToAlternate() {
        List<String> tmp = this.data;
        this.data = this.additionalData;
        this.additionalData = tmp;
    }

    public List<Double> getAdditionalDataAsDouble(Boolean evaluate) {
        if(evaluate) {
            return Expressions.eval(this.additionalData, this.variables);
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

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    public void addVariables(String variable) {
        this.variables.add(variable);
    }

    public void addVariables(List<String> variables) {
        this.variables.addAll(variables);
    }

    public static DataSet fromDoubleList(List<Double> data) {
        DataSet ds = new DataSet();
        ds.data = data.stream().map(String::valueOf).collect(Collectors.toList());
        return ds;
    }

    public static DataSet fromDoubleList(List<Double> data, List<String> variables) {
        DataSet ds = new DataSet();
        ds.data = data.stream().map(String::valueOf).collect(Collectors.toList());
        ds.variables = variables;
        return ds;
    }

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
}
