package Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataSet {

    private List<String> data;
    private List<String> variables;

    public DataSet() {
        this.data = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    public DataSet(List<Double> data) {
        this.data = data.stream().map(String::valueOf).toList();
        this.variables = new ArrayList<>();
    }

    public DataSet(List<Double> data, List<String> variables) {
        this.data = data.stream().map(String::valueOf).toList();
        this.variables = variables;
    }

    public List<String> getData() {
        return data;
    }

    public List<Double> getDataAsDouble() {
        return data.stream().map(Double::parseDouble).collect(Collectors.toList());
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void addData(String data) {
        this.data.add(data);
    }

    public void addData(List<String> data) {
        this.data.addAll(data);
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
}
