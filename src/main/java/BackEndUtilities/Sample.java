package BackEndUtilities;

import Interfaces.IValidator;
import Measures.Measures;
import Validators.DataValidator;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
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

    public boolean save(String fileName) {
        Gson gson = new Gson();
        try {
            Writer writer = new FileWriter(fileName);
            gson.toJson(this, writer);
            writer.flush();
            writer.close();
            Measures.getLogger().debug("Sample written to " + fileName);
            return true;
        } catch (IOException e) {
            Measures.getLogger().error("Sample failed to write to " + fileName);
            return false;
        }
    }

    public static Sample load(String fileName) {
        Gson gson = new Gson();
        try {
            Sample obj =  gson.fromJson(new FileReader(fileName), Sample.class);
            logger.debug("Successfully loaded sample from " + fileName);
            return obj;
        } catch (FileNotFoundException e) {
            logger.error("!!! Failed to load sample from " + fileName);
            return null;
        }
    }

    public boolean exportCSV(String fileName) {
        File file = new File(fileName);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            writer.writeNext(data.toArray(String[]::new));

            // closing writer connection
            writer.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
